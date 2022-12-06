import me.omega.Baseline;
import me.omega.Log;
import me.omega.LoggedClass;
import me.omega.impl.CSVLogType;
import me.omega.impl.SystemLogType;

import java.io.IOException;

public class Test extends LoggedClass {

    @Log(isNested = true)
    private final TestLog testLog = new TestLog();
    public Test() {
        register(this);
    }

    public static void main(String[] args) throws IOException {

        Baseline.setDebug(true);
        Baseline.addLogType(new SystemLogType());
        Baseline.addLogType(new CSVLogType());

        new Test();
        new TestLog2();

        Baseline.start();

    }

}