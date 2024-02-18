package me.omega.baseline.loggers;

import me.omega.baseline.LoggedValue;

public interface BaselineLogger {

    /**
     * Code called when Baseline needs to log a message. For example:
     * <pre>
     *     System.out.println(...);
     * </pre>
     */
    default void log(LoggedValue<?> value) {};

    default void logString(LoggedValue<?> value) {}
    default void logNumber(LoggedValue<?> value) {}
    default void logBoolean(LoggedValue<?> value) {}
    default void logEnum(LoggedValue<?> value) {}

}
