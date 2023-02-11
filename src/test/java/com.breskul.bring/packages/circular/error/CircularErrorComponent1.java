package com.breskul.bring.packages.circular.error;

import com.breskul.bring.annotations.Component;

@Component
public class CircularErrorComponent1 {

    private final CircularErrorComponent2 component2;

    public CircularErrorComponent1(CircularErrorComponent2 component2){
        this.component2 = component2;
    }
}
