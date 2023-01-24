package com.breskul.applicationContextTest.testBeanAutowireNoUniqueBeanException;

import com.breskul.applicationContext.annotations.Autowire;
import com.breskul.applicationContext.annotations.Bean;

@Bean
public class SameBean1 implements SameBeanInterface {
    @Autowire
    SameBeanInterface nonUniqueBean;

    public SameBean1() {
    }
}
