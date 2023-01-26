package com.breskul.bring;

import com.breskul.bring.exceptions.NoSuchBeanException;
import com.breskul.bring.exceptions.NoUniqueBeanException;
import org.junit.jupiter.api.*;

import java.lang.reflect.InvocationTargetException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AnnotationConfigApplicationContextTest {

    private static ApplicationContext context;

    @BeforeAll
    public static void createContext() {
        context = AnnotationConfigApplicationContextTestUtils.getContext();
    }

    @Test
    public void createContextTest() {
        Assertions.assertNotNull(context);
    }

    @Test
    @Order(1)
    public void storeBeanTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException{
        TestComponent1 beanInstance = AnnotationConfigApplicationContextTestUtils.storingBeanByName(context,TestComponent1.class,"testComponent1");
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
        AnnotationConfigApplicationContextTestUtils.storingBeanByName(context, TestComponent1.class,"testComponnent2");
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

}
