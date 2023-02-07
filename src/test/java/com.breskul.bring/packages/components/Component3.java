package com.breskul.bring.packages.components;


import com.breskul.bring.annotations.Component;

@Component
public class Component3 implements SameBeanInterface {

    private final Component4 component4;
    public Component3(Component4 component4) {
        this.component4 = component4;
    }
}
