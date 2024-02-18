package me.omega.baseline;

import me.omega.baseline.loggers.BaselineLogger;

import java.util.function.BiConsumer;
import java.util.function.Function;

public enum LogType {
    UNKNOWN((clazz) -> false, (logger, value) -> {}),
    STRING((clazz) -> clazz.equals(String.class), BaselineLogger::logString),
    NUMBER(LogType::isNumeric, BaselineLogger::logNumber),
    BOOLEAN(LogType::isBoolean, BaselineLogger::logBoolean),
    ENUM(Class::isEnum, BaselineLogger::logEnum),
    ;

    private final Function<Class<?>, Boolean> checker;
    private final BiConsumer<BaselineLogger, LoggedValue<?>> logger;

    LogType(Function<Class<?>, Boolean> checker, BiConsumer<BaselineLogger, LoggedValue<?>> logger) {
        this.checker = checker;
        this.logger = logger;
    }

    void log(BaselineLogger logger, LoggedValue<?> value) {
        this.logger.accept(logger, value);
    }

    public static <T> LogType fromObject(T object) {
        if (object == null) return UNKNOWN;
        for (LogType type : values()) {
            if (type.checker.apply(object.getClass())) return type;
        }
        return UNKNOWN;
    }

    private static boolean isNumeric(Class<?> clazz) {
        return Number.class.isAssignableFrom(clazz) || clazz.equals(byte.class) || clazz.equals(short.class) || clazz.equals(int.class) || clazz.equals(long.class) || clazz.equals(float.class) || clazz.equals(double.class);
    }

    private static boolean isBoolean(Class<?> clazz) {
        return clazz.equals(boolean.class) || clazz.equals(Boolean.class);
    }

}
