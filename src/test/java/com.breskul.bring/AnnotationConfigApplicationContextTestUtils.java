package com.breskul.bring;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AnnotationConfigApplicationContextTestUtils {

    public static ApplicationContext getContext() {
        return new AnnotationConfigApplicationContext("com.breskul.bring");
    }

    public static <T> T storingBeanByName(ApplicationContext context, Class<T> beanType, String beanName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method methodCreateInstance = context.getClass().getDeclaredMethod("createInstance", Class.class);
        methodCreateInstance.setAccessible(true);
        var beanInstance = beanType.cast(methodCreateInstance.invoke(context, beanType));

        Method methodStoreBean = context.getClass().getDeclaredMethod("storeBean", String.class, Object.class);
        methodStoreBean.setAccessible(true);
        methodStoreBean.invoke(context, beanName, beanInstance);

        return beanInstance;
    }

    public static <T> void injectBean(ApplicationContext context, T bean) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method methodInjectBean = context.getClass().getDeclaredMethod("injectBeanWithFields", Object.class);
        methodInjectBean.setAccessible(true);
        methodInjectBean.invoke(context, bean);
    }
}
