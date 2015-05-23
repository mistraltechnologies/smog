package com.mistraltech.smog.core;

import com.mistraltech.smog.core.util.PropertyDescriptorUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class PropertyAccessor {
    private final String propertyName;

    public PropertyAccessor(String propertyName) {
        this.propertyName = propertyName;
    }

    public Object getPropertyValue(Object item) {
        PropertyDescriptor property = PropertyDescriptorUtils.getPropertyDescriptor(propertyName, item.getClass());

        final Method readMethod = property.getReadMethod();

        try {
            return readMethod.invoke(item);
        } catch (IllegalAccessException e) {
            throw new PropertyUnreadableException(item.getClass(), readMethod, propertyName, e);
        } catch (InvocationTargetException e) {
            throw new PropertyUnreadableException(item.getClass(), readMethod, propertyName, e);
        }
    }
}
