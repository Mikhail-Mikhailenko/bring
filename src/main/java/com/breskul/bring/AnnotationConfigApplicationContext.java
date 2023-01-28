package com.breskul.bring;

import com.breskul.bring.annotations.Component;
import com.breskul.bring.exceptions.BeanInitializingException;
import com.breskul.bring.exceptions.NoSuchBeanException;
import com.breskul.bring.exceptions.NoUniqueBeanException;
import com.breskul.bring.exceptions.NuSuchBeanConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Standalone application context, accepting component classes as input — in particular @Configuration-annotated classes,
 * but also plain @Component types
 */
public class AnnotationConfigApplicationContext implements ApplicationContext {
    private static final Logger LOGGER = LoggerFactory.getLogger(BringApplication.class);
    private final Map<String, Object> context = new ConcurrentHashMap<>();
    private final String packagePath;

    public AnnotationConfigApplicationContext(String packageName) {
        Objects.requireNonNull(packageName);
        this.packagePath = packageName;
        initiateContext();
    }

    /**
     * Scanning a package for classes annotated by {@link Component}.
     * It creates an instances of such classes and put them into context.
     */
    private void initiateContext() {
        Reflections reflections = new Reflections(packagePath, Scanners.TypesAnnotated);

        for (Class<?> bean : reflections.getTypesAnnotatedWith(Component.class)) {
            String beanCustomName = bean.getAnnotation(Component.class).value();
            String beanName = StringUtils.defaultIfEmpty(beanCustomName, WordUtils.uncapitalize(bean.getSimpleName()));
            try {
                context.put(beanName, bean.getDeclaredConstructor().newInstance());
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException |
                     NoSuchMethodException e) {
                LOGGER.error("Can't create an instance of {} class", beanName);
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Store bean in the context
     *
     * @param beanName The name of bean
     * @param bean     The instance of bean
     * @param <T>      The generic type of instance
     */
    private <T> void storeBean(String beanName, T bean) {
        Objects.requireNonNull(beanName);
        Objects.requireNonNull(bean);
        LOGGER.info("Bring Framework: Creating bean >>> {}: {}", beanName, bean);
        context.put(beanName, bean);
    }

    /**
     * Maps filter by bean type
     *
     * @param beanType The Class of type
     * @param <T>      type of the bean
     * @return Predicate - filter isAssignableFrom for values types
     */
    private <T> Predicate<Map.Entry<String, Object>> filterByBeanType(Class<T> beanType) {
        return entry -> beanType.isAssignableFrom(entry.getValue().getClass());
    }

    /**
     * Creating an instance of bean by beanType
     *
     * @param beanType - The type of bean instance
     * @param <T>
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