package me.omega.baseline.value;

import me.omega.baseline.Log;
import me.omega.baseline.LoggedClass;

import java.lang.reflect.Field;

public class NumberLoggedValue extends LoggedValue<Number[]> {

    public NumberLoggedValue(LoggedClass loggedClass, Field field, Log log) {
        super(loggedClass, field, log);
    }

}
