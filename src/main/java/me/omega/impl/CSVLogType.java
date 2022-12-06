package me.omega.impl;

import me.omega.LogType;
import me.omega.object.BaselineObject;
import me.omega.object.LoggedObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Date;

/**
 * A log type that logs to a csv file.
 * @see LogType
 */
public class CSVLogType implements LogType {

    private final FileWriter writer;
    private int counter;

    public CSVLogType() throws IOException {

        String fileName = "testlog_" + new Date().toString().replace(" ", "_").replace(":", "-") + ".csv";
        File file = new File("C:\\Users\\xxtx1\\Documents\\TestLogsBaseline\\" + fileName);
        file.createNewFile();

        writer = new FileWriter(file);
        writer.write("ID,Field,Value,Deviation,Baseline");
        writer.write("\n");
        writer.flush();
    }

    @Override
    public void log(String field, double value, BaselineObject loggedObject) {
        counter++;
        StringBuilder lineBuilder = new StringBuilder();
        lineBuilder.append(counter);
        lineBuilder.append(",");
        lineBuilder.append(formatMillis(ManagementFactory.getRuntimeMXBean().getUptime()));
        lineBuilder.append(",");
        lineBuilder.append(field);
        lineBuilder.append(",");
        lineBuilder.append(loggedObject.baseline());
        lineBuilder.append(",");
        lineBuilder.append(value);
        lineBuilder.append(",");
        lineBuilder.append(loggedObject.allowedDeviation());
        lineBuilder.append("\n");
        try {
            writer.write(lineBuilder.toString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String formatMillis(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        return days + "d " + hours % 24 + "h " + minutes % 60 + "m " + seconds % 60 + "s " + millis % 1000 + "ms";
    }

}
