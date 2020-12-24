package com.mistraltech.smog.core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Identifies the version of the Smog library in use. Reserved for use on {@link com.mistraltech.smog.core.Smog}.
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface SmogVersion {
    int major();

    int minor();
}
