package com.breskul.bring.packages.autowired.correct;


import com.breskul.bring.annotations.Component;

@Component("coolMessageServiceDemo")
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
