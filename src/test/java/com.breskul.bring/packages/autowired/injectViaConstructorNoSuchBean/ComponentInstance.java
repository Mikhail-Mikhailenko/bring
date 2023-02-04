package com.breskul.bring.packages.autowired.injectViaConstructorNoSuchBean;

import com.breskul.bring.annotations.Bean;
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