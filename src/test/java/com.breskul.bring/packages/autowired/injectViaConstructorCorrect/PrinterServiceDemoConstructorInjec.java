package com.breskul.bring.packages.autowired.injectViaConstructorCorrect;

import com.breskul.bring.annotations.Component;

@Component
public class PrinterServiceDemoConstructorInjec {

    MessageServiceDemoConstructorInjec messageServiceDemoConstructorInjec;
    ListenerServiceConstructorInjec listenerServiceConstructorInjec;
    public PrinterServiceDemoConstructorInjec(){}
    public PrinterServiceDemoConstructorInjec(MessageServiceDemoConstructorInjec messageServiceDemoConstructorInjec, ListenerServiceConstructorInjec listenerServiceConstructorInjec) {
        this.messageServiceDemoConstructorInjec = messageServiceDemoConstructorInjec;
        this.listenerServiceConstructorInjec = listenerServiceConstructorInjec;
    }

    public void printMessage() {
        System.out.println(messageServiceDemoConstructorInjec.getMessage());
        listenerServiceConstructorInjec.listen();
    }

}
