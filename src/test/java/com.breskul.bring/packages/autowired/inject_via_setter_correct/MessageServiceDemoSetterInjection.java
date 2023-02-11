package com.breskul.bring.packages.autowired.inject_via_setter_correct;

import com.breskul.bring.annotations.Component;

@Component
public class MessageServiceDemoSetterInjection {

    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
