import me.omega.Baseline;
import me.omega.impl.CSVLogType;
import me.omega.impl.SystemLogType;

import java.io.IOException;

public class Test {

    public static void main(String[] args) throws IOException {

        Baseline.setDebug(true);
        Baseline.addLogType(new SystemLogType());
        Baseline.addLogType(new CSVLogType());

        TestLog t1 = new TestLog();
        Baseline.start();
    }

}