package me.omega.baseline.value;

import me.omega.baseline.Log;
import me.omega.baseline.LoggedClass;

import java.lang.reflect.Field;

public class EnumLoggedValue extends LoggedValue<Enum<?>> {

    public EnumLoggedValue(LoggedClass loggedClass, Field field, Log log) {
        super(loggedClass, field, log);
    }

}
