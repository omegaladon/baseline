package me.omega.baseline.value;

import me.omega.baseline.Log;
import me.omega.baseline.LoggedClass;

import java.lang.reflect.Field;

public class BooleanLoggedValue extends LoggedValue<Boolean> {

    public BooleanLoggedValue(LoggedClass loggedClass, Field field, Log log) {
        super(loggedClass, field, log);
    }

}
