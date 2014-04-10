package com.mistraltech.smog.core;


import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;


public class PropertyMatcherTest
{
    private PathProvider mockPathProvider;

    @Before
    public void setUp() throws Exception
    {
        mockPathProvider = new PathProviderStub("myPath");
    }

    @Test
    public void canGetPath() {
        PropertyMatcher<String> propertyMatcher = new PropertyMatcher<String>("myProperty", mockPathProvider);

        assertEquals("myPath.myProperty", propertyMatcher.getPath());
    }

    @Test
    public void setsSelfAsPathProviderOnAssignedMatchers() {
        PropertyMatcher<String> propertyMatcher = new PropertyMatcher<String>("myProperty", mockPathProvider);
        PathAwareMatcherStub<String> pathAwareMatcherStub = new PathAwareMatcherStub<String>();

        propertyMatcher.setMatcher(pathAwareMatcherStub);

        assertSame(propertyMatcher, pathAwareMatcherStub.getPathProvider());
    }

    @Test
    public void isSpecifiedIsFalseWhenNoMatcherAssigned() {
        PropertyMatcher<String> propertyMatcher = new PropertyMatcher<String>("myProperty", mockPathProvider);

        assertFalse(propertyMatcher.isSpecified());
    }

    @Test
    public void isSpecifiedIsTrueWhenMatcherAssigned() {
        PropertyMatcher<String> propertyMatcher = new PropertyMatcher<String>("myProperty", mockPathProvider);

        propertyMatcher.setMatcher(anything());

        assertTrue(propertyMatcher.isSpecified());
    }

    @Test
    public void matchesIfNoMatcherAssigned() {
        PropertyMatcher<String> propertyMatcher = new PropertyMatcher<String>("myProperty", mockPathProvider);

        assertTrue(propertyMatcher.matches("any string"));
    }

    @Test
    public void matchesIfAssignedMatcherMatches() {
        PropertyMatcher<String> propertyMatcher = new PropertyMatcher<String>("myProperty", mockPathProvider);
        propertyMatcher.setMatcher(equalTo("matched string"));

        assertTrue(propertyMatcher.matches("matched string"));
    }

    @Test
    public void matchFailsIfAssignedMatcherFails() {
        PropertyMatcher<String> propertyMatcher = new PropertyMatcher<String>("myProperty", mockPathProvider);
        propertyMatcher.setMatcher(equalTo("matched string"));

        assertFalse(propertyMatcher.matches("not matched string"));
    }

    @Test
    public void describesWhenMatcherAssigned()
    {
        PropertyMatcher<String> propertyMatcher = new PropertyMatcher<String>("myProperty", mockPathProvider);
        PathAwareMatcherStub<String> assignedMatcher = new PathAwareMatcherStub<String>();
        assignedMatcher.setDescriptionText("assigned matcher description text");
        propertyMatcher.setMatcher(assignedMatcher);

        StringDescription description = new StringDescription();
        propertyMatcher.describeTo(description);

        assertEquals("has myProperty (assigned matcher description text)", description.toString());
    }

    @Test
    public void describesWhenNoMatcherAssigned() {
        PropertyMatcher<String> propertyMatcher = new PropertyMatcher<String>("myProperty", mockPathProvider);

        StringDescription description = new StringDescription();
        propertyMatcher.describeTo(description);

        assertEquals("has myProperty (<any>)", description.toString());
    }

    @Test
    public void describeMismatchDelegatesToAssignedPathAwareMatcher()
    {
        PropertyMatcher<String> propertyMatcher = new PropertyMatcher<String>("myProperty", mockPathProvider);
        PathAwareMatcherStub<String> assignedMatcher = new PathAwareMatcherStub<String>();
        assignedMatcher.setMismatchDescriptionText("assigned matcher mismatch description");
        propertyMatcher.setMatcher(assignedMatcher);

        StringDescription description = new StringDescription();
        propertyMatcher.describeMismatch("foo", description);

        assertEquals("assigned matcher mismatch description", description.toString());
    }

    @Test
    public void describeMismatchGeneratesMismatchDescription() {
        PropertyMatcher<String> propertyMatcher = new PropertyMatcher<String>("myProperty", mockPathProvider);
        propertyMatcher.setMatcher(equalTo("bar"));

        StringDescription description = new StringDescription();
        propertyMatcher.describeMismatch("foo", description);

        assertEquals("myPath.myProperty was \"foo\" (expected \"bar\")", description.toString());
    }

    private static class PathProviderStub implements PathProvider {
        private String path;

        private PathProviderStub(String path)
        {
            this.path = path;
        }

        @Override
        public String getPath()
        {
            return path;
        }
    }

    private static class PathAwareMatcherStub<T> implements Matcher<T>, PathAware {
        private PathProvider pathProvider;
        private String descriptionText = "";
        private String mismatchDescriptionText = "";

        @Override
        public boolean matches(Object item)
        {
            return false;
        }

        @Override
        public void describeMismatch(Object item, Description mismatchDescription)
        {
            mismatchDescription.appendText(mismatchDescriptionText);
        }

        @Override
        public void _dont_implement_Matcher___instead_extend_BaseMatcher_()
        {
            // ... unless its a test mock representing some arbitrary Matcher
        }

        @Override
        public void setPathProvider(PathProvider pathProvider)
        {
            this.pathProvider = pathProvider;
        }

        @Override
        public void describeTo(Description description)
        {
            description.appendText(this.descriptionText);
        }

        public PathProvider getPathProvider()
        {
            return pathProvider;
        }

        public void setDescriptionText(String descriptionText)
        {
            this.descriptionText = descriptionText;
        }

        public void setMismatchDescriptionText(String mismatchDescriptionText)
        {
            this.mismatchDescriptionText = mismatchDescriptionText;
        }
    }
}
