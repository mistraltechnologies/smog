package com.mistraltech.smog.core;


import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class PropertyMatcherTest {
    private PathProvider mockPathProvider;
    private PropertyMatcherRegistry mockRegistry;

    @BeforeEach
    public void setUp() throws Exception {
        mockPathProvider = new PathProviderStub("myPath");
        mockRegistry = mock(PropertyMatcherRegistry.class);
    }

    @Test
    public void canGetPropertyName() {
        PropertyMatcher<String> propertyMatcher = new PropertyMatcher<String>("foo", mockRegistry);

        assertEquals("foo", propertyMatcher.getPropertyName());
    }

    @Test
    public void cannotConstructWithEmptyPropertyNameAndAnyRegistry() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> new PropertyMatcher<String>(null, mockRegistry));
        assertEquals("No property name", e.getMessage());
    }

    @Test
    public void cannotConstructWithEmptyPropertyNameAndAnyRegistryAndPathProvider() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> new PropertyMatcher<String>(null, mockRegistry, mockPathProvider));
        assertEquals("No property name", e.getMessage());
    }

    @Test
    public void registersWithRegistryOnConstruction() {
        PropertyMatcher<String> propertyMatcher = new PropertyMatcher<String>("myProperty", mockRegistry);

        verify(mockRegistry).registerPropertyMatcher(propertyMatcher);
    }

    @Test
    public void registersWithRegistryOnConstructionWithAnyPathProvider() {
        PropertyMatcher<String> propertyMatcher =
                new PropertyMatcher<String>("myProperty", mockRegistry, mockPathProvider);

        verify(mockRegistry).registerPropertyMatcher(propertyMatcher);
    }

    @Test
    public void doesNotAttemptToRegisterWithNullRegistryOnConstruction() {
        new PropertyMatcher<String>("myProperty", null, mockPathProvider);
    }

    @Test
    public void canGetPathWithConstructorAssignedPathProvider() {
        PropertyMatcher<String> propertyMatcher =
                new PropertyMatcher<String>("myProperty", null, mockPathProvider);

        assertEquals("myPath.myProperty", propertyMatcher.getPath());
    }

    @Test
    public void canGetPathWithPostConstructionPathProvider() {
        PropertyMatcher<String> propertyMatcher = new PropertyMatcher<String>("myProperty", null);
        propertyMatcher.setPathProvider(mockPathProvider);

        assertEquals("myPath.myProperty", propertyMatcher.getPath());
    }

    @Test
    public void cannotGetPathWhenPathProviderIsNotSet() {
        PropertyMatcher<String> propertyMatcher = new PropertyMatcher<String>("myProperty", null);

        Exception e = assertThrows(IllegalStateException.class, propertyMatcher::getPath);
        assertEquals("No PathProvider assigned", e.getMessage());
    }

    @Test
    public void setsSelfAsPathProviderOnAssignedMatchers() {
        PropertyMatcher<String> propertyMatcher =
                new PropertyMatcher<String>("myProperty", mockRegistry, mockPathProvider);
        PathAwareMatcherStub<String> pathAwareMatcherStub = new PathAwareMatcherStub<String>();

        propertyMatcher.setMatcher(pathAwareMatcherStub);

        assertSame(propertyMatcher, pathAwareMatcherStub.getPathProvider());
    }

    @Test
    public void isSpecifiedIsFalseWhenNoMatcherAssigned() {
        PropertyMatcher<String> propertyMatcher =
                new PropertyMatcher<String>("myProperty", null, mockPathProvider);

        assertFalse(propertyMatcher.isSpecified());
    }

    @Test
    public void isSpecifiedIsTrueWhenMatcherAssigned() {
        PropertyMatcher<String> propertyMatcher =
                new PropertyMatcher<String>("myProperty", null, mockPathProvider);

        propertyMatcher.setMatcher(anything());

        assertTrue(propertyMatcher.isSpecified());
    }

    @Test
    public void matchesIfNoMatcherAssigned() {
        PropertyMatcher<String> propertyMatcher =
                new PropertyMatcher<String>("myProperty", null, mockPathProvider);

        assertTrue(propertyMatcher.matches("any string"));
    }

    @Test
    public void matchesIfAssignedMatcherMatches() {
        PropertyMatcher<String> propertyMatcher =
                new PropertyMatcher<String>("myProperty", null, mockPathProvider);
        propertyMatcher.setMatcher(equalTo("matched string"));

        assertTrue(propertyMatcher.matches("matched string"));
    }

    @Test
    public void matchFailsIfAssignedMatcherFails() {
        PropertyMatcher<String> propertyMatcher =
                new PropertyMatcher<String>("myProperty", null, mockPathProvider);
        propertyMatcher.setMatcher(equalTo("matched string"));

        assertFalse(propertyMatcher.matches("not matched string"));
    }

    @Test
    public void describesWhenMatcherAssigned() {
        PropertyMatcher<String> propertyMatcher =
                new PropertyMatcher<String>("myProperty", null, mockPathProvider);
        PathAwareMatcherStub<String> assignedMatcher = new PathAwareMatcherStub<String>();
        assignedMatcher.setDescriptionText("assigned matcher description text");
        propertyMatcher.setMatcher(assignedMatcher);

        StringDescription description = new StringDescription();
        propertyMatcher.describeTo(description);

        assertEquals("has myProperty (assigned matcher description text)", description.toString());
    }

    @Test
    public void describesWhenNoMatcherAssigned() {
        PropertyMatcher<String> propertyMatcher =
                new PropertyMatcher<String>("myProperty", null, mockPathProvider);

        StringDescription description = new StringDescription();
        propertyMatcher.describeTo(description);

        assertEquals("has myProperty (<any>)", description.toString());
    }

    @Test
    public void describeMismatchDelegatesToAssignedPathAwareMatcher() {
        PropertyMatcher<String> propertyMatcher =
                new PropertyMatcher<String>("myProperty", null, mockPathProvider);
        PathAwareMatcherStub<String> assignedMatcher = new PathAwareMatcherStub<String>();
        assignedMatcher.setMismatchDescriptionText("assigned matcher mismatch description");
        propertyMatcher.setMatcher(assignedMatcher);

        StringDescription description = new StringDescription();
        propertyMatcher.describeMismatch("foo", description);

        assertEquals("assigned matcher mismatch description", description.toString());
    }

    @Test
    public void describeMismatchGeneratesMismatchDescription() {
        PropertyMatcher<String> propertyMatcher =
                new PropertyMatcher<String>("myProperty", null, mockPathProvider);
        propertyMatcher.setMatcher(equalTo("bar"));

        StringDescription description = new StringDescription();
        propertyMatcher.describeMismatch("foo", description);

        assertEquals("myPath.myProperty was \"foo\" (expected \"bar\")", description.toString());
    }

    private static class PathProviderStub implements PathProvider {
        private String path;

        private PathProviderStub(String path) {
            this.path = path;
        }

        @Override
        public String getPath() {
            return path;
        }
    }

    private static class PathAwareMatcherStub<T> extends BaseMatcher<T> implements PathAware {
        private PathProvider pathProvider;
        private String descriptionText = "";
        private String mismatchDescriptionText = "";

        @Override
        public boolean matches(Object item) {
            return false;
        }

        @Override
        public void describeMismatch(Object item, Description mismatchDescription) {
            mismatchDescription.appendText(mismatchDescriptionText);
        }

        @Override
        public void describeTo(Description description) {
            description.appendText(this.descriptionText);
        }

        public PathProvider getPathProvider() {
            return pathProvider;
        }

        @Override
        public void setPathProvider(PathProvider pathProvider) {
            this.pathProvider = pathProvider;
        }

        public void setDescriptionText(String descriptionText) {
            this.descriptionText = descriptionText;
        }

        public void setMismatchDescriptionText(String mismatchDescriptionText) {
            this.mismatchDescriptionText = mismatchDescriptionText;
        }
    }
}
