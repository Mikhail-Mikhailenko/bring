package com.breskul.bring.packages.autowired.inject_via_constructor_no_unique_bean;

import com.breskul.bring.annotations.Component;

@Component
public class Componentinstnace3 {

    private SameInterface sameInterface;
    public Componentinstnace3(){}
    public Componentinstnace3(SameInterface sameInterface) {
        this.sameInterface = sameInterface;
    }
}
