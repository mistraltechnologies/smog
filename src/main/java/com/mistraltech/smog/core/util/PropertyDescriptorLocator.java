package com.mistraltech.smog.core.util;

import com.mistraltech.smog.core.PropertyNotFoundException;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

/**
 * A helper class for obtaining the {@link PropertyDescriptor}s of a bean by property name.
 */
public class PropertyDescriptorLocator {

    private final Class<?> beanClass;
    private final PropertyDescriptor[] propertyDescriptors;

    public PropertyDescriptorLocator(Class<?> beanClass) {
        this.beanClass = beanClass;
        propertyDescriptors = readPropertyDescriptors(beanClass);
    }

    public PropertyDescriptorLocator(Object obj) {
        this(obj.getClass());
    }

    private PropertyDescriptor[] readPropertyDescriptors(Class<?> beanClass) {
        try {
            final BeanInfo beanInfo = Introspector.getBeanInfo(beanClass, null);
            return beanInfo.getPropertyDescriptors();
        } catch (IntrospectionException e) {
            throw new RuntimeException("Could not read property descriptors for " + beanClass, e);
        }
    }

    /**
     * Get the property descriptor for the named property. Throws an exception if the property does not exist.
     *
     * @param propertyName property name
     * @return the PropertyDescriptor for the named property
     * @throws PropertyNotFoundException if the named property does not exist on the bean
     * @see #findPropertyDescriptor(String)
     */
    public PropertyDescriptor getPropertyDescriptor(String propertyName) {
        final PropertyDescriptor propertyDescriptor = findPropertyDescriptor(propertyName);

        if (propertyDescriptor == null) {
            throw new PropertyNotFoundException(beanClass, propertyName);
        }

        return propertyDescriptor;
    }

    /**
     * Attempt to get the property descriptor for the named property.
     *
     * @param propertyName property name
     * @return the PropertyDescriptor for the named property if found; otherwise null
     * @see #getPropertyDescriptor(String)
     */
    public PropertyDescriptor findPropertyDescriptor(String propertyName) {
        for (PropertyDescriptor property : propertyDescriptors) {
            if (property.getName().equals(propertyName)) {
                return property;
            }
        }

        return null;
    }
}
