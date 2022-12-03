package me.omega.impl;

import me.omega.LogType;

/**
 * A log type that logs errors to the system console using:
 * <pre>
 *     System.err.println(message)
 * </pre>
 * @see me.omega.LogType
 */
public class SystemLogType implements LogType {

    @Override
    public void log(String message) {
        System.err.println(message);
    }

}
