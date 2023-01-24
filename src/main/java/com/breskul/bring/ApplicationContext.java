package com.breskul.bring;

import com.breskul.bring.exceptions.NoSuchBeanException;
import com.breskul.bring.exceptions.NoUniqueBeanException;

import java.util.Map;

/**
 * Central interface to provide configuration for an application.
 * This is read-only while the application is running, but may be reloaded if the implementation supports this.
 */
public interface ApplicationContext {
    /** Retrieve the bean instance that uniquely matches the given object type, if any.
     *
     * @param beanType - the type of instance stored in context
     * @return The instance of type stored in the context
     * @param <T> A type of instance
     * @throws NoSuchBeanException Bean not found in the context
     * @throws NoUniqueBeanException Bean is not unique in the context
     */
    <T> T getBean(Class<T> beanType) throws NoSuchBeanException, NoUniqueBeanException;

    /** Retrieve an instance, which may be shared or independent, of the specified bean.
     *
     * @param name - The name of instance stored in context
     * @param beanType - The type of instance stored in context
     * @return The instance of type from context
     * @param <T> A type of instance
     * @throws NoSuchBeanException Bean not found in the context
     */
    <T> T getBean(String name, Class<T> beanType) throws NoSuchBeanException;

    /** Return all beans instance matches the given type
     *
     * @param beanType  the type of instance stored in context
     * @return The map of context instances
     * @param <T> A type of instance
     */
    <T> Map<String, T> getAllBeans(Class<T> beanType);
}
