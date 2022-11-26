package me.omega;

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

    protected static final HashMap<LoggedClass, HashMap<Field, LoggedObject>> fieldMap = new HashMap<>();
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

    protected static void add(LoggedClass loggedClass, Field field, LoggedObject loggedObject) {
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
                LoggedObject loggedObject = fieldMap.get(loggedClass).get(field);
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
                    LoggedObject loggedObject = fieldMap.get(loggedClass).get(field);
                    try {
                        field.setAccessible(true);
                        double value = field.getDouble(loggedClass);
                        if (value > loggedObject.baseline() + loggedObject.allowedDeviation() || value < loggedObject.baseline() - loggedObject.allowedDeviation()) {
                            String message = "[" + loggedClass.getClass().getSimpleName() + "." + idMap.get(loggedClass) + "] Value " + field.getName() + " recorded " + value + " which is outside of the allowed deviation of " + loggedObject.allowedDeviation() + " from the baseline of " + loggedObject.baseline();
                            for (LogType logType : logTypes) {
                                logType.log(message);
                            }
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 0, 5, java.util.concurrent.TimeUnit.SECONDS);
    }

}
