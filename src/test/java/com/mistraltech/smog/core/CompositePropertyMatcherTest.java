package com.mistraltech.smog.core;

import org.hamcrest.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.*;

public class CompositePropertyMatcherTest {
    private Description description;

    @Before
    public void setUp() {
        description = new StringDescription();
    }

    @Test
    public void matchesFailsWhenItemIsNull() {
        CompositePropertyMatcher<TargetItem> cpm = new TargetItemCompositePropertyMatcher("foo");

        assertFalse(cpm.matches(null));
    }

    @Test
    public void matchesFailsWhenItemIsNotExpectedType() {
        CompositePropertyMatcher<TargetItem> cpm = new TargetItemCompositePropertyMatcher("foo");

        assertFalse(cpm.matches(1));
    }

    @Test
    public void matchesFailsWhenMatchesSafelyWithDescriptionFails() {
        TargetItemCompositePropertyMatcher cpm = new TargetItemCompositePropertyMatcher("foo");
        cpm.setMatchesSafelyResult(false);

        assertFalse(cpm.matches(new TargetItem()));
    }

    @Test
    public void matchesSucceedsWhenMatchesSafelyWithDescriptionSucceeds() {
        TargetItemCompositePropertyMatcher cpm = new TargetItemCompositePropertyMatcher("foo");
        cpm.setMatchesSafelyResult(true);

        assertTrue(cpm.matches(new TargetItem()));
    }

    @Test
    public void matchesCanSucceedForSubclassesOfExpectedType() {
        TargetItemCompositePropertyMatcher cpm = new TargetItemCompositePropertyMatcher("foo");
        cpm.setMatchesSafelyResult(true);

        assertTrue(cpm.matches(new TargetItem() {
            // Empty subclass
        }));
    }

    @Test
    public void mismatchesAreLoggedWhenNotSubordinateOfPathProvider() {
        TargetItemCompositePropertyMatcher cpm = new TargetItemCompositePropertyMatcher("foo");
        cpm.setMatchesSafelyResult(false);

        cpm.matches(new TargetItem());

        assertEquals("log message count", 1, cpm.getLogMessages().size());

        String expectedLogMessage = String.format("%s didn't match - %s",
                cpm.getClass().getName(),
                FailingTargetItemMatcher.MISMATCH_DESCRIPTION);

        assertEquals("log message content", expectedLogMessage, cpm.getLogMessages().get(0));
    }

    @Test
    public void canOverrideDefaultLoggingDescriptionFactoryMethod() {
        TargetItemCompositePropertyMatcher cpm = new TargetItemCompositePropertyMatcher("foo");
        cpm.setMatchesSafelyResult(false);
        cpm.setOverrideLoggingDescription("message: ");
        cpm.matches(new TargetItem());

        assertEquals("log message count", 1, cpm.getLogMessages().size());

        assertThat("log message content", cpm.getLogMessages().get(0), startsWith("message: "));
    }

    @Test
    public void mismatchesAreNotLoggedWhenSubordinateOfPathProvider() {
        TargetItemCompositePropertyMatcher cpm = new TargetItemCompositePropertyMatcher("foo");
        cpm.setMatchesSafelyResult(false);
        cpm.setPathProvider(new StubPathProvider());

        cpm.matches(new TargetItem());

        assertEquals("log message count", 0, cpm.getLogMessages().size());
    }

    @Test
    public void matchesAreNotLogged() {
        TargetItemCompositePropertyMatcher cpm = new TargetItemCompositePropertyMatcher("foo");
        cpm.setMatchesSafelyResult(true);

        cpm.matches(new TargetItem());

        assertEquals("log message count", 0, cpm.getLogMessages().size());
    }

    @Test
    public void getPathDelegatesToPathProvider() {
        TargetItemCompositePropertyMatcher cpm = new TargetItemCompositePropertyMatcher("foo");
        cpm.setMatchesSafelyResult(true);
        cpm.setPathProvider(new StubPathProvider());

        assertEquals(StubPathProvider.PATH, cpm.getPath());
    }

