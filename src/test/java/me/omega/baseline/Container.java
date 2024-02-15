package me.omega.baseline;

import me.omega.baseline.loggers.SystemOutLogger;

public class Container implements LoggedClass {
    @Log
    private double value;

    @Log(name = "this_is_a_named_value")
    private double namedValue;

    public Container() {
        Baseline.addInstance(this);
        value = 5.0;
        namedValue = 10.0;
    }

    public static void main(String[] args) {
        Baseline.addLogger(new SystemOutLogger());
        Baseline.setDebug(true);
        new Container();
        Baseline.start("me.omega.baseline");
    }
}
