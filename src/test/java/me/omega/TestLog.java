package me.omega;

public class TestLog extends LoggedClass {

    @Log(baseline = 1.0, allowedDeviation = 10.0)
    public double a = 1;
    @Log(baseline = 5.0, allowedDeviation = 2.0)
    public double c = 25;

    public TestLog() {
        register(this);
    }

}
