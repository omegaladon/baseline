package me.omega.baseline;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Log {
    String name() default "";

    double baseline() default 0.0;

    double allowedDeviation() default 0.0;
}
