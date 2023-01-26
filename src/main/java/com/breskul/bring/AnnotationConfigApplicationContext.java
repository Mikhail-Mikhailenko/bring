package com.breskul.bring;

import com.breskul.bring.annotations.Autowired;
import com.breskul.bring.exceptions.BeanInitializingException;
import com.breskul.bring.exceptions.NoSuchBeanException;
import com.breskul.bring.exceptions.NoUniqueBeanException;
import com.breskul.bring.exceptions.NuSuchBeanConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
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
        scan();
    }

    /**
     * Scanning a package for classes or methods annotated by @Component and @Bean
     */
    private void scan() {
    }

    private void inject() {
        // inject by fields
        context.values().forEach(this::injectBeanWithFields);
        // TODO: inject by setters
    }

    /**
     * Inject bean fields with @Autowired annotation.
     * Called after filling the context and instances initializing
     *
     * @param bean Bean for injection
     * @param <T>
     */
    private <T> void injectBeanWithFields(T bean) {
        Field[] fields = bean.getClass().getDeclaredFields();
        Arrays.stream(fields)
                .filter(field -> !field.getType().isPrimitive() && field.isAnnotationPresent(Autowired.class))
                .forEach(field -> injectField(field, field.getType(), bean));
    }

    /**
     * Fill value into the field
     *
     * @param field    the field of bean type
     * @param beanType the type of bean
     * @param bean     the instance of bean
     * @param <T>      bean instance generic
     * @param <R>      field value generic
     */
    private <T, R> void injectField(Field field, Class<R> beanType, T bean) {
        Objects.requireNonNull(bean);
        R value = getBean(beanType);
        try {
            field.setAccessible(true);
            field.set(bean, value);
        } catch (IllegalAccessException ex) {
            LOGGER.error("Bring Framework: Injection failed >>> Bean: {}}. Field name: {}. Value: {}", bean, field.getName(), value);
        } finally {
            LOGGER.info("Bring Framework: Injection success >>> Bean: {}}. Field name: {}. Value: {}", bean, field.getName(), value);
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
        return (entry) -> entry.getValue().getClass().isAssignableFrom(beanType);
    }

    /**
     * Creating an instance of bean by beanType
     *
     * @param beanType - The type of bean instance
     * @param <T> bean type generic
     * @return instance of the created bean
     */
    private <T> T createInstance(Class<T> beanType) {
        var constructor = Arrays.stream(beanType.getConstructors())
                .findFirst()
                .orElseThrow(() -> {
                    throw new NuSuchBeanConstructor("Bean type: " + beanType.getName());
                });
        // TODO: Constructor injection
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