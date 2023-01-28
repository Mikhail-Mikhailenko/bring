package com.breskul.bring.testComponentsEdgeCases.testBeanAutowireNoUniqueBeanException;

import com.breskul.bring.annotations.Autowired;
import com.breskul.bring.annotations.Component;

@Component
public class Component1 implements SameBeanInterface {
    @Autowired
    SameBeanInterface nonUniqueComponent;

    public Component1() {
    }
}
