package me.omega.object;

import java.lang.reflect.Field;

public interface BaselineObject {
    double baseline();
    double allowedDeviation();
    default Field field() {
        return null;
    }
}
