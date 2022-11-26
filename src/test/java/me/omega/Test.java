package me.omega;

public class Test {

    public static void main(String[] args) throws IllegalAccessException {
        TestLog t1 = new TestLog();
//        TestLog t2 = new TestLog();
        new TestContainer();
        Baseline.debug();
        Baseline.start();
    }

}