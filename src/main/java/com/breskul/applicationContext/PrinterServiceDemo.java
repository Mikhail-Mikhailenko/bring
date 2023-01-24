package com.breskul.applicationContext;

import com.breskul.applicationContext.annotations.Autowire;
import com.breskul.applicationContext.annotations.Bean;

@Bean
public class PrinterServiceDemo {

    public PrinterServiceDemo() {
    }

    @Autowire
    private MessageServiceDemo messageServiceDemo;

    public String getMessage(){
        return messageServiceDemo.getMessage();
    }
    public void printMessage() {
        System.out.println(getMessage());
    }
}
