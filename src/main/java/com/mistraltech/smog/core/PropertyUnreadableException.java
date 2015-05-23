package com.mistraltech.smog.core;

import java.lang.reflect.Method;

public class PropertyUnreadableException extends RuntimeException {
    public PropertyUnreadableException(Class<?> clazz, Method method, String propertyName, Throwable e) {
        super(String.format("Could not read property %s of class %s using method %s",
                propertyName, clazz.getSimpleName(), method.getName() + "()"), e);
    }
}
