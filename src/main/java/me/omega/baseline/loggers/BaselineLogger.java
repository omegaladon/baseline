package me.omega.baseline.loggers;

import me.omega.baseline.value.LoggedValue;

public interface BaselineLogger {

    /**
     * Code called when Baseline needs to log a message. For example:
     * <pre>
     *     System.out.println(message);
     * </pre>
     */
    abstract void log(LoggedValue<?> value, String extra);

}
