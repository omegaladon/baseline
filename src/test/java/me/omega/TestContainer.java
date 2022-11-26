package me.omega;

public class TestContainer extends LoggedClass {

    @Log(baseline = 5, allowedDeviation = 0.0)
    private final double testValue = 0.0;
    public TestContainer() {
        new TestLog2();
        register(this);
    }

}
