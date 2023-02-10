package com.breskul.bring;

import com.breskul.bring.exceptions.BeanInitializingException;
import com.breskul.bring.packages.autowired.correct.MessageServiceDemo;
import com.breskul.bring.packages.autowired.correct.PrinterServiceDemo;
import com.breskul.bring.exceptions.NoSuchBeanException;
import com.breskul.bring.exceptions.NoUniqueBeanException;
import com.breskul.bring.packages.autowired.injectBeansList.Constructor.DataBaseConstructor;
import com.breskul.bring.packages.autowired.injectBeansList.Setter.DataBaseSetter;
import com.breskul.bring.packages.autowired.injectViaConstructorCorrect.ListenerServiceConstructorInjec;
import com.breskul.bring.packages.autowired.injectViaConstructorCorrect.MessageServiceDemoConstructorInjec;
import com.breskul.bring.packages.autowired.injectViaConstructorCorrect.PrinterServiceDemoConstructorInjec;
import com.breskul.bring.packages.autowired.injectViaSetterCorrect.ListenerServiceSetterInjection;
import com.breskul.bring.packages.autowired.injectViaSetterCorrect.MessageServiceDemoSetterInjection;
import com.breskul.bring.packages.autowired.injectViaSetterCorrect.PrinterServiceDemoSetterInjection;
import com.breskul.bring.packages.circular.correct.CircularComponent1;
import com.breskul.bring.packages.circular.correct.CircularComponent2;
import com.breskul.bring.packages.circular.correct.CircularComponent3;
import com.breskul.bring.packages.components.Component1;
import com.breskul.bring.packages.components.Component2;
import com.breskul.bring.packages.components.SameBeanInterface;
import com.breskul.bring.packages.configurations.ConfiguredBeanInterface;
import com.breskul.bring.packages.configurations.ConfiguredComponent1;
import com.breskul.bring.packages.configurations.ConfiguredComponent2;
import com.breskul.bring.packages.configurations.TestConfiguration;
import com.breskul.bring.packages.configurations.TestCustomNameConfiguration;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;

import java.lang.reflect.Field;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@DisplayName("ApplicationContext tests")
public class ApplicationContextTest {

