package com.mistraltech.smog.core;

import org.hamcrest.Description;

public class ReflectingPropertyMatcher<T> extends PropertyMatcher<T> {

    private PropertyAccessor propertyAccessor;

    public ReflectingPropertyMatcher(String propertyName, PropertyMatcherRegistry registry) {
        this(propertyName, registry, null);
    }

    public ReflectingPropertyMatcher(String propertyName, PropertyMatcherRegistry registry, PathProvider pathProvider) {
        super(propertyName, registry, pathProvider);
        propertyAccessor = new PropertyAccessor(propertyName);
    }

    @Override
    public boolean matches(Object item) {
        return !super.isSpecified() || super.matches(propertyAccessor.getPropertyValue(item));
    }

    @Override
    public void describeMismatch(Object item, Description mismatchDescription) {
        super.describeMismatch(propertyAccessor.getPropertyValue(item), mismatchDescription);
    }
}

