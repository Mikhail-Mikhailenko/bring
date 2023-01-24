package com.breskul.applicationContext.applicationContext;

import com.breskul.applicationContext.annotations.Autowire;
import com.breskul.applicationContext.annotations.Bean;
import com.breskul.applicationContext.exceptions.NoSuchBeanException;
import com.breskul.applicationContext.exceptions.NoUniqueBeanException;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ApplicationContextImpl implements ApplicationContext {

    private final Map<String, Object> beansMap = new ConcurrentHashMap<>();

    public ApplicationContextImpl(String packageName) throws Exception {
        var reflections = new Reflections(packageName);
        var futureBeans = reflections.getTypesAnnotatedWith(Bean.class);
        registerBeansInContext(futureBeans);
        autoWireBeans();
    }

    private void autoWireBeans() throws Exception {
        for (Map.Entry<String, Object> entry : beansMap.entrySet()) {
            var beanInstance = entry.getValue();

            for (Field field : beanInstance.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Autowire.class)) {
                    var autowiredBeansInstance = getBean(field.getType());
                    field.set(beanInstance, autowiredBeansInstance);
                }
            }
        }
    }

    private void registerBeansInContext(Set<Class<?>> classSet) throws Exception {
        for (Class<?> beanClass : classSet) {
            var constructor = beanClass.getConstructor();
            var beanInstance = constructor.newInstance();
            beansMap.put(resolveBeanName(beanClass), beanInstance);
        }

    }

    private String resolveBeanName(Class<?> beanClass) {
        var beanName = beanClass.getAnnotation(Bean.class).value();
        if (!beanName.isEmpty()) {
            return beanName;
        }
        return processBeanName(beanClass.getSimpleName());
    }

    private String processBeanName(String beanName) {
        return beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
    }

    /**
     * <h3>Returns the bean itself</h3>
     * @param beanType {@link Class} beanType to which instance will be casted
     *
     * @return {@link Object} beanInstance
     */
    @Override
    public <T> T getBean(Class<T> beanType) {
        var beansMap = getAllBeans(beanType);
        if (beansMap.size() >= 2) {
            throw new NoUniqueBeanException();
        } else if (beansMap.isEmpty()) {
            throw new NoSuchBeanException();
        }
        return beanType.cast(beansMap.get(resolveBeanName(beanType)));
    }

    /**
     * <h3>Returns the bean itself</h3>
     * @param beanName {@link String} beanName
     * @param beanType {@link Class} beanType to which instance will be casted
     *
     * @return {@link Object} beanInstance
     */

    @Override
    public <T> T getBean(String beanName, Class<T> beanType) {
        var beanInstanceOptional = Optional.ofNullable(beansMap.get(beanName));
        return beanType.cast(beanInstanceOptional.orElseThrow(NoSuchBeanException::new));
    }

    /**
     * <h3>Returns the map of all beans mathced by type.</h3>
     * @param beanType - bean type filter criteria
     *
     * @return {@link Map} where {@link String} is beanName and {@link Object} is beanInstance
     */
    @Override
    public <T> Map<String, Object> getAllBeans(Class<T> beanType) {
        return beansMap.entrySet().stream()
                .filter(entry -> beanType.isAssignableFrom(entry.getValue().getClass()))
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> beanType.cast(entry.getValue())));
    }
}
