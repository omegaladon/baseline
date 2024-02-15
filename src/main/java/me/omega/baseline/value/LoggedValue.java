package me.omega.baseline.value;

import me.omega.baseline.Baseline;
import me.omega.baseline.Log;
import me.omega.baseline.LoggedClass;

import java.lang.reflect.Field;
import java.util.function.Supplier;

public abstract class LoggedValue<T> {

    public final LoggedClass loggedClass;
    public final Field field;
    public final Log log;
    public final Supplier<T> getter;

    public LoggedValue(LoggedClass loggedClass, Field field, Log log) {
        this.loggedClass = loggedClass;
        this.field = field;
        this.log = log;

        this.getter = () -> {
            try {
                return (T) field.get(loggedClass);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    public T get() {
        return getter.get();
    }

}
