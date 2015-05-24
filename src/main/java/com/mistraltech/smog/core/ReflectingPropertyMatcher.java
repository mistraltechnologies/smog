package com.mistraltech.smog.core;

import com.mistraltech.smog.core.util.PropertyDescriptorLocator;
import org.hamcrest.Description;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A type of {@link PropertyMatcher} that gets the property value to match against from the supplied bean
 * by reflection.
 *
 * @param <T> the type of the property being matched
 */
public class ReflectingPropertyMatcher<T> extends PropertyMatcher<T> {

    private PropertyAccessor propertyAccessor;

    /**
     * @see #ReflectingPropertyMatcher(String, PropertyMatcherRegistry, PathProvider)
     */
    public ReflectingPropertyMatcher(String propertyName, PropertyMatcherRegistry registry) {
        this(propertyName, registry, null);
    }

    /**
     * @see PropertyMatcher#PropertyMatcher(String, PropertyMatcherRegistry, PathProvider)
     */
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

    static class PropertyAccessor {
        private final String propertyName;

        public PropertyAccessor(String propertyName) {
            this.propertyName = propertyName;
        }

        public Object getPropertyValue(Object item) {
            PropertyDescriptor property = new PropertyDescriptorLocator(item).getPropertyDescriptor(propertyName);

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
}

