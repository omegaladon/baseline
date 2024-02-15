package me.omega.baseline.value;

import me.omega.baseline.Log;
import me.omega.baseline.LoggedClass;

import java.lang.reflect.Field;

public class NumberArrayLoggedValue extends LoggedValue<Number[]> {

    public NumberArrayLoggedValue(LoggedClass loggedClass, Field field, Log log) {
        super(loggedClass, field, log);
    }

}
