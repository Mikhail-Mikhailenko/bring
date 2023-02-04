package com.breskul.bring.packages.autowired.injectViaConstructorCorrect;

import com.breskul.bring.annotations.Component;

@Component
public class PrinterServiceDemoConstructorInjec {

    MessageServiceDemoConstructorInjec messageServiceDemoConstructorInjec;
    ListenerServiceConstructorInjec listenerServiceConstructorInjec;

    public void print() {
        System.out.println(messageServiceDemoConstructorInjec.getMessage());
        listenerServiceConstructorInjec.listen();
    }

}
