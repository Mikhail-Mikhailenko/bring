package com.breskul.bring.packages.autowired.inject_via_constructor_no_such_bean;

import com.breskul.bring.annotations.Component;

import java.lang.reflect.Field;

@Component
public class ComponentInstance {

    Field nonExistentComponent;

    public ComponentInstance() {
    }

    public ComponentInstance(Field nonExistentComponent) {
        this.nonExistentComponent = nonExistentComponent;

    }
}