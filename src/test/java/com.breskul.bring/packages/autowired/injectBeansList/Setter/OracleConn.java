package com.breskul.bring.packages.autowired.injectBeansList.Setter;

import com.breskul.bring.annotations.Component;


@Component
public class OracleConn implements DataBaseInterface {
    public OracleConn(){}

    @Override
    public void connect() {
        System.out.println("ORACLE_DB_CONNECTED");
    }
}