package com.breskul.applicationContext.applicationContext;

import java.util.Map;

public interface ApplicationContext {

    <T> T getBean(Class<T> beanType);

    <T> T getBean(String beanName, Class<T> beanType);

    <T> Map<String, Object> getAllBeans(Class<T> beanType);
}
