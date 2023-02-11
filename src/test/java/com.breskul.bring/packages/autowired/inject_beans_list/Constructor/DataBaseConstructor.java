package com.breskul.bring.packages.autowired.inject_beans_list.Constructor;

import com.breskul.bring.annotations.Component;

import java.util.List;

@Component
public class DataBaseConstructor {

    private List<DataBaseInterface> databases;
    public DataBaseConstructor(){

    }
    public DataBaseConstructor(List<DataBaseInterface> databases) {
        this.databases = databases;
    }


    public void connect() {
        databases.forEach(DataBaseInterface::connect);
    }

    public List<DataBaseInterface> getDatabases() {
        return databases;
    }
}
