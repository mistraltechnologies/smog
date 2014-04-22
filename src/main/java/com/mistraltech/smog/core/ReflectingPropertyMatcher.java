package com.mistraltech.smog.core;

import org.hamcrest.Description;
import org.hamcrest.beans.PropertyUtil;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectingPropertyMatcher<T> extends PropertyMatcher<T> {

    public ReflectingPropertyMatcher(String propertyName, PathProvider pathProvider) {
        super(propertyName, pathProvider);
    }

    @Override
    public boolean matches(Object item) {
        return super.matches(getPropertyValue(item));
    }

    @Override
    public void describeMismatch(Object item, Description mismatchDescription) {
        super.describeMismatch(getPropertyValue(item), mismatchDescription);
    }

    private Object getPropertyValue(Object item) {
        PropertyDescriptor property = PropertyUtil.getPropertyDescriptor(getPropertyName(), item);

        if (property == null) {
            throw new PropertyNotFoundException(item.getClass(), getPropertyName());
        }

        final Method readMethod = property.getReadMethod();

        try {
            return readMethod.invoke(item);
        } catch (IllegalAccessException e) {
            throw new PropertyUnreadableException(item.getClass(), readMethod, getPropertyName(), e);
        } catch (InvocationTargetException e) {
            throw new PropertyUnreadableException(item.getClass(), readMethod, getPropertyName(), e);
        }
    }
}

