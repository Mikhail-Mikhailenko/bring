package com.breskul.bring.packages.autowired.injectViaConstructorCorrect;

import com.breskul.bring.annotations.Component;

@Component
public class MessageServiceDemoConstructorInjec {
    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
