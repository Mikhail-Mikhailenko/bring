package com.breskul.bring.packages.autowired.inject_beans_list.Setter;

import com.breskul.bring.annotations.Autowired;
import com.breskul.bring.annotations.Component;

import java.util.List;

@Component
public class DataBaseSetter {

    private List<DataBaseInterface> databases;
    public DataBaseSetter(){

    }
    @Autowired
    public void setDatabases(List<DataBaseInterface> databases) {
        this.databases = databases;
    }

    public void connect() {
        databases.forEach(DataBaseInterface::connect);
    }

    public List<DataBaseInterface> getDatabases() {
        return databases;
    }
}
