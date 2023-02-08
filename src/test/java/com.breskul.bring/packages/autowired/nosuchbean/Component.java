package com.breskul.bring.packages.autowired.nosuchbean;


import com.breskul.bring.annotations.Autowired;

import java.lang.reflect.Field;

@com.breskul.bring.annotations.Component
public class Component {

    @Autowired
    Field nonExistentComponent;

    public Component() {
    }
}
