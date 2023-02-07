package com.breskul.bring.packages.circular.correct;

import com.breskul.bring.annotations.Component;

@Component
public class CircularComponent2 {
    private CircularComponent1 component1;

    public CircularComponent2(CircularComponent1 component1){
        this.component1 = component1;
    }
}
