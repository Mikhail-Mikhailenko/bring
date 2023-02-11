package com.breskul.bring.packages.autowired.inject_via_constructor_correct;

import com.breskul.bring.annotations.Component;

@Component
public class ListenerServiceConstructorInjec {
    public ListenerServiceConstructorInjec(){}
    public void listen() {
        System.out.println("LISTENING_SERVICE_IS_LISTENING");
    }

}
