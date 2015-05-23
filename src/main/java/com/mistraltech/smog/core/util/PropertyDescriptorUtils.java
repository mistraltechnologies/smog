package com.mistraltech.smog.core.util;

import com.mistraltech.smog.core.PropertyNotFoundException;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

public final class PropertyDescriptorUtils {
    private PropertyDescriptorUtils() {
    }

    public static PropertyDescriptor getPropertyDescriptor(String propertyName, Class clazz) {
        try {
            final BeanInfo beanInfo = Introspector.getBeanInfo(clazz, null);
            final PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                if (property.getName().equals(propertyName)) {
                    return property;
                }
            }
        } catch (IntrospectionException e) {
            throw new IllegalArgumentException("Could not get property descriptors for " + clazz, e);
        }

        throw new PropertyNotFoundException(clazz, propertyName);
    }
}
