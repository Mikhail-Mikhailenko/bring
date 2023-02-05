package com.breskul.bring;

import com.breskul.bring.annotations.Autowired;
import com.breskul.bring.annotations.Bean;
import com.breskul.bring.annotations.Component;
import com.breskul.bring.annotations.Configuration;
import com.breskul.bring.exceptions.NoSuchBeanException;
import com.breskul.bring.exceptions.NoUniqueBeanException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.text.WordUtils;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

/**
 * Standalone application context, accepting component classes as input
 * <p>Classes should be annotated with  {@link Configuration} and {@link Component}</p>
 */
public class AnnotationConfigApplicationContext implements ApplicationContext {
    private static final Logger LOGGER = LoggerFactory.getLogger(BringApplication.class);
    private final Map<String, Object> context = new ConcurrentHashMap<>();
    private final String[] packagePaths;

    public AnnotationConfigApplicationContext(String... packageNames) {
        Objects.requireNonNull(packageNames);
        this.packagePaths = packageNames;
        initiateContext();
    }

    /**
     * <h3>Scanning a package for classes annotated by {@link Component}.</h3>
     * <p>It creates an instances of such classes and put them into context.</p>
     */
    private void initiateContext() {
        loadComponents();
        loadConfigurations();
        autoWireBeans();
        proxyConfigurations();
    }

    private void proxyConfigurations() {
        Map<String, Object> configurationBeans = context.entrySet().stream()
                .filter(entry -> entry.getValue().getClass().isAnnotationPresent(Configuration.class))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        configurationBeans.forEach(this::createConfigurationProxy);
    }

