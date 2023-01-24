package com.breskul.applicationContextTest.testBeanAutowireNoUniqueBeanException;

import com.breskul.applicationContext.annotations.Bean;

@Bean
public class SameBean2 implements SameBeanInterface {
    public SameBean2() {
    }
}
