package com.breskul.bring;

import com.breskul.bring.demoComponents.MessageServiceDemo;
import com.breskul.bring.demoComponents.PrinterServiceDemo;
import com.breskul.bring.exceptions.CommonException;
import com.breskul.bring.exceptions.NoSuchBeanException;
import com.breskul.bring.exceptions.NoUniqueBeanException;
import com.breskul.bring.testComponentsEdgeCases.testBeansCorrectMap.Component1;
import com.breskul.bring.testComponentsEdgeCases.testBeansCorrectMap.Component2;
import com.breskul.bring.testComponentsEdgeCases.testBeansCorrectMap.SameBeanInterface;
import org.junit.jupiter.api.*;

import java.lang.reflect.Field;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@DisplayName("ApplicationContext tests")
public class ApplicationContextTest {
    private static final String DEMO_PACAKGE_NAME = "com.breskul.bring.demoComponents";
    private static final String AUTOWIRE_NO_UNIQUE_BEAN_EXCEPTION_PACAKGE_NAME = "com.breskul.bring.testComponentsEdgeCases.testBeanAutowireNoUniqueBeanException";
    private static final String CORRECT_MAP_PACKAGE_NAME = "com.breskul.bring.testComponentsEdgeCases.testBeansCorrectMap";
    private static final String NO_SUCH_BEAN_PACAKGE_NAME = "com.breskul.bring.testComponentsEdgeCases.testComponentNoSuchBeanException";
    private static ApplicationContext context;


    @Nested
    @Order(1)
    @DisplayName("1. ApplicationContext Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class AppplicationContextInterfaceTest {
        @Test
        @Order(1)
        @DisplayName("Bean retrieved by type and name correctly")
        void getBeanByType() {
            ApplicationContext applicationContext = new AnnotationConfigApplicationContext(DEMO_PACAKGE_NAME);
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
        void getNoSuchBeanException() {
            ApplicationContext applicationContext = new AnnotationConfigApplicationContext(DEMO_PACAKGE_NAME);
            assertThrows(NoSuchBeanException.class, () -> applicationContext.getBean(Field.class));
            assertThrows(NoSuchBeanException.class, () -> applicationContext.getBean("PrinterServiceDemo", PrinterServiceDemo.class));
        }

        @Test
        @Order(3)
        @DisplayName("NoUniqueBeanException is thrown when there are 2 or more same class beans")
        void getNoUniqueBeanException() {
            var applicationContext = new AnnotationConfigApplicationContext(CORRECT_MAP_PACKAGE_NAME);
            assertThrows(NoUniqueBeanException.class, () -> applicationContext.getBean(SameBeanInterface.class));
        }

        @Test
        @Order(4)
        @DisplayName("Correct Beans map is returned")
        void getCorrectBeansMap() throws Exception {
            Map<String, Object> testBeansMap = new HashMap<>();
            testBeansMap.put("component1", Component1.class.getConstructor().newInstance());
            testBeansMap.put("component2", Component2.class.getConstructor().newInstance());
            var applicationContext = new AnnotationConfigApplicationContext(CORRECT_MAP_PACKAGE_NAME);
            Map<String, SameBeanInterface> applicationContextMap = applicationContext.getAllBeans(SameBeanInterface.class);
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
        void autowireBeanCorrectly() {
            var applicationContext = new AnnotationConfigApplicationContext(DEMO_PACAKGE_NAME);
            MessageServiceDemo messageServiceDemo = applicationContext.getBean(MessageServiceDemo.class);
            messageServiceDemo.setMessage("MY_MESSASGE");
            PrinterServiceDemo printerServiceDemo = applicationContext.getBean(PrinterServiceDemo.class);
            assertEquals(messageServiceDemo.getMessage(), printerServiceDemo.getMessage());
        }

        @Test
        @Order(2)
        @DisplayName("Autowiring throws NoUniqueBeanException")
        void autowiringGetNoUniqueBeanException() {
            assertThrows(NoUniqueBeanException.class,
                    () -> new AnnotationConfigApplicationContext(AUTOWIRE_NO_UNIQUE_BEAN_EXCEPTION_PACAKGE_NAME));

        }

        @Test
        @Order(3)
        @DisplayName("Autowiring throws NoSuchBeanException")
        void autowiringGetNoSuchBeanException() {
            assertThrows(NoSuchBeanException.class, () -> new AnnotationConfigApplicationContext(NO_SUCH_BEAN_PACAKGE_NAME));
        }

    }

    @Nested
    @Order(3)
    @DisplayName("3. Excpetions Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class ApplicationContextExceptionsTest {
        @Test
        @Order(1)
        @DisplayName("Common exception message is correct")
        void autowireBeanCorrectly() {
            final String location = getClass().getName();
            final String cause = "This is test cause";
            final String suggestedSolution = "This is the suggested solution";

            final String messageExpected = String.format(CommonException.PATTERN, location, cause, suggestedSolution);
            String messageActual = "";
            try {
                throw new CommonException(location, cause, suggestedSolution);
            } catch (RuntimeException ex) {
                messageActual = ex.getMessage();
            }
            assertEquals(messageExpected, messageActual);
        }
    }
}
