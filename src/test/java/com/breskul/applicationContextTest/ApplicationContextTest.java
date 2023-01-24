package com.breskul.applicationContextTest;

import com.breskul.applicationContext.MessageServiceDemo;
import com.breskul.applicationContext.PrinterServiceDemo;
import com.breskul.applicationContext.applicationContext.ApplicationContextImpl;
import com.breskul.applicationContext.exceptions.NoSuchBeanException;
import com.breskul.applicationContext.exceptions.NoUniqueBeanException;
import com.breskul.applicationContextTest.testBeansCorrectMap.SameBean1;
import com.breskul.applicationContextTest.testBeansCorrectMap.SameBean2;
import com.breskul.applicationContextTest.testBeansCorrectMap.SameBeanInterface;
import org.junit.jupiter.api.*;


import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@DisplayName("ApplicationContext tests")
public class ApplicationContextTest {
    private final String packageName = "com.breskul.applicationContext";

    @Nested
    @Order(1)
    @DisplayName("1. ApplicationContext Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class AppplicationContextInterfaceTest {

        @Test
        @Order(1)
        @DisplayName("Bean retrieved by type and name correctly")
        void getBeanByType() throws Exception {
            var applicationContext = new ApplicationContextImpl(packageName);
            MessageServiceDemo messageServiceDemo = applicationContext.getBean(MessageServiceDemo.class);
            assertEquals(messageServiceDemo.getClass(), MessageServiceDemo.class);

            MessageServiceDemo messageServiceDemoByName = applicationContext.getBean("coolMessageServiceDemo", MessageServiceDemo.class);
            assertEquals(messageServiceDemoByName.getClass(), MessageServiceDemo.class);

            PrinterServiceDemo printerServiceDemo = applicationContext.getBean("printerServiceDemo", PrinterServiceDemo.class);
            assertEquals(printerServiceDemo.getClass(), PrinterServiceDemo.class);
        }

        @Test
        @Order(2)
        @DisplayName("NoSuchBeanException is thown when there is no bean")
        void getNoSuchBeanException() throws Exception {
            var applicationContext = new ApplicationContextImpl(packageName);
            assertThrows(NoSuchBeanException.class, () -> applicationContext.getBean(Field.class));
            assertThrows(NoSuchBeanException.class, () -> applicationContext.getBean("PrinterServiceDemo", PrinterServiceDemo.class));
        }

        @Test
        @Order(3)
        @DisplayName("NoUniqueBeanException is thrown when there are 2 or more same class beans")
        void getNoUniqueBeanException() throws Exception {
            String testBeansPackageName = "com.breskul.applicationContextTest.testBeansCorrectMap";
            var applicationContext = new ApplicationContextImpl(testBeansPackageName);
            assertThrows(NoUniqueBeanException.class, () -> applicationContext.getBean(SameBeanInterface.class));
        }

        @Test
        @Order(4)
        @DisplayName("Correct Beans map is returned")
        void getCorrectBeansMap() throws Exception {
            Map<String, Object> testBeansMap = new HashMap<>();
            testBeansMap.put("sameBean1", SameBean1.class.getConstructor().newInstance());
            testBeansMap.put("sameBean2", SameBean2.class.getConstructor().newInstance());
            String testBeansPackageName = "com.breskul.applicationContextTest.testBeansCorrectMap";
            var applicationContext = new ApplicationContextImpl(testBeansPackageName);
            Map<String, Object> applicationContextMap = applicationContext.getAllBeans(SameBeanInterface.class);
            for (Map.Entry<String, Object> entryTest : testBeansMap.entrySet()) {
                var beanName = entryTest.getKey();
                var beanValue = entryTest.getValue();
                assertTrue(applicationContextMap.containsKey(beanName));
                assertEquals(beanValue.getClass(), beanValue.getClass());
            }
        }
    }

    @Nested
    @Order(2)
    @DisplayName("2. ApplicationContext Autowiring Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class ApplicationContextAutowiringTest {
        @Test
        @Order(1)
        @DisplayName("Bean is autowired correclty")
        void autowireBeanCorrectly() throws Exception {
            var applicationContext = new ApplicationContextImpl(packageName);
            MessageServiceDemo messageServiceDemo = applicationContext.getBean(MessageServiceDemo.class);
            messageServiceDemo.setMessage("MY_MESSASGE");
            PrinterServiceDemo printerServiceDemo = applicationContext.getBean(PrinterServiceDemo.class);
            assertEquals(messageServiceDemo.getMessage(), printerServiceDemo.getMessage());
        }

        @Test
        @Order(2)
        @DisplayName("Autowiring throws NoUniqueBeanException")
        void autowiringGetNoUniqueBeanException(){
            String testBeansPackageName = "com.breskul.applicationContextTest.testBeanAutowireNoUniqueBeanException";
            assertThrows(NoUniqueBeanException.class, () -> new ApplicationContextImpl(testBeansPackageName));

        }

        @Test
        @Order(3)
        @DisplayName("Autowiring throws NoSuchBeanException")
        void autowiringGetNoSuchBeanException(){
            String testBeansPackageName = "com.breskul.applicationContextTest.testBeanAutowireNoSuchBeanException";
            assertThrows(NoSuchBeanException.class, () -> new ApplicationContextImpl(testBeansPackageName));
        }

    }
}
