package com.breskul.bring.packages.autowired.inject_beans_list.Constructor;

import com.breskul.bring.annotations.Component;

@Component
public class PostgresConn implements DataBaseInterface {
    public PostgresConn(){}

    @Override
    public void connect() {
        System.out.println("POSTGRESS_DB_CONNECTED");
    }
}
