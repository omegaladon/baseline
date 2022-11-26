package me.omega;

import java.lang.reflect.Field;

/**
 * A class used by Baseline to log data. In order for proper execution, it is suggested to run {@link LoggedClass#register(LoggedClass)} in your class's constructor to register all {@link Log} fields to Baseline.
 * <p></p>
 * For example:
 * <pre>
 *     public class Example extends LoggedClass {
 *
 *         {@code @Log(baseline = 9.71, allowedDeviation = 9.73)}
 *         public int team = 9.72;
 *
 *         public Example() {
 *             register(this);
 *         }
 *     }
 */
public abstract class LoggedClass {

    private boolean classIsRegistered = false;

    /**
     * Registers the fields of this class to Baseline. You must execute this method in your class's constructor.
     * @param loggedClass The class to register. Most commonly, loggedClass will be {@code this}.
     */
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
