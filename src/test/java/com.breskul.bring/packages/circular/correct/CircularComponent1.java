package com.breskul.bring.packages.circular.correct;

import com.breskul.bring.annotations.Autowired;
import com.breskul.bring.annotations.Component;

@Component
public class CircularComponent1 {
    @Autowired
    private CircularComponent2 component2;

    @Autowired
    private CircularComponent3 component3;

    public CircularComponent1(){

    }
}
