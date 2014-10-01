package com.mistraltech.smog.core;

import org.hamcrest.StringDescription;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.naming.OperationNotSupportedException;
import java.lang.reflect.InvocationTargetException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReflectingPropertyMatcherTest {
    private static final Person bob = new Person();

    private PropertyMatcherRegistry mockRegistry;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
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

        thrown.expect(PropertyNotFoundException.class);
        thrown.expectMessage("Could not find accessor method on class Person for property surname");

        matcher.matches(bob);
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

        thrown.expect(PropertyUnreadableException.class);
        thrown.expectMessage("Could not read property brokenName of class Person using method getBrokenName()");
        thrown.expectCause(isA(InvocationTargetException.class));

        matcher.matches(bob);
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
