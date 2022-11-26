package me.omega;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Baseline {

    protected static final HashMap<LoggedClass, HashMap<Field, LoggedObject>> fieldMap = new HashMap<>();
    protected static final HashMap<LoggedClass, Integer> idMap = new HashMap<>();
    protected static final ArrayList<LogType> logTypes = new ArrayList<>();

    private static boolean hasStarted = false;
    private static boolean debug;

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

    public static void addLogType(LogType... type) {
        Collections.addAll(logTypes, type);
    }

    public static void debug() {
        System.out.println("Debugging...");
        for (LoggedClass loggedClass : fieldMap.keySet()) {
            for (Field field : fieldMap.get(loggedClass).keySet()) {
                LoggedObject loggedObject = fieldMap.get(loggedClass).get(field);
                System.out.println("[" + loggedClass.getClass().getSimpleName() + "." + idMap.get(loggedClass) + "] Registered " + loggedClass.getClass().getSimpleName() + "." + field.getName() + " with baseline " + loggedObject.getBaseline() + " and allowed deviation " + loggedObject.getAllowedDeviation());
            }
        }
    }

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
                        if (value > loggedObject.getBaseline() + loggedObject.getAllowedDeviation() || value < loggedObject.getBaseline() - loggedObject.getAllowedDeviation()) {
                            String message = "[" + loggedClass.getClass().getSimpleName() + "." + idMap.get(loggedClass) + "] Value " + field.getName() + " recorded " + value + " which is outside of the allowed deviation of " + loggedObject.getAllowedDeviation() + " from the baseline of " + loggedObject.getBaseline();
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
