package com.breskul.bring.demoComponents;

import com.breskul.bring.AnnotationConfigApplicationContext;
import com.breskul.bring.ApplicationContext;

public class ApplicationContextDemo {
    private static final String message = "HELLO_BRESKUL_APPLICATION_CONTEXT";
    private static final String PACAKGE_NAME = "com.breskul.bring.demoComponents";

    public static void main(String[] args)  {


        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(PACAKGE_NAME);
        MessageServiceDemo messageServiceDemo = applicationContext.getBean(MessageServiceDemo.class);
        messageServiceDemo.setMessage(message);
        PrinterServiceDemo printerServiceDemo = applicationContext.getBean(PrinterServiceDemo.class);
        printerServiceDemo.printMessage();


    }
}
