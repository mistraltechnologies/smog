package com.mistraltech.smog.core;

import org.hamcrest.Description;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectingPropertyMatcher<T> extends PropertyMatcher<T> {

    public ReflectingPropertyMatcher(String propertyName, PropertyMatcherRegistry registry) {
        super(propertyName, registry);
    }

    public ReflectingPropertyMatcher(String propertyName, PropertyMatcherRegistry registry, PathProvider pathProvider) {
        super(propertyName, registry, pathProvider);
    }

    @Override
    public boolean matches(Object item) {
        return !super.isSpecified() || super.matches(getPropertyValue(item));
    }

    @Override
    public void describeMismatch(Object item, Description mismatchDescription) {
        super.describeMismatch(getPropertyValue(item), mismatchDescription);
    }

    private Object getPropertyValue(Object item) {
        PropertyDescriptor property = getPropertyDescriptor(getPropertyName(), item);

        final Method readMethod = property.getReadMethod();

        try {
            return readMethod.invoke(item);
        } catch (IllegalAccessException e) {
            throw new PropertyUnreadableException(item.getClass(), readMethod, getPropertyName(), e);
        } catch (InvocationTargetException e) {
            throw new PropertyUnreadableException(item.getClass(), readMethod, getPropertyName(), e);
        }
    }

    private PropertyDescriptor getPropertyDescriptor(String propertyName, Object item) {
        try {
            final BeanInfo beanInfo = Introspector.getBeanInfo(item.getClass(), null);
            final PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                if (property.getName().equals(propertyName)) {
                    return property;
                }
            }
        } catch (IntrospectionException e) {
            throw new IllegalArgumentException("Could not get property descriptors for " + item.getClass(), e);
        }

        throw new PropertyNotFoundException(item.getClass(), getPropertyName());
    }
}

