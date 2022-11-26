package me.omega;

import java.lang.reflect.Field;

public abstract class LoggedClass {

    private boolean classIsRegistered = false;

    public void register(LoggedClass loggedClass) {
        if (classIsRegistered) {
            throw new IllegalStateException(loggedClass.getClass().getSimpleName() + " is already registered to be logged.");
        }
        classIsRegistered = true;

        for (Field field : loggedClass.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Log.class)) {
                Log log = field.getAnnotation(Log.class);
                LoggedObject loggedObject = new LoggedObject(log.baseline(), log.allowedDeviation());
                Baseline.add(loggedClass, field, loggedObject);
            }
        }
        Baseline.findId(loggedClass);
    }

}
