package me.omega.baseline;

import java.util.function.Function;

public enum LogType {
    UNKNOWN((clazz) -> false),
    LOGGED_CLASS(LoggedClass.class::isAssignableFrom),
    STRING((clazz) -> clazz.equals(String.class)),
    NUMBER(LogType::isNumeric),
    BOOLEAN(LogType::isBoolean),
    ENUM(Class::isEnum),
    ;

    private final Function<Class<?>, Boolean> checker;

    LogType(Function<Class<?>, Boolean> checker) {
        this.checker = checker;
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
