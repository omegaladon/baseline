package me.omega;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Log {

    double baseline();
    double allowedDeviation();

}
