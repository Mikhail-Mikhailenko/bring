package com.breskul.bring.packages.circular.error;

import com.breskul.bring.annotations.Component;

@Component
public class CircularErrorComponent2 {

    private final CircularErrorComponent1 component1;
    public CircularErrorComponent2(CircularErrorComponent1 component1){
        this.component1 = component1;
    }
}
