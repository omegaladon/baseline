package me.omega.baseline;

import me.omega.baseline.loggers.BaselineLogger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class Baseline {

    public static final Logger logger = Logger.getLogger(Baseline.class.getName());

    private static Boolean started = false;
    private static Boolean debug = false;
    private static Duration interval = Duration.ofMillis(250);

    private static final HashSet<LoggedValue<?>> values = new HashSet<>();
    private static final HashMap<LoggedClass, LoggedValue<?>> classValueMap = new HashMap<>();
    private static final HashMap<Class<?>, ArrayList<Field>> cachedFields = new HashMap<>();
    private static final HashMap<Class<?>, ArrayList<Method>> cachedMethods = new HashMap<>();
    private static final HashSet<BaselineLogger> loggers = new HashSet<>();

    private static ScheduledExecutorService executor;

    public static List<LoggedValue<?>> addInstance(LoggedClass loggedClass) throws IllegalArgumentException {
        var list = new ArrayList<LoggedValue<?>>();
        var clazz = loggedClass.getClass();
        if (!cachedFields.containsKey(clazz)) {
            var fields = new ArrayList<Field>();
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Log.class)) {
                    field.setAccessible(true);
                    fields.add(field);
                }
            }
            cachedFields.put(clazz, fields);
        }

        if (!cachedMethods.containsKey(clazz)) {
            var methods = new ArrayList<Method>();
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Log.class)) {
                    method.setAccessible(true);
                    methods.add(method);
                }
            }
            cachedMethods.put(clazz, methods);
        }

        var fields = cachedFields.get(clazz);
        fields.forEach(field -> {
            if (!field.isAnnotationPresent(Log.class)) return;
            var log = field.getAnnotation(Log.class);
            var type = field.getType();

            try {
                if (LoggedClass.class.isAssignableFrom(type)) {
                    list.addAll(addInstance(log, field.getName(), loggedClass, (LoggedClass) field.get(loggedClass),
                            () -> null));
                } else {
                    list.add(addValue(log, field.getName(), loggedClass, () -> {
                        try {
                            return field.get(loggedClass);
                        } catch (IllegalAccessException e) {
                            return null;
                        }
                    }));
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Unsupported log type: " + type);
            }
        });

        var methods = cachedMethods.get(clazz);
        methods.forEach(method -> {
            if (!method.isAnnotationPresent(Log.class)) return;
            var log = method.getAnnotation(Log.class);
            var type = method.getReturnType();

            try {
                if (LoggedClass.class.isAssignableFrom(type)) {
                    list.addAll(addInstance(log, method.getName(), loggedClass, (LoggedClass) method.invoke(loggedClass),
                            () -> null));
                } else {
                    list.add(addValue(log, method.getName(), loggedClass, () -> {
                        try {
                            return method.invoke(loggedClass);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            return null;
                        }
                    }));
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Unsupported log type: " + type);
            }
        });

        if (debug) logger.info("Added " + list.size() + " values for " + clazz.getSimpleName() + "!");
        return list;
    }

    private static LoggedValue<?> addValue(Log log, String name, LoggedClass loggedClass,
                                          Supplier<?> getter) throws IllegalStateException {
        var value = new LoggedValue<>(loggedClass, name, log, getter);
        values.add(value);
        return value;
    }

    private static List<LoggedValue<?>> addInstance(Log log, String name, LoggedClass loggedClass,
                                                    LoggedClass instance, Supplier<?> getter) throws IllegalStateException {
        LoggedClass.class.isAssignableFrom(instance.getClass());
        var value = addInstance(instance);
        value.forEach((loggedValue) -> {
            if (loggedValue.parent != null) return;
            classValueMap.putIfAbsent(loggedClass, new LoggedValue<>(loggedClass, name, log, getter));
            loggedValue.setParent(
                    classValueMap.get(loggedClass)
            );
        });
        var list = new ArrayList<>(value);
        list.add(
                classValueMap.computeIfAbsent(loggedClass, (key) -> new LoggedValue<>(loggedClass, name, log,
                        getter))
        );
        return list;
    }

    public static void start() {
        if (loggers.isEmpty()) throw new IllegalStateException("No loggers have been added!");
        if (started) throw new IllegalStateException("Baseline is already started!");

        started = true;
        logger.info("Baseline has started!");
        logger.info("Debug mode is " + (debug ? "enabled" : "disabled") + ".");

        init();
    }

    private static void init() {
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            try {
                values.forEach((loggedValue) -> {
                    loggers.forEach((logger) -> logger.log(loggedValue, ""));
                });
            } catch (Exception e) {
                logger.severe("An error occurred while logging values: " + e.getMessage());
                e.printStackTrace();
            }
        }, 0, interval.toMillis(), TimeUnit.MILLISECONDS);
    }

    public static void addLogger(BaselineLogger logger) {
        if (started) {
            throw new IllegalStateException("Cannot add a logger after Baseline has started!");
        }
        loggers.add(logger);
    }

    public static void setDebug(Boolean debug) {
        if (started) {
            throw new IllegalStateException("Cannot set debug mode after Baseline has started!");
        }
        Baseline.debug = debug;
    }

    public static void setInterval(Duration interval) {
        if (started) {
            throw new IllegalStateException("Cannot set interval after Baseline has started!");
        }
        Baseline.interval = interval;
    }

    private static boolean isNumericClass(Class<?> clazz) {
        return Number.class.isAssignableFrom(clazz) || clazz.equals(byte.class) || clazz.equals(short.class) || clazz.equals(int.class) || clazz.equals(long.class) || clazz.equals(float.class) || clazz.equals(double.class);
    }

}
