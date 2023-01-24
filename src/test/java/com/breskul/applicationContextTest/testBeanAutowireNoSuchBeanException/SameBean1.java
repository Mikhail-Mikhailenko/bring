package com.breskul.applicationContextTest.testBeanAutowireNoSuchBeanException;

import com.breskul.applicationContext.annotations.Autowire;
import com.breskul.applicationContext.annotations.Bean;

import java.lang.reflect.Field;

@Bean
public class SameBean1 implements SameBeanInterface {
    @Autowire
    Field nonExistentBean;



    public SameBean1() {
    }
}
