package com.mistraltech.smog.core.annotation;

import javax.lang.model.SourceVersion;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
public @interface SmogVersion {
    int major();
    int minor();
}
