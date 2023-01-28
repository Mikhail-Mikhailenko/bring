package com.breskul.bring;

import com.breskul.bring.exceptions.NoSuchBeanException;
import com.breskul.bring.exceptions.NoUniqueBeanException;
import org.junit.jupiter.api.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@DisplayName("ApplicationContext tests")
public class ApplicationContextTest {

    private static ApplicationContext context;

    @BeforeAll
    public static void createContext() {
        context = new AnnotationConfigApplicationContext("com.breskul.bring");
    }

    @Test
    public void createContextTest() {
        Assertions.assertNotNull(context);
    }

    @Test
    @Order(1)
    public void storeBeanTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException{
        TestComponent1 beanInstance = storingBeanByName(TestComponent1.class,"testComponent1");
        Assertions.assertNotNull(beanInstance);
        Assertions.assertEquals(context.getAllBeans(TestComponent1.class).size(),1);
    }

    @Test
    @Order(2)
    public void getBeanTest(){
        var beanInstance = context.getBean(TestComponent1.class);
        Assertions.assertNotNull(beanInstance);
    }

    @Test
    @Order(3)
    public void getBeanByNameTest(){
        var beanInstance = context.getBean("testComponent1", TestComponent1.class);
        Assertions.assertNotNull(beanInstance);
    }

    @Test
    @Order(4)
    public void getAllBeansTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException{
        storingBeanByName(TestComponent1.class,"testComponnent2");
        Assertions.assertEquals(context.getAllBeans(TestComponent1.class).size(),2);
    }

    @Test
    @Order(5)
    public void getBeanNoUniqueBeanExceptionTest() {
        Assertions.assertThrows(NoUniqueBeanException.class,()->context.getBean(TestComponent1.class));
    }

    @Test
    @Order(6)
    public void getBeanNoSuchBeanException(){
        Assertions.assertThrows(NoSuchBeanException.class,()->context.getBean(Integer.class));
    }


    public <T> T storingBeanByName(Class<T> beanType, String beanName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method methodCreateInstance = context.getClass().getDeclaredMethod("createInstance", Class.class);
        methodCreateInstance.setAccessible(true);
        var beanInstance = beanType.cast(methodCreateInstance.invoke(context,beanType));

        Method methodStoreBean = context.getClass().getDeclaredMethod("storeBean", String.class, Object.class);
        methodStoreBean.setAccessible(true);
        methodStoreBean.invoke(context, beanName, beanInstance);

        return beanInstance;
    }

}
