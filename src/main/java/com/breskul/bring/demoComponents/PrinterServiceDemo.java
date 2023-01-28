package com.breskul.bring.demoComponents;


import com.breskul.bring.annotations.Autowired;
import com.breskul.bring.annotations.Component;

@Component
public class PrinterServiceDemo {

    public PrinterServiceDemo() {
    }

    @Autowired
    private MessageServiceDemo messageServiceDemo;

    public String getMessage(){
        return messageServiceDemo.getMessage();
    }
    public void printMessage() {
        System.out.println(getMessage());
    }
}
