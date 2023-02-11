package com.breskul.bring.packages.autowired.inject_via_setter_correct;

import com.breskul.bring.annotations.Autowired;
import com.breskul.bring.annotations.Component;

@Component
public class PrinterServiceDemoSetterInjection {
    private MessageServiceDemoSetterInjection messageServiceDemoSetterInjection;
    private ListenerServiceSetterInjection listenerServiceSetterInjection;

    @Autowired
    public void setMessageService(MessageServiceDemoSetterInjection messageServiceDemoSetterInjection, ListenerServiceSetterInjection listenerServiceSetterInjection){
        this.messageServiceDemoSetterInjection = messageServiceDemoSetterInjection;
        this.listenerServiceSetterInjection = listenerServiceSetterInjection;
    }

    public void printMessage() {
        System.out.println(messageServiceDemoSetterInjection.getMessage());
        listenerServiceSetterInjection.listen();
    }
}
