package com.breskul.bring.packages.autowired.injectViaConstructorCorrect;

import com.breskul.bring.annotations.Component;

@Component
public class ListenerServiceConstructorInjec {
    public void listen() {
        System.out.println("LISTENING_SERVICE_IS_LISTENING");
    }

}
