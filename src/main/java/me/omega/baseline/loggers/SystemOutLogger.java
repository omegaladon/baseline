package me.omega.baseline.loggers;

import me.omega.baseline.LoggedValue;

public class SystemOutLogger implements BaselineLogger {

    @Override
    public void log(LoggedValue<?> value, String extra) {
        System.out.println(value.getParentString() + ": " + value.getName() + " = " + value.getValue() + " " + extra);
    }

}
