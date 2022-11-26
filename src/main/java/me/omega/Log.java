package me.omega;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to mark a field to be logged.
 * <p>The field must be an {@link Integer}, {@link Double}, or {@link Float}, and must be public.</p>
 * <p>For example:</p>
 * <pre>
 *     {@code @Log(baseline = 1.0, allowedDeviation = 10.0)}
 *     public int example = 51;
 * </pre>
 * <pre>
 *     {@code @Log(baseline = 5.3256, allowedDeviation = 0.05)}
 *     public double bestDouble;
 * </pre>
 * Your class must extend {@link LoggedClass} and execute {@link LoggedClass#register(LoggedClass)}.
 * @see LoggedClass
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Log {

    /**
     * The baseline value for the field.
     */
    double baseline();
    /**
     * The allowed deviation from the baseline value.
     */
    double allowedDeviation();

}