    /**
     * <h3>Create proxy for {@link Configuration} annotation</h3>
     *
     * @param name    {@link String}
     * @param beanInstance {@link Object}
     */
    private void createConfigurationProxy(String name, Object beanInstance) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(beanInstance.getClass());
        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
            if (method.isAnnotationPresent(Bean.class)) {
                var beanName = resolveBeanName(method.getAnnotation(Bean.class).value(), method.getReturnType());
                return context.get(beanName);
            }
            return proxy.invokeSuper(obj, args);
        });
        context.put(name, enhancer.create());
    }

    private void loadComponents() {
        Arrays.stream(packagePaths)
                .map(packagePath -> new Reflections(packagePath, Scanners.TypesAnnotated))
                .flatMap(reflections -> reflections.getTypesAnnotatedWith(Component.class).stream())
                .forEach(this::registerComponentsBeansInContext);
    }

    private void registerComponentsBeansInContext(Class<?> beanClass) {
        try {
            var constructor = beanClass.getConstructor();
            var beanInstance = constructor.newInstance();
            String beanName = resolveBeanName(beanClass.getAnnotation(Component.class).value(), beanClass);
            context.put(beanName, beanInstance);
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadConfigurations() throws RuntimeException {
        Arrays.stream(packagePaths)
                .map(packagePath -> new Reflections(packagePath, Scanners.TypesAnnotated))
                .flatMap(reflections -> reflections.getTypesAnnotatedWith(Configuration.class).stream())
                .forEach(this::registerConfigurationsBeansInContext);
    }

    private void registerConfigurationsBeansInContext(Class<?> configClass) {
        try {
            var beanInstance = configClass.getConstructor().newInstance();
            Method[] methods = configClass.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Bean.class)) {
                    String beanName = resolveBeanName(method.getAnnotation(Bean.class).value(), method.getReturnType());
                    method.setAccessible(true);
                    var bean = method.invoke(beanInstance);
                    context.put(beanName, bean);
                }
            }
            String configBeanName = resolveBeanName(configClass.getAnnotation(Configuration.class).value(), configClass);
            context.put(configBeanName, beanInstance);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                 | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private void autoWireBeans() {
        try {
            for (Map.Entry<String, Object> entry : context.entrySet()) {
                var beanInstance = entry.getValue();
                injectBeanViaAutowiredAnnotation(beanInstance.getClass(), beanInstance);
                Map<Class<?>, String> fieldTypeStringNameMap = new HashMap<>();
                Class<?> beanClass = beanInstance.getClass();
                for (Field field : beanClass.getDeclaredFields()) {
                    fieldTypeStringNameMap.put(field.getType(), field.getName());
                }
                injectBeanViaConstructor(beanInstance.getClass(), beanInstance, fieldTypeStringNameMap);
                injectBeanViaSetter(beanInstance.getClass(), beanInstance, fieldTypeStringNameMap);
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <h3>Injects Beans via {@link Autowired} annotation</h3>
     *
     * @param beanClass    {@link Class}
     * @param beanInstance {@link Object}
     */
    private <T> void injectBeanViaAutowiredAnnotation(Class<?> beanClass, T beanInstance) throws IllegalAccessException {
        for (Field field : beanClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                field.setAccessible(true);
                var autowiredBeansInstance = getBean(field.getType());
                field.set(beanInstance, autowiredBeansInstance);
            }
        }
    }

    /**
     * <h3>Injects Bean via {@link Constructor}</h3>
     *
     * @param beanClass    {@link Class}
     * @param beanInstance {@link Object}
     */
    private <T> void injectBeanViaConstructor(Class<?> beanClass, T beanInstance, Map<Class<?>, String> fieldTypeStringNameMap) throws NoSuchFieldException, IllegalAccessException {

        Constructor<?>[] constructors = beanClass.getConstructors();
        for (Constructor<?> constructor : constructors) {
            for (Parameter parameter : constructor.getParameters()) {
                Class<?> parameterType = parameter.getType();
                var autowiredBeansInstance = getBean(parameterType);
                String parameterName = fieldTypeStringNameMap.get(parameterType);
                Field field = beanClass.getDeclaredField(parameterName);
                field.setAccessible(true);
                field.set(beanInstance, autowiredBeansInstance);
            }
        }
    }
    /**
     * <h3>Injects Bean via Setter </h3>
     *
     * @param beanClass    {@link Class}
     * @param beanInstance {@link Object}
     */

    private <T> void injectBeanViaSetter(Class<?> beanClass, T beanInstance, Map<Class<?>, String> fieldTypeStringNameMap) throws NoSuchFieldException, IllegalAccessException {
        Method[] beanMethods = beanClass.getDeclaredMethods();
        for (Method method: beanMethods){
            if (method.getName().startsWith("set") && method.isAnnotationPresent(Bean.class)){
                Class<?>[] parameterTypes = method.getParameterTypes();
                for (Class<?> parameterType: parameterTypes){
                    var autowiredBeansInstance = getBean(parameterType);
                    String parameterName = fieldTypeStringNameMap.get(parameterType);
                    Field field = beanClass.getDeclaredField(parameterName);
                    field.setAccessible(true);
                    field.set(beanInstance, autowiredBeansInstance);
                }
            }
        }
    }

    /**
     * <h3>Resolves bean name</h3>
     *
     * @param beanClass {@link Class}
     * @return {@link String}
     */
    private String resolveBeanName(String beanCustomName, Class<?> beanClass) {
        if (beanCustomName != null && !beanCustomName.isEmpty()) {
            return beanCustomName;
        }
        return WordUtils.uncapitalize(beanClass.getSimpleName());
    }

    /**
     * <h3>Maps filter by bean type</h3>
     *
     * @param beanType {@link Class} of bean
     * @return Predicate - filter isAssignableFrom for values types
     */
    private <T> Predicate<Map.Entry<String, Object>> filterByBeanType(Class<T> beanType) {
        return entry -> beanType.isAssignableFrom(entry.getValue().getClass());
    }

    @Override
    public <T> T getBean(Class<T> beanType) throws NoSuchBeanException, NoUniqueBeanException {
        Map<String, T> beans = getAllBeans(beanType);
        if (beans.size() > 1) {
            throw new NoUniqueBeanException(this.getClass().getName(), beanType.getName());
        }
        return beans.values().stream()
                .findFirst()
                .orElseThrow(() -> {
                    throw new NoSuchBeanException(this.getClass().getName(), beanType.getName());
                });
    }

    @Override
    public <T> T getBean(String name, Class<T> beanType) throws NoSuchBeanException {
        Objects.requireNonNull(name);
        return getAllBeans(beanType).entrySet().stream()
                .filter(entry -> entry.getKey().equals(name))
                .map(Map.Entry::getValue)
                .findAny()
                .orElseThrow(() -> {
                    throw new NoSuchBeanException(this.getClass().getName(), name, beanType.getName());
                });
    }

    @Override
    public <T> Map<String, T> getAllBeans(Class<T> beanType) {
        return context.entrySet().stream()
                .filter(filterByBeanType(beanType))
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> beanType.cast(entry.getValue())));
    }
}
