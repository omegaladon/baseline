package me.omega.baseline;

import me.omega.baseline.loggers.SystemOutLogger;

import java.time.Duration;

public class Container implements LoggedClass {
    @Log
    private double value;

    @Log(name = "test/NamedValue")
    private double namedValue;

    @Log(name = "holder")
    private Holder holder = new Holder();

    public Container() {
        Baseline.addInstance(this);
        value = 5.0;
        namedValue = 10.0;
    }

    public static void main(String[] args) {
        Baseline.addLogger(new SystemOutLogger());
        Baseline.setDebug(true);
        Baseline.setInterval(Duration.ofSeconds(1));
        new Container();
        new Container();
        Baseline.start("me.omega.baseline");
    }
}
