package com.breskul.bring;

import com.breskul.bring.annotations.Autowired;
import com.breskul.bring.annotations.Bean;
import com.breskul.bring.annotations.Component;
import com.breskul.bring.annotations.Configuration;
import com.breskul.bring.exceptions.BeanInitializingException;
import com.breskul.bring.exceptions.NoSuchBeanException;
import com.breskul.bring.exceptions.NoUniqueBeanException;
import com.breskul.bring.exceptions.NuSuchBeanConstructor;
import org.apache.commons.text.WordUtils;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <h3>Standalone application context, accepting component classes as input</h3>
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
            var config = configClass.getDeclaredConstructor().newInstance();
            Method[] methods = configClass.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Bean.class)) {
                    String beanName = resolveBeanName(method.getAnnotation(Bean.class).value(), method.getReturnType());
                    method.setAccessible(true);
                    var bean = method.invoke(config);
                    context.put(beanName, bean);
                }
            }
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
                injectBeanViaConstructor(beanInstance.getClass(), beanInstance);
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (NoSuchBeanException e) {
            throw new NoSuchBeanException("NO_SUCH_BEAN_EXCEPTION");
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
            field.setAccessible(true);
            if (field.isAnnotationPresent(Autowired.class)) {
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
    private <T> void injectBeanViaConstructor(Class<?> beanClass, T beanInstance) throws NoSuchFieldException, IllegalAccessException {
        Map<Class<?>, String> fieldTypeStringNameMap = new HashMap<>();
        for (Field field : beanClass.getDeclaredFields()) {
            fieldTypeStringNameMap.put(field.getType(), field.getName());
        }
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
     * <h3>Store bean in the context</h3>
     *
     * @param beanName The name of bean
     * @param bean     The instance of bean
     */
    private <T> void storeBean(String beanName, T bean) {
        Objects.requireNonNull(beanName);
        Objects.requireNonNull(bean);
        LOGGER.info("Bring Framework: Creating bean >>> {}: {}", beanName, bean);
        context.put(beanName, bean);
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

    /**
     * <h3>Creates an instance of bean by beanType</h3>
     *
     * @param beanType - {@link Class} of bean
     * @return instance of the created bean
     */
    private <T> T createInstance(Class<T> beanType) {
        var constructor = Arrays.stream(beanType.getConstructors())
                .findFirst()
                .orElseThrow(() -> {
                    throw new NuSuchBeanConstructor("Bean type: " + beanType.getName());
                });
        try {
            return beanType.cast(constructor.newInstance());
        } catch (Exception ex) {
            throw new BeanInitializingException("Bean type: " + beanType.getName());
        }
    }

    @Override
    public <T> T getBean(Class<T> beanType) throws NoSuchBeanException, NoUniqueBeanException {
        Map<String, T> beans = getAllBeans(beanType);
        if (beans.size() > 1) {
            throw new NoUniqueBeanException("Bean type: " + beanType.getName());
        }
        return beans.values().stream()
                .findFirst()
                .orElseThrow(() -> {
                    throw new NoSuchBeanException("Bean type: " + beanType.getName());
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
                    throw new NoSuchBeanException("Bean name: " + name + "; Bean type: " + beanType.getName());
                });
    }

    @Override
    public <T> Map<String, T> getAllBeans(Class<T> beanType) {
        return context.entrySet().stream()
                .filter(filterByBeanType(beanType))
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> beanType.cast(entry.getValue())));
    }
}