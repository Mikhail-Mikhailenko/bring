package com.breskul.bring.packages.autowired.injectBeansList.Constructor;

import com.breskul.bring.annotations.Component;

@Component
public class AWSRedshiftConn implements DataBaseInterface{
    public AWSRedshiftConn(){
    }
    @Override
    public void connect() {
        System.out.println("AWS_DB_CONNECTED");
    }
}
