package com.breskul.bring.packages.autowired.injectBeansList.Constructor;

import com.breskul.bring.annotations.Component;

@Component
public class PostgresConn implements DataBaseInterface {
    public PostgresConn(){}

    @Override
    public void connect() {
        System.out.println("POSTGRESS_DB_CONNECTED");
    }
}
