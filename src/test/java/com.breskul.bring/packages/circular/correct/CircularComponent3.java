package com.breskul.bring.packages.circular.correct;

import com.breskul.bring.annotations.Autowired;
import com.breskul.bring.annotations.Component;

@Component
public class CircularComponent3 {
    @Autowired
    private CircularComponent1 component1;
}