    @Nested
    @Order(1)
    @DisplayName("1. ApplicationContext Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class ApplicationContextInterfaceTest {
        private static final String COMPONENTS_PACKAGE_NAME = "com.breskul.bring.packages.components";

        @Test
        @Order(1)
        @DisplayName("Bean retrieved by type and name correctly")
        void getBeanByType() {
            ApplicationContext applicationContext = new AnnotationConfigApplicationContext(COMPONENTS_PACKAGE_NAME);
            SameBeanInterface messageServiceDemo = applicationContext.getBean(Component1.class);
            assertEquals(messageServiceDemo.getClass(), Component1.class);

            SameBeanInterface messageServiceDemoByName = applicationContext.getBean("component1", Component1.class);
            assertEquals(messageServiceDemoByName.getClass(), Component1.class);

            SameBeanInterface printerServiceDemo = applicationContext.getBean("customNamedComponent", Component2.class);
            assertEquals(printerServiceDemo.getClass(), Component2.class);
        }

        @Test
        @Order(2)
        @DisplayName("NoSuchBeanException is thrown when there is no bean")
        void getNoSuchBeanException() {
            ApplicationContext applicationContext = new AnnotationConfigApplicationContext(COMPONENTS_PACKAGE_NAME);
            assertThrows(NoSuchBeanException.class, () -> applicationContext.getBean(Field.class));
            assertThrows(NoSuchBeanException.class, () -> applicationContext.getBean("component2", Component2.class));
        }

        @Test
        @Order(3)
        @DisplayName("NoUniqueBeanException is thrown when there are 2 or more same class beans")
        void getNoUniqueBeanException() {
            var applicationContext = new AnnotationConfigApplicationContext(COMPONENTS_PACKAGE_NAME);
            assertThrows(NoUniqueBeanException.class, () -> applicationContext.getBean(SameBeanInterface.class));
        }

        @Test
        @Order(4)
        @DisplayName("Correct Beans map is returned")
        void getCorrectBeansMap() throws Exception {
            Map<String, Object> testBeansMap = new HashMap<>();
            testBeansMap.put("component1", Component1.class.getConstructor().newInstance());
            testBeansMap.put("customNamedComponent", Component2.class.getConstructor().newInstance());
            var applicationContext = new AnnotationConfigApplicationContext(COMPONENTS_PACKAGE_NAME);
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
        private static final String AUTOWIRE_CORRECT_PACAKGE_NAME = "com.breskul.bring.packages.autowired.correct";
        private static final String AUTOWIRE_NO_UNIQUE_BEAN_EXCEPTION_PACAKGE_NAME = "com.breskul.bring.packages.autowired.nouniquebean";
        private static final String AUTOWIRE_NO_SUCH_BEAN_PACAKGE_NAME = "com.breskul.bring.packages.autowired.nosuchbean";

        @Test
        @Order(1)
        @DisplayName("Bean is autowired correctly")
        void autowireBeanCorrectly() {
            var applicationContext = new AnnotationConfigApplicationContext(AUTOWIRE_CORRECT_PACAKGE_NAME);
            MessageServiceDemo messageServiceDemo = applicationContext.getBean(MessageServiceDemo.class);
            messageServiceDemo.setMessage("MY_MESSAGE");
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
            assertThrows(NoSuchBeanException.class, () -> new AnnotationConfigApplicationContext(AUTOWIRE_NO_SUCH_BEAN_PACAKGE_NAME));
        }

    }

    @Nested
    @Order(3)
    @DisplayName("3. ApplicationContext Configuration Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class ApplicationContextConfigurationTest {
        private static final String CONFIGURATION_PACKAGE_NAME = "com.breskul.bring.packages.configurations";
        @Test
        @Order(1)
        @DisplayName("Beans are loaded from the MyConfiguration")
        void loadConfiguration() {
            ApplicationContext applicationContext = new AnnotationConfigApplicationContext(CONFIGURATION_PACKAGE_NAME);
            ConfiguredBeanInterface component1ByType = applicationContext.getBean(ConfiguredComponent1.class);
            assertEquals(component1ByType.getClass(), ConfiguredComponent1.class);

            ConfiguredBeanInterface component1ByName = applicationContext.getBean("configuredComponent1", ConfiguredComponent1.class);
            assertEquals(component1ByName.getClass(), ConfiguredComponent1.class);

            ConfiguredBeanInterface printerServiceDemo = applicationContext.getBean("customBeanName", ConfiguredComponent2.class);
            assertEquals(printerServiceDemo.getClass(), ConfiguredComponent2.class);
        }

        @Test
        @Order(2)
        @DisplayName("NoSuchBeanException is thrown when there is no bean")
        void getNoSuchBeanException() {
            ApplicationContext applicationContext = new AnnotationConfigApplicationContext(CONFIGURATION_PACKAGE_NAME);
            assertThrows(NoSuchBeanException.class, () -> applicationContext.getBean(Field.class));
            assertThrows(NoSuchBeanException.class, () -> applicationContext.getBean("component2", ConfiguredComponent2.class));
        }

        @Test
        @Order(3)
        @DisplayName("NoUniqueBeanException is thrown when there are 2 or more same class beans")
        void getNoUniqueBeanException() {
            var applicationContext = new AnnotationConfigApplicationContext(CONFIGURATION_PACKAGE_NAME);
            assertThrows(NoUniqueBeanException.class, () -> applicationContext.getBean(ConfiguredBeanInterface.class));
        }

        @Test
        @Order(4)
        @DisplayName("Correct Beans map is returned")
        void getCorrectBeansMap() throws Exception {
            Map<String, Object> testBeansMap = new HashMap<>();
            testBeansMap.put("configuredComponent1", ConfiguredComponent1.class.getConstructor().newInstance());
            testBeansMap.put("customBeanName", ConfiguredComponent2.class.getConstructor().newInstance());
            var applicationContext = new AnnotationConfigApplicationContext(CONFIGURATION_PACKAGE_NAME);
            Map<String, ConfiguredBeanInterface> applicationContextMap = applicationContext.getAllBeans(ConfiguredBeanInterface.class);
            for (Map.Entry<String, Object> entryTest : testBeansMap.entrySet()) {
                var beanName = entryTest.getKey();
                var beanValue = entryTest.getValue();
                assertTrue(applicationContextMap.containsKey(beanName));
                assertEquals(beanValue.getClass(), beanValue.getClass());
            }
        }

        @Test
        @Order(5)
        @DisplayName("Configuration bean exists")
        void getConfigurationBean() {
            var applicationContext = new AnnotationConfigApplicationContext(CONFIGURATION_PACKAGE_NAME);

            TestConfiguration testConfiguration = applicationContext.getBean(TestConfiguration.class);
            assertNotNull(testConfiguration);

            TestCustomNameConfiguration customNameConfiguration = applicationContext
                    .getBean("customNameConfiguration", TestCustomNameConfiguration.class);
            assertNotNull(customNameConfiguration);
        }

        @Test
        @Order(6)
        @DisplayName("Configuration beans return the same instance of object")
        void getProxyConfigurationBean() {
            var applicationContext = new AnnotationConfigApplicationContext(CONFIGURATION_PACKAGE_NAME);

            TestConfiguration testConfiguration = applicationContext.getBean(TestConfiguration.class);
            assertNotNull(testConfiguration);

            assertSame(testConfiguration.getComponent1(), testConfiguration.getComponent1());
            assertSame(testConfiguration.getComponent2(), testConfiguration.getComponent2());
        }
    }

    @Nested
    @Order(4)
    @DisplayName("4. Inject Bean Via Setter Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class InjectBeanViaSetterTest {
        private static final String INJECT_VIA_SETTER_PACKAGE_NAME = "com.breskul.bring.packages.autowired.injectViaSetterCorrect";
        private static final String INJECT_VIA_SETTER_NO_SUCH_BEAN = "com.breskul.bring.packages.autowired.injectViaSetterNoSuchBean";
        private static final String INJECT_VIA_SETTER_NO_UNIQUE_BEAN = "com.breskul.bring.packages.autowired.injectViaSetterNoUniqueBean";
        @Test
        @Order(1)
        @DisplayName("Beans are correctly injected via setter")
        void loadConfiguration() {
            ApplicationContext applicationContext = new AnnotationConfigApplicationContext(INJECT_VIA_SETTER_PACKAGE_NAME);
            ListenerServiceSetterInjection listenerServiceSetterInjection = applicationContext.getBean(ListenerServiceSetterInjection.class);
            assertEquals(listenerServiceSetterInjection.getClass(), ListenerServiceSetterInjection.class);

            MessageServiceDemoSetterInjection messageServiceDemoSetterInjection = applicationContext.getBean(MessageServiceDemoSetterInjection.class);
            assertEquals(messageServiceDemoSetterInjection.getClass(), MessageServiceDemoSetterInjection.class);
            messageServiceDemoSetterInjection.setMessage("MESSAGE_FROM_MESSAGE_SERVICE!");
            PrinterServiceDemoSetterInjection printerServiceDemoSetterInjection = applicationContext.getBean(PrinterServiceDemoSetterInjection.class);
            assertEquals(printerServiceDemoSetterInjection.getClass(), PrinterServiceDemoSetterInjection.class);

            printerServiceDemoSetterInjection.printMessage();
        }

        @Test
        @Order(2)
        @DisplayName("NoSuchBeanException is thrown when there is no bean")
        void getNoSuchBeanException() {
            assertThrows(NoSuchBeanException.class, () -> new AnnotationConfigApplicationContext(INJECT_VIA_SETTER_NO_SUCH_BEAN));
        }

        @Test
        @Order(3)
        @DisplayName("NoUniqueBeanException is thrown when there are 2 or more same class beans")
        void getNoUniqueBeanException() {
            assertThrows(NoUniqueBeanException.class, () -> new AnnotationConfigApplicationContext(INJECT_VIA_SETTER_NO_UNIQUE_BEAN));
        }

    }


    @Nested
    @Order(5)
    @DisplayName("5. Inject Beans via Constructor")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class InjectBeansViaConstrucor {
        private static final String INJECT_VIA_CONSTRUCTOR_BEAN_CORRECT = "com.breskul.bring.packages.autowired.injectViaConstructorCorrect";
        private static final String INJECT_VIA_CONSTRUCTOR_NO_SUCH_BEAN = "com.breskul.bring.packages.autowired.injectViaConstructorNoSuchBean";
        private static final String INJECT_VIA_CONSTRUCTOR_NO_UNIQUE_BEAN = "com.breskul.bring.packages.autowired.injectViaConstructorNoUniqueBean";
        @Test
        @Order(1)
        @DisplayName("Beans are correctly injected via constructor")
        void loadConfiguration() {
            ApplicationContext applicationContext = new AnnotationConfigApplicationContext(INJECT_VIA_CONSTRUCTOR_BEAN_CORRECT);
            ListenerServiceConstructorInjec listenerServiceConstructorInjec = applicationContext.getBean(ListenerServiceConstructorInjec.class);
            assertEquals(listenerServiceConstructorInjec.getClass(), ListenerServiceConstructorInjec.class);

            MessageServiceDemoConstructorInjec messageServiceDemoConstructorInjec = applicationContext.getBean(MessageServiceDemoConstructorInjec.class);
            assertEquals(messageServiceDemoConstructorInjec.getClass(), MessageServiceDemoConstructorInjec.class);
            messageServiceDemoConstructorInjec.setMessage("MESSAGE_FROM_MESSAGE_SERVICE!");

            PrinterServiceDemoConstructorInjec printerServiceDemoConstructorInjec = applicationContext.getBean(PrinterServiceDemoConstructorInjec.class);
            assertEquals(printerServiceDemoConstructorInjec.getClass(), PrinterServiceDemoConstructorInjec.class);

            printerServiceDemoConstructorInjec.printMessage();
        }

        @Test
        @Order(2)
        @DisplayName("NoSuchBeanException is thrown when there is no bean")
        void getNoSuchBeanException() {
            assertThrows(NoSuchBeanException.class, () -> new AnnotationConfigApplicationContext(INJECT_VIA_CONSTRUCTOR_NO_SUCH_BEAN));
        }

        @Test
        @Order(3)
        @DisplayName("NoUniqueBeanException is thrown when there are 2 or more same class beans")
        void getNoUniqueBeanException() {
            assertThrows(NoUniqueBeanException.class, () -> new AnnotationConfigApplicationContext(INJECT_VIA_CONSTRUCTOR_NO_UNIQUE_BEAN));
        }

    }


    @Nested
    @Order(6)
    @DisplayName("6. Inject List of Beans")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class InjectListBeans{
        private static final String INJECT_BEANS_VIA_CONSTRUCTOR = "com.breskul.bring.packages.autowired.injectBeansList.Constructor";

        private static final String INJECT_BEANS_VIA_SETTER = "com.breskul.bring.packages.autowired.injectBeansList.Setter";
        private static final String NO_SUCH_BEAN_EXCEPTION_CONSTRUCTOR = "com.breskul.bring.packages.autowired.injectBeansList.ConstructorNoSuchBean";
        private static final String NO_SUCH_BEAN_EXCEPTION_SETTER = "com.breskul.bring.packages.autowired.injectBeansList.SetterNoSuchBean";

        @Test
        @Order(1)
        @DisplayName("List of beans are correctly injected via Constructor")
        void injectListBeansViaConstructor() {
            ApplicationContext applicationContext = new AnnotationConfigApplicationContext(INJECT_BEANS_VIA_CONSTRUCTOR);
            DataBaseConstructor dataBaseConstructor = applicationContext.getBean(DataBaseConstructor.class);
            assertEquals(dataBaseConstructor.getDatabases().size(), 3);
            dataBaseConstructor.connect();

        }

        @Test
        @Order(2)
        @DisplayName("List of beans are correctly injected via Setter")
        void injectListBeansViaSetter() {
            ApplicationContext applicationContext = new AnnotationConfigApplicationContext(INJECT_BEANS_VIA_SETTER);
            DataBaseSetter dataBaseConstructor = applicationContext.getBean(DataBaseSetter.class);
            assertEquals(dataBaseConstructor.getDatabases().size(), 3);
            dataBaseConstructor.connect();

        }

        @Test
        @Order(3)
        @DisplayName("No Such Bean Excpetion is thrown for List injection")
        void  testNoSuchBeanException() {
            assertThrows(NoSuchBeanException.class, () -> new AnnotationConfigApplicationContext(NO_SUCH_BEAN_EXCEPTION_CONSTRUCTOR));
            assertThrows(NoSuchBeanException.class, () -> new AnnotationConfigApplicationContext(NO_SUCH_BEAN_EXCEPTION_SETTER));

        }

    }

    @Nested
    @Order(7)
    @DisplayName("7. Circular Dependencies")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class CircularDependencies {
        private static final String CIRCULAR_DEPENDENCIES_CORRECT_PACKAGE = "com.breskul.bring.packages.circular.correct";
        private static final String CIRCULAR_DEPENDENCIES_ERROR_PACKAGE = "com.breskul.bring.packages.circular.error";
        @Test
        @Order(1)
        @DisplayName("Circular Dependencies are resolved")
        void loadConfiguration() {
            ApplicationContext applicationContext = new AnnotationConfigApplicationContext(CIRCULAR_DEPENDENCIES_CORRECT_PACKAGE);
            CircularComponent1 component1 = applicationContext.getBean(CircularComponent1.class);
            assertEquals(component1.getClass(), CircularComponent1.class);

            CircularComponent2 component2 = applicationContext.getBean(CircularComponent2.class);
            assertEquals(component2.getClass(), CircularComponent2.class);

            CircularComponent3 component3 = applicationContext.getBean(CircularComponent3.class);
            assertEquals(component3.getClass(), CircularComponent3.class);
        }

        @Test
        @Order(2)
        @DisplayName("Circular Dependency error")
        void getNoSuchBeanException() {
            assertThrows(BeanInitializingException.class, () -> new AnnotationConfigApplicationContext(CIRCULAR_DEPENDENCIES_ERROR_PACKAGE));
        }
    }

}
