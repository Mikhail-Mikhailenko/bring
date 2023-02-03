package com.breskul.bring.packages.autowired.injectViaSetterCorrect;

import com.breskul.bring.annotations.Component;

@Component
public class ListenerServiceSetterInjection {

    public void listen(){
        System.out.println("LISTENING_SERVICE_IS_LISTENING");
    }

}
