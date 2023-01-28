package com.breskul.bring.demoComponents;


import com.breskul.bring.annotations.Autowired;
import com.breskul.bring.annotations.Component;

@Component
public class PrinterServiceDemo {

    public PrinterServiceDemo() {
    }

    public PrinterServiceDemo(MessageServiceDemo messageServiceDemo){
        this.cool = messageServiceDemo;
    }

    private MessageServiceDemo cool;

    public String getMessage(){
        return cool.getMessage();
    }
    public void printMessage() {
        System.out.println(getMessage());
    }
}
