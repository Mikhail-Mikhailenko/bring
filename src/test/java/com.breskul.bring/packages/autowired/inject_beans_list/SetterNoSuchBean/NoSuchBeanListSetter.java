package com.breskul.bring.packages.autowired.inject_beans_list.SetterNoSuchBean;

import com.breskul.bring.annotations.Autowired;
import com.breskul.bring.annotations.Component;

import java.lang.reflect.Field;
import java.util.List;
@Component
public class NoSuchBeanListSetter {

    public NoSuchBeanListSetter(){}
    List<Field> nonExistentComponent;
    @Autowired
    public void setNonExistentComponent(List<Field> nonExistentComponent) {
        this.nonExistentComponent = nonExistentComponent;
    }
}
