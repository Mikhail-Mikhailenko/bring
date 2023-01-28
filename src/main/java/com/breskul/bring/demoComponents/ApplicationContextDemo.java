package com.breskul.bring.demoComponents;

import com.breskul.bring.AnnotationConfigApplicationContext;
import com.breskul.bring.ApplicationContext;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public class ApplicationContextDemo {
    private static final String message = "HELLO_BRESKUL_APPLICATION_CONTEXT";
    private static final String PACAKGE_NAME = "com.breskul.bring.demoComponents";

    public static void main(String[] args) throws Exception {


        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(PACAKGE_NAME);
        MessageServiceDemo messageServiceDemo = applicationContext.getBean(MessageServiceDemo.class);
        messageServiceDemo.setMessage(message);
        PrinterServiceDemo printerServiceDemo = applicationContext.getBean(PrinterServiceDemo.class);
        printerServiceDemo.printMessage();


    }
}
