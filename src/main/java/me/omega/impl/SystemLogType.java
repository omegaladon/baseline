package me.omega.impl;

import me.omega.LogType;

public class SystemLogType implements LogType {

    @Override
    public void log(String message) {
        System.err.println(message);
    }

}
