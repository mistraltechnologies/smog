package com.mistraltech.smog.core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Class/interface annotation that identifies the type that the annotated class/interface is a matcher for.
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface Matches {
    /**
     * Identifies the type that is matched
     *
     * @return the matched type
     */
    Class<?> value();

    /**
     * Describes the matched type, e.g. "a Widget"
     *
     * @return matched type description
     */
    String description() default "";
}
