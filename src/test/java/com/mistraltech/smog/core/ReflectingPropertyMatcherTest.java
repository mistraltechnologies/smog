package com.mistraltech.smog.core;

import org.hamcrest.StringDescription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.OperationNotSupportedException;
import java.lang.reflect.InvocationTargetException;

import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReflectingPropertyMatcherTest {
    private static final Person bob = new Person();

    private PropertyMatcherRegistry mockRegistry;

    @BeforeEach
    public void setUp() throws Exception {
        mockRegistry = mock(PropertyMatcherRegistry.class);
    }

    @Test
    public void registersWithRegistryOnConstruction() {
        PropertyMatcher<String> propertyMatcher = new PropertyMatcher<String>("myProperty", mockRegistry);

        verify(mockRegistry).registerPropertyMatcher(propertyMatcher);
    }

    @Test
    public void canMatchAgainstPropertyParent() {
        PropertyMatcher<String> nameMatcher = new ReflectingPropertyMatcher<String>("name", null);
        nameMatcher.setMatcher(equalTo("Bob"));

        boolean result = nameMatcher.matches(bob);

        assertTrue(result);
    }

    @Test
    public void cannotMatchWhenPropertyNotFound() {
        PropertyMatcher<String> matcher = new ReflectingPropertyMatcher<String>("surname", null);
        matcher.setMatcher(anything());

        Exception e = assertThrows(PropertyNotFoundException.class, () -> matcher.matches(bob));
        assertEquals("Could not find accessor method on class Person for property surname", e.getMessage());

    }

    @Test
    public void doesNotReadPropertyUnnecessarilyWhenMatcherUnspecified() {
        PropertyMatcher<String> matcher = new ReflectingPropertyMatcher<String>("surname", null);

        matcher.matches(bob);

        // Absence of exception means test passed. If there was an attempt to read the property value
        // a PropertyNotFoundException would have been thrown.
        // See cannotMatchWhenPropertyNotFound().
    }

    @Test
    public void cannotMatchWhenAccessorFails() {
        PropertyMatcher<String> matcher = new ReflectingPropertyMatcher<String>("brokenName", null);
        matcher.setMatcher(anything());

        Exception e = assertThrows(PropertyUnreadableException.class, () -> matcher.matches(bob));
        assertEquals("Could not read property brokenName of class Person using method getBrokenName()", e.getMessage());
        assertTrue(e.getCause() instanceof InvocationTargetException);
    }

    @Test
    public void canDescribeMismatchAgainstPropertyParent() {
        PropertyMatcher<String> nameMatcher = new ReflectingPropertyMatcher<String>("name", null, new PathProvider() {
            @Override
            public String getPath() {
                return "path";
            }
        });

        nameMatcher.setMatcher(equalTo("Bill"));
        StringDescription mismatchDescription = new StringDescription();

        nameMatcher.describeMismatch(bob, mismatchDescription);

        assertEquals("path.name was \"Bob\" (expected \"Bill\")", mismatchDescription.toString());
    }

    @SuppressWarnings("UnusedDeclaration")
    private static class Person {
        public String getName() {
            return "Bob";
        }

        public String getBrokenName() throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }
}
