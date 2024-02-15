package me.omega.baseline.loggers;

import me.omega.baseline.value.LoggedValue;

public class SystemOutLogger implements BaselineLogger {

    @Override
    public void log(LoggedValue<?> value, String extra) {
        System.out.println(value.field.getName() + " = " + value.get() + " " + extra);
    }

}
