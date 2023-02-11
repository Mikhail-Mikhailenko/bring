package com.breskul.bring.packages.autowired.inject_beans_list.Constructor;

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
