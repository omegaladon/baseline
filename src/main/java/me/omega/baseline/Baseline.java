package me.omega.baseline;

import me.omega.baseline.loggers.BaselineLogger;
import me.omega.baseline.value.*;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class Baseline {

    public static final Logger logger = Logger.getLogger(Baseline.class.getName());
    private static Reflections reflections;

    private static Boolean started = false;
    private static Boolean debug = false;
    private static Duration interval = Duration.ofMillis(250);

    private static final ArrayList<LoggedValue<?>> values = new ArrayList<>();
    private static final HashMap<Class<?>, ArrayList<Field>> cachedFields = new HashMap<>();
    private static final ArrayList<BaselineLogger> loggers = new ArrayList<>();

    private static ScheduledExecutorService executor;

    public static void addInstance(LoggedClass loggedClass) {
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

        var fields = cachedFields.get(clazz);
        AtomicInteger counter = new AtomicInteger();
        fields.forEach(field -> {
            if (!field.isAnnotationPresent(Log.class)) return;
            var log = field.getAnnotation(Log.class);
            var type = field.getType();
            if (isNumericClass(type)) {
                values.add(new NumberLoggedValue(loggedClass, field, log));
            } else if (type.isArray() && isNumericClass(type.getComponentType())) {
                values.add(new NumberArrayLoggedValue(loggedClass, field, log));
            } else if (Enum.class.isAssignableFrom(type)) {
                values.add(new EnumLoggedValue(loggedClass, field, log));
            } else if (Boolean.class.isAssignableFrom(type) || type.equals(boolean.class)) {
                values.add(new BooleanLoggedValue(loggedClass, field, log));
            } else {
                throw new IllegalArgumentException("Unsupported log type: " + type);
            }
            counter.getAndIncrement();
        });
        if (debug) logger.info("Added " + counter.get() + " values for " + clazz.getSimpleName() + "!");
    }

    public static void start(String... packages) {
        if (packages.length == 0) throw new IllegalArgumentException("At least one package must be provided!");
        if (loggers.isEmpty()) throw new IllegalStateException("No loggers have been added!");
        if (started) throw new IllegalStateException("Baseline is already started!");

        started = true;
        logger.info("Baseline has started!");
        logger.info("Debug mode is " + (debug ? "enabled" : "disabled") + ".");

        reflections = new Reflections(new ConfigurationBuilder()
                .forPackages(packages)
        );
        init();
    }

    private static void init() {
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            logger.info("Logging values...");
            try {
                values.forEach((loggedValue) -> {
                    loggers.forEach((logger) -> logger.log(loggedValue, ""));
                    logger.info(loggedValue.field.getName() + ": " + loggedValue.getter.get());
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
