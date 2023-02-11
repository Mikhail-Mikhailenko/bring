package com.breskul.bring.packages.autowired.inject_via_setter_correct;

import com.breskul.bring.annotations.Component;

@Component
public class ListenerServiceSetterInjection {

    public void listen(){
        System.out.println("LISTENING_SERVICE_IS_LISTENING");
    }

}
