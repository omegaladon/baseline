package me.omega;

public class TestLog2 extends LoggedClass {

    @Log(baseline = 1.0, allowedDeviation = 10.0)
    public double bestThingEver = 50;

    public TestLog2() {
        register(this);
    }

}
