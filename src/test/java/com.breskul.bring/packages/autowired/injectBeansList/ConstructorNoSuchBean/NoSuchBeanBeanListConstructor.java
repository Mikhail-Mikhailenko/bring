package com.breskul.bring.packages.autowired.injectBeansList.ConstructorNoSuchBean;


import com.breskul.bring.annotations.Component;

import java.lang.reflect.Field;
import java.util.List;
@Component
public class NoSuchBeanBeanListConstructor {
    public NoSuchBeanBeanListConstructor(){}
    List<Field> nonExistentComponent;

    public NoSuchBeanBeanListConstructor(List<Field> nonExistentComponent) {
        this.nonExistentComponent = nonExistentComponent;
    }
}
