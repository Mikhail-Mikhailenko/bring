package com.breskul.bring.packages.autowired.inject_via_setter_no_unique_bean;

import com.breskul.bring.annotations.Autowired;
import com.breskul.bring.annotations.Component;

@Component
public class Componentinstnace3 {

    private SameInterface sameInterface;
    public Componentinstnace3(){}

    @Autowired
    public void setSameInterface(SameInterface sameInterface) {
        this.sameInterface = sameInterface;
    }
}
