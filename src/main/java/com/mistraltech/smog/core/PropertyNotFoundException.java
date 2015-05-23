package com.mistraltech.smog.core;

public class PropertyNotFoundException extends RuntimeException {
    public PropertyNotFoundException(Class clazz, String propertyName) {
        super(String.format("Could not find accessor method on class %s for property %s",
                clazz.getSimpleName(), propertyName));
    }
}
