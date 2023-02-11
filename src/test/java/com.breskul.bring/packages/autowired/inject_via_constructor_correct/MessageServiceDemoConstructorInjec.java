package com.breskul.bring.packages.autowired.inject_via_constructor_correct;

import com.breskul.bring.annotations.Component;

@Component
public class MessageServiceDemoConstructorInjec {

    public MessageServiceDemoConstructorInjec(){}
    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