    @Test
    public void getPathWithoutPathProviderReturnsEmptyString() {
        TargetItemCompositePropertyMatcher cpm = new TargetItemCompositePropertyMatcher("foo");
        cpm.setMatchesSafelyResult(true);

        assertEquals("", cpm.getPath());
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotConstructWithNullDescription() {
        new TargetItemCompositePropertyMatcher(null);
    }

    @Test
    public void canDescribeWithoutPropertyMatchers() {
        TargetItemCompositePropertyMatcher cpm = new TargetItemCompositePropertyMatcher("foo");

        cpm.describeTo(description);

        assertEquals("foo that ()", description.toString());
    }

    @Test
    public void canDescribeWithSinglePropertyMatcher() {
        TargetItemCompositePropertyMatcher cpm = new TargetItemCompositePropertyMatcher("foo");
        cpm.addPropertyMatchers(new SpecifiedPropertyMatcher("bar", cpm, "x"));
        cpm.describeTo(description);

        assertEquals("foo that (has bar (\"x\"))", description.toString());
    }

    @Test
    public void canDescribeWithMultiplePropertyMatchers() {
        TargetItemCompositePropertyMatcher cpm = new TargetItemCompositePropertyMatcher("foo");
        cpm.addPropertyMatchers(
                new SpecifiedPropertyMatcher("bar1", cpm, "x"),
                new SpecifiedPropertyMatcher("bar2", cpm, "y"));
        cpm.describeTo(description);

        assertEquals("foo that (has bar1 (\"x\") and has bar2 (\"y\"))", description.toString());
    }

    @Test
    public void describeIgnoresUnspecifiedPropertyMatchers() {
        TargetItemCompositePropertyMatcher cpm = new TargetItemCompositePropertyMatcher("foo");
        cpm.addPropertyMatchers(
                new PropertyMatcher("bar1", cpm),
                new SpecifiedPropertyMatcher("bar2", cpm, "y"));
        cpm.describeTo(description);

        assertEquals("foo that (has bar2 (\"y\"))", description.toString());
    }

    @Test
    public void canDescribeMismatchForNullItem() {
        TargetItemCompositePropertyMatcher cpm = new TargetItemCompositePropertyMatcher("foo");
        cpm.setPathProvider(new StubPathProvider());

        cpm.describeMismatch(null, description);

        assertEquals(StubPathProvider.PATH + " was null", description.toString());
    }

    @Test
    public void canDescribeMismatchForItemOfIncorrectType() {
        TargetItemCompositePropertyMatcher cpm = new TargetItemCompositePropertyMatcher("foo");
        cpm.setPathProvider(new StubPathProvider());

        cpm.describeMismatch("bar", description);

        assertEquals(StubPathProvider.PATH + " was \"bar\"", description.toString());
    }

    @Test
    public void canDescribeMismatchForItemOfCorrectType() {
        TargetItemCompositePropertyMatcher cpm = new TargetItemCompositePropertyMatcher("foo");
        cpm.setMatchesSafelyResult(false);

        cpm.describeMismatch(new TargetItem(), description);

        assertEquals(FailingTargetItemMatcher.MISMATCH_DESCRIPTION, description.toString());
    }

    private static class TargetItem {
    }

    private static class TargetItemCompositePropertyMatcher extends CompositePropertyMatcher<TargetItem> {
        private List<String> logMessages = new ArrayList<String>();
        private Boolean matchesSafelyResult = null;
        private String overrideLoggingDescription;

        private TargetItemCompositePropertyMatcher(String matchedObjectDescription) {
            super(matchedObjectDescription);
        }

        @Override
        protected void matchesSafely(TargetItem item, MatchAccumulator matchAccumulator) {
            if (matchesSafelyResult == null) {
                throw new IllegalStateException("No result specified for matchesSafely");
            }

            if (!matchesSafelyResult) {
                matchAccumulator.matches(new FailingTargetItemMatcher(), item);
            }
        }

        public void setMatchesSafelyResult(boolean matchesSafelyResult) {
            this.matchesSafelyResult = matchesSafelyResult;
        }

        @Override
        protected Description createLogMismatchDescription() {
            if (overrideLoggingDescription == null) {
                return super.createLogMismatchDescription();
            } else {
                return new StringDescription().appendText(overrideLoggingDescription);
            }
        }

        @Override
        protected void writeLog(String text) {
            logMessages.add(text);
        }

        public List<String> getLogMessages() {
            return logMessages;
        }

        public void setOverrideLoggingDescription(String loggingDescription) {
            this.overrideLoggingDescription = loggingDescription;
        }
    }

    private static class FailingTargetItemMatcher extends DiagnosingMatcher<TargetItem> {
        public static final String MISMATCH_DESCRIPTION = "composite property matcher mismatch description";

        @Override
        protected boolean matches(Object item, Description mismatchDescription) {
            mismatchDescription.appendText(MISMATCH_DESCRIPTION);
            return false;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("to not match");
        }
    }

    private static class StubPathProvider implements PathProvider {
        private static final String PATH = "provided path";

        @Override
        public String getPath() {
            return PATH;
        }
    }

    private class SpecifiedPropertyMatcher extends PropertyMatcher<String> {
        public SpecifiedPropertyMatcher(String propertyName, PathProvider pathProvider, String expected) {
            super(propertyName, pathProvider);
            setMatcher(CoreMatchers.equalTo(expected));
        }
    }
}

