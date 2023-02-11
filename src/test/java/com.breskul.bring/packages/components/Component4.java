package com.breskul.bring.packages.components;


import com.breskul.bring.annotations.Component;

@Component
public class Component4 implements SameBeanInterface {
    private final Component2 component2;
    public Component4(Component2 component2) {
        this.component2 = component2;
    }
}
