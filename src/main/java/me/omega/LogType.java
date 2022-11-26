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
     */
    void log(String message);

}
