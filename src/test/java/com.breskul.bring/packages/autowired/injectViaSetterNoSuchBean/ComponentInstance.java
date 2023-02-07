package com.breskul.bring.packages.autowired.injectViaSetterNoSuchBean;

import com.breskul.bring.annotations.Autowired;
import com.breskul.bring.annotations.Component;

import java.lang.reflect.Field;

@Component
public class ComponentInstance {

    Field nonExistentComponent;

    @Autowired
    public void setNonExistentComponent(Field nonExistentComponent) {
        this.nonExistentComponent = nonExistentComponent;
    }

    public ComponentInstance() {
    }
}