package me.omega;

import me.omega.impl.SystemLogType;

public class Test {

    public static void main(String[] args) {

        Baseline.setDebug(true);
        Baseline.addLogType(new SystemLogType());

        TestLog t1 = new TestLog();
        Baseline.start();
    }

}