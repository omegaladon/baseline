package me.omega.baseline.loggers;

import me.omega.baseline.value.LoggedValue;

public class SystemOutLogger implements BaselineLogger {

    @Override
    public void log(LoggedValue<?> value, String extra) {
        if (value.parent == null) {
            System.out.println("Root: " + value.getName() + " = " + value.getValue() + " " + extra);
        } else {
            System.out.println(value.getParentString() + ": " + value.getName() + " = " + value.getValue() + " " + extra);
        }
    }

}
