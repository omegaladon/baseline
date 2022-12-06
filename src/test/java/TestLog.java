import me.omega.Log;

public class TestLog {

    @Log(baseline = 1.0, allowedDeviation = 10.0)
    public double a = 1;
    @Log(baseline = 5.0, allowedDeviation = 2.0)
    public int c = 25;
    @Log(baseline = 20.0, allowedDeviation = 2.0)
    public double amazing = 3;

//    public TestLog() {
//        register(this);
//    }

}
