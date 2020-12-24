package com.mistraltech.smog.core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Identifies the property in the target object that the annotated method applies to.
 * <p>
 * Useful for overriding defaults assumed by matcher generators.
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface MatchesProperty {
    /**
     * The name of the property that is matched
     *
     * @return the matched property name
     */
    String value();
}
