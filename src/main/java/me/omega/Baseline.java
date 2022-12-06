package me.omega;

import me.omega.object.BaselineObject;
import me.omega.object.LoggedObject;
import me.omega.object.NestedLoggedObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * The main class for Baseline.
 * @author omega
 */
public class Baseline {

    protected static final HashMap<LoggedClass, HashMap<Field, BaselineObject>> fieldMap = new HashMap<>();
    protected static final HashMap<LoggedClass, Integer> idMap = new HashMap<>();
    protected static final ArrayList<LogType> logTypes = new ArrayList<>();

    private static boolean hasStarted = false;
    private static boolean debug;

    /**
     * Sets the debug mode for Baseline.
     */
    public static void setDebug(boolean debug) {
        Baseline.debug = debug;
    }

    protected static void add(LoggedClass loggedClass, Field field, BaselineObject loggedObject) {
        if (!fieldMap.containsKey(loggedClass)) {
            fieldMap.put(loggedClass, new HashMap<>());
        }
        fieldMap.get(loggedClass).put(field, loggedObject);
    }

    protected static void findId(LoggedClass loggedClass) {
        int count = 0;
        for (LoggedClass loggedClazz : fieldMap.keySet()) {
            if (loggedClazz.getClass().equals(loggedClass.getClass())) {
                count++;
            }
        }
        idMap.put(loggedClass, count);
    }

    /**
     * Adds a LogType to Baseline.
     * You can create your own LogType by implementing the LogType interface. Read more about it <a href="https://github.com/omegaladon/baseline">here</a>.
     */
    public static void addLogType(LogType... type) {
        Collections.addAll(logTypes, type);
    }

    /**
     * Prints out various debugging information.
     */
    public static void debug() {
        System.out.println("Debugging...");
        for (LoggedClass loggedClass : fieldMap.keySet()) {
            for (Field field : fieldMap.get(loggedClass).keySet()) {
                BaselineObject loggedObject = fieldMap.get(loggedClass).get(field);
                System.out.println("[" + loggedClass.getClass().getSimpleName() + "." + idMap.get(loggedClass) + "] Registered " + loggedClass.getClass().getSimpleName() + "." + field.getName() + " with baseline " + loggedObject.baseline() + " and allowed deviation " + loggedObject.allowedDeviation());
            }
        }
    }

    /**
     * Starts Baseline. This can only be run once.
     */
    public static void start() {
        if (logTypes.isEmpty()) {
            throw new IllegalStateException("No LogTypes have been added. Add a LogType with Baseline.addLogType(LogType type)");
        }
        if (hasStarted) {
            throw new IllegalStateException("Baseline has already started logging.");
        }
        hasStarted = true;

        if (debug) debug();

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            for (LoggedClass loggedClass : fieldMap.keySet()) {
                for (Field field : fieldMap.get(loggedClass).keySet()) {
                    BaselineObject loggedObject = fieldMap.get(loggedClass).get(field);
                    if (loggedObject instanceof LoggedObject) {
                        try {
                            field.setAccessible(true);
                            double value = field.getDouble(loggedClass);
                            if (value > loggedObject.baseline() + loggedObject.allowedDeviation() || value < loggedObject.baseline() - loggedObject.allowedDeviation()) {
                                String message = "[(" + idMap.get(loggedClass) + ") " +  loggedClass.getClass().getSimpleName() + "] Value " + field.getName() + " recorded " + value + " which is outside of the allowed deviation of " + loggedObject.allowedDeviation() + " from the baseline of " + loggedObject.baseline();
                                for (LogType logType : logTypes) {
                                    logType.log(message);
                                    logType.log("(" + idMap.get(loggedClass) + ") " + loggedClass.getClass().getSimpleName(), value, loggedObject);
                                }
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    } else if (loggedObject instanceof NestedLoggedObject) {
                        try {
                            field.setAccessible(true);
                            Field object = loggedObject.field();
                            for (Field nestedField : object.getType().getDeclaredFields()) {
                                if (!nestedField.getName().equals(field.getName())) continue;
                                nestedField.setAccessible(true);
                                Object value = nestedField.get(nestedField.getDeclaringClass());
                                System.out.println(value);
//                                double value = nestedField.getDouble(object);
//                                if (value > loggedObject.baseline() + loggedObject.allowedDeviation() || value < loggedObject.baseline() - loggedObject.allowedDeviation()) {
//                                    String message = "[(" + idMap.get(loggedClass) + ") " + loggedClass.getClass().getSimpleName() + "." + loggedObject.field().getName() + "." + nestedField.getName() + "] Recorded " + value + " which is outside of the allowed deviation of " + loggedObject.allowedDeviation() + " from the baseline of " + loggedObject.baseline();
//                                    for (LogType logType : logTypes) {
//                                        logType.log(message);
//                                        logType.log("(" + idMap.get(loggedClass) + ") " + loggedClass.getClass().getSimpleName() + "." + field.getName() + "." + nestedField.getName(), value, loggedObject);
//                                    }
//                                }
                            }
//                        } catch (IllegalAccessException e) {
//                            throw new RuntimeException(e);
//                        }
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        } finally {

                        }
                    }
                }
            }
        }, 0, 5, java.util.concurrent.TimeUnit.SECONDS);
    }

    protected HashMap<LoggedClass, HashMap<Field, BaselineObject>> getFieldMap() {
        return fieldMap;
    }

    protected HashMap<LoggedClass, Integer> getIdMap() {
        return idMap;
    }

}
