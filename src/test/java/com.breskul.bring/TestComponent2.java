package com.breskul.bring;

import com.breskul.bring.annotations.Autowired;

public class TestComponent2 {
    @Autowired
    TestComponent1 testComponent1;

    public TestComponent1 getTestComponent1() {
        return testComponent1;
    }

    public void setTestComponent1(TestComponent1 testComponent1) {
        this.testComponent1 = testComponent1;
    }
}
