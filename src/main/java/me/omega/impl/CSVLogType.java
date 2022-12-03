package me.omega.impl;

import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import me.omega.Baseline;
import me.omega.LogType;
import me.omega.LoggedClass;
import me.omega.LoggedObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A log type that logs to a csv file.
 * @see LogType
 */
public class CSVLogType implements LogType {

    private final CSVWriter writer;
    private int counter;

    public CSVLogType() throws IOException {
        writer = new CSVWriter(new FileWriter("C:\\Users\\xxtx1\\Documents\\TestLogsBaseline\\test.csv"));
        writer.writeNext(new String[]{"id", "timestamp", "field", "baseline", "value", "deviation"});
    }

    @Override
    public void log(String field, double value, LoggedObject loggedObject) {
        counter++;
        writer.writeNext(new String[]{
                String.valueOf(counter),
                String.valueOf(System.currentTimeMillis()),
                field,
                String.valueOf(loggedObject.baseline()),
                String.valueOf(value),
                String.valueOf(loggedObject.allowedDeviation())
        });
    }

    @Override
    public void setup() {

    }

}
