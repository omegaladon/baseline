package me.omega.object;

import java.lang.reflect.Field;

public record NestedLoggedObject(double baseline, double allowedDeviation, Field field) implements BaselineObject { }
