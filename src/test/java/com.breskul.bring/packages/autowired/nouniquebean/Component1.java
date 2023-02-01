package com.breskul.bring.packages.autowired.nouniquebean;

import com.breskul.bring.annotations.Autowired;
import com.breskul.bring.annotations.Component;

@Component
public class Component1 implements SameBeanInterface {
    @Autowired
    SameBeanInterface nonUniqueComponent;

    public Component1() {
    }
}
