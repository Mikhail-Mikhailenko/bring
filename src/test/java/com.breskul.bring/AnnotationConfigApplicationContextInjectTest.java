package com.breskul.bring;

import org.junit.jupiter.api.*;

import java.lang.reflect.InvocationTargetException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AnnotationConfigApplicationContextInjectTest {
    private static ApplicationContext context;

    @BeforeAll
    public static void createContext() {
        context = AnnotationConfigApplicationContextTestUtils.getContext();
    }

    @Test
    @Order(1)
    public void checkInjectionsWithoutAutowried() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        TestComponent1 testComponent1 = AnnotationConfigApplicationContextTestUtils.storingBeanByName(context,TestComponent1.class,"testComponent1");
        AnnotationConfigApplicationContextTestUtils.injectBean(context,testComponent1);
    }

    @Test
    @Order(2)
    public void checkInjectionsTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        TestComponent1 testComponent1 = AnnotationConfigApplicationContextTestUtils.storingBeanByName(context,TestComponent1.class,"testComponent1");
        TestComponent2 testComponent2 = AnnotationConfigApplicationContextTestUtils.storingBeanByName(context,TestComponent2.class,"testComponent2");
        AnnotationConfigApplicationContextTestUtils.injectBean(context,testComponent2);
        Assertions.assertEquals(testComponent1,testComponent2.getTestComponent1());
    }

}
