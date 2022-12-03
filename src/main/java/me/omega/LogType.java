package me.omega;

import me.omega.impl.SystemLogType;

/**
 * A type of log that can be used by {@link Baseline} to log data. For an example, see {@link SystemLogType}.
 * @see me.omega.impl.SystemLogType
 */
public interface LogType {

    /**
     * Code called when Baseline needs to log a message. For example:
     * <pre>
     *     System.out.println(message);
     * </pre>
     * @param message The message to log.
     */
    default void log(String message) {}

    /**
     * Used for log types that don't log a message. For an example, see {@link me.omega.impl.CSVLogType}.
     * @param field The name of the field being logged with its ID.
     * @param value The value of the field being logged.
     * @param loggedObject Logged object instance with data on deviation and baseline.
     * @see me.omega.impl.CSVLogType
     */
    default void log(String field, double value, LoggedObject loggedObject) {}

    /**
     * Code run when Baseline starts.
     */
    default void setup() {}

    /**
     * Code run when Baseline ends/finishes.
     */
    default void finish() {}

}
