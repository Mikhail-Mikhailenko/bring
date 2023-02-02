package com.breskul.bring.packages.autowired.injectViaSetterNoUniqueBean;

import com.breskul.bring.annotations.Bean;
import com.breskul.bring.annotations.Component;

@Component
public class Componentinstnace3 {

    private SameInterface sameInterface;

    @Bean
    public void setSameInterface(SameInterface sameInterface) {
        this.sameInterface = sameInterface;
    }
}
