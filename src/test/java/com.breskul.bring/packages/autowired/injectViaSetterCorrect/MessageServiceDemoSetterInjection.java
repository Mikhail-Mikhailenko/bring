package com.breskul.bring.packages.autowired.injectViaSetterCorrect;

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
