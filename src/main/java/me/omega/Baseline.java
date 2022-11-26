package me.omega;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Baseline {

    private static final HashMap<LoggedClass, HashMap<Field, LoggedObject>> fieldMap = new HashMap<>();

    private static boolean hasStarted = false;

    public static void add(LoggedClass loggedClass, Field field, LoggedObject loggedObject) {
        if (!fieldMap.containsKey(loggedClass)) {
            fieldMap.put(loggedClass, new HashMap<>());
        }
        fieldMap.get(loggedClass).put(field, loggedObject);
    }

    public static void debug() {
        System.out.println("Starting baseline logging...");
        for (LoggedClass loggedClass : fieldMap.keySet()) {
            for (Field field : fieldMap.get(loggedClass).keySet()) {
                LoggedObject loggedObject = fieldMap.get(loggedClass).get(field);
                System.out.println("[" + loggedClass + "]Registered " + loggedClass.getClass().getSimpleName() + "." + field.getName() + " with baseline " + loggedObject.getBaseline() + " and allowed deviation " + loggedObject.getAllowedDeviation());
            }
        }
    }

    public static void start() {
        if (hasStarted) {
            throw new IllegalStateException("Baseline has already started logging!");
        }
        hasStarted = true;

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            for (LoggedClass loggedClass : fieldMap.keySet()) {
                for (Field field : fieldMap.get(loggedClass).keySet()) {
                    LoggedObject loggedObject = fieldMap.get(loggedClass).get(field);
                    try {
                        field.setAccessible(true);
                        double value = field.getDouble(loggedClass);
                        if (value > loggedObject.getBaseline() + loggedObject.getAllowedDeviation() || value < loggedObject.getBaseline() - loggedObject.getAllowedDeviation()) {
                            System.out.println("[" + loggedClass + "] Baseline violation detected on " + loggedClass.getClass().getSimpleName() + "." + field.getName() + " with value " + value + " and baseline " + loggedObject.getBaseline() + " and allowed deviation " + loggedObject.getAllowedDeviation());
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 0, 5, java.util.concurrent.TimeUnit.SECONDS);
    }

    public static boolean isPresent(LoggedClass loggedClass) {
        return fieldMap.containsKey(loggedClass);
    }

}
