package com.breskul.applicationContext;

import com.breskul.applicationContext.annotations.Bean;

@Bean("coolMessageServiceDemo")
public class MessageServiceDemo {

    private String message;
    public MessageServiceDemo(){}

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
