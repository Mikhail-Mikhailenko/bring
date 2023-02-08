package com.breskul.bring.packages.configurations;

import com.breskul.bring.annotations.Bean;
import com.breskul.bring.annotations.Configuration;

@Configuration
public class TestConfiguration {

    @Bean
    public ConfiguredComponent1 getComponent1(){
        return new ConfiguredComponent1();
    }

    @Bean("customBeanName")
    public ConfiguredComponent2 getComponent2(){
        return new ConfiguredComponent2();
    }
}
