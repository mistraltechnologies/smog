package com.mistraltech.smog.core;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.DiagnosingMatcher;
import org.hamcrest.StringDescription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CompositePropertyMatcherTest {
    private Description description;

    @BeforeEach
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

        assertEquals(1, cpm.getLogMessages().size(), "log message count");

        String expectedLogMessage = String.format("%s didn't match - %s",
                cpm.getClass().getName(),
                FailingTargetItemMatcher.MISMATCH_DESCRIPTION);

        assertEquals(expectedLogMessage, cpm.getLogMessages().get(0), "log message content");
    }

    @Test
    public void canOverrideDefaultLoggingDescriptionFactoryMethod() {
        TargetItemCompositePropertyMatcher cpm = new TargetItemCompositePropertyMatcher("foo");
        cpm.setMatchesSafelyResult(false);
        cpm.setOverrideLoggingDescription("message: ");
        cpm.matches(new TargetItem());

        assertEquals(1, cpm.getLogMessages().size(), "log message count");

        assertThat("log message content", cpm.getLogMessages().get(0), startsWith("message: "));
    }

    @Test
    public void mismatchesAreNotLoggedWhenSubordinateOfPathProvider() {
        TargetItemCompositePropertyMatcher cpm = new TargetItemCompositePropertyMatcher("foo");
        cpm.setMatchesSafelyResult(false);
        cpm.setPathProvider(new StubPathProvider());

        cpm.matches(new TargetItem());

        assertEquals(0, cpm.getLogMessages().size(), "log message count");
    }

    @Test
    public void matchesAreNotLogged() {
        TargetItemCompositePropertyMatcher cpm = new TargetItemCompositePropertyMatcher("foo");
        cpm.setMatchesSafelyResult(true);

        cpm.matches(new TargetItem());

        assertEquals(0, cpm.getLogMessages().size(), "log message count");
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

    @Test
    public void cannotConstructWithNullDescription() {
        assertThrows(IllegalArgumentException.class, () -> new TargetItemCompositePropertyMatcher(null));
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
        cpm.registerPropertyMatcher(new SpecifiedPropertyMatcher("bar", "x"));
        cpm.describeTo(description);

        assertEquals("foo that (has bar (\"x\"))", description.toString());
    }

    @Test
    public void canDescribeWithMultiplePropertyMatchers() {
        TargetItemCompositePropertyMatcher cpm = new TargetItemCompositePropertyMatcher("foo");
        cpm.registerPropertyMatcher(new SpecifiedPropertyMatcher("bar1", "x"));
        cpm.registerPropertyMatcher(new SpecifiedPropertyMatcher("bar2", "y"));
        cpm.describeTo(description);

        assertEquals("foo that (has bar1 (\"x\") and has bar2 (\"y\"))", description.toString());
    }

    @Test
    public void describeIgnoresUnspecifiedPropertyMatchers() {
        TargetItemCompositePropertyMatcher cpm = new TargetItemCompositePropertyMatcher("foo");
        cpm.registerPropertyMatcher(new PropertyMatcher("bar1", null));
        cpm.registerPropertyMatcher(new SpecifiedPropertyMatcher("bar2", "y"));
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

    @Test
    public void assignsPathProviderToPropertyMatchersWithoutPathProvider() {
        TargetItemCompositePropertyMatcher cpm = new TargetItemCompositePropertyMatcher("foo");
        PropertyMatcher propertyMatcher = new PropertyMatcher("", null);

        cpm.registerPropertyMatcher(propertyMatcher);

        assertSame(cpm, propertyMatcher.getPathProvider());
    }

    @Test
    public void doesNotOverridePathProviderOnPropertyMatchers() {
        TargetItemCompositePropertyMatcher cpm = new TargetItemCompositePropertyMatcher("foo");
        PathProvider originalPathProvider = new StubPathProvider();
        PropertyMatcher propertyMatcher = new PropertyMatcher("", null, originalPathProvider);

        cpm.registerPropertyMatcher(propertyMatcher);

        assertSame(originalPathProvider, propertyMatcher.getPathProvider());
    }

    @Test
    public void matchesSafelyInvokesPropertyMatchers() {
        final SpecifiedPropertyMatcher propertyMatcher1 = new SpecifiedPropertyMatcher("prop1", "x");
        final SpecifiedPropertyMatcher propertyMatcher2 = new SpecifiedPropertyMatcher("prop2", "y");

        CompositePropertyMatcher<String> cpm = new CompositePropertyMatcher<String>("foo");
        cpm.registerPropertyMatcher(propertyMatcher1);
        cpm.registerPropertyMatcher(propertyMatcher2);

        boolean result = cpm.matches("x");

        assertFalse(result, "match should fail");

        // Matchers are invoked twice because match failed
        assertEquals(2, propertyMatcher1.getInvocationCount());
        assertEquals(2, propertyMatcher2.getInvocationCount());
    }

    @Test
    public void matchesSafelyDoesNotInvokePropertyMatchersThatAreAlreadyApplied() {
        final SpecifiedPropertyMatcher propertyMatcher1 = new SpecifiedPropertyMatcher("prop1", "x");
        final SpecifiedPropertyMatcher propertyMatcher2 = new SpecifiedPropertyMatcher("prop2", "x");

        // MatchesSafely invokes matches on propertyMatcher1, then delegates to superclass implementation.
        // We expect propertyMatcher1 to not get invoked a second time in the superclass implementation.
        CompositePropertyMatcher<String> cpm = new CompositePropertyMatcher<String>("foo") {
            @Override
            protected void matchesSafely(String item, MatchAccumulator matchAccumulator) {
                matchAccumulator.matches(propertyMatcher1, item);
                super.matchesSafely(item, matchAccumulator);
            }
        };

        cpm.registerPropertyMatcher(propertyMatcher1);
        cpm.registerPropertyMatcher(propertyMatcher2);

        cpm.matches("x");

        assertEquals(1, propertyMatcher1.getInvocationCount());
        assertEquals(1, propertyMatcher2.getInvocationCount());
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
        private int invocationCount;

        public SpecifiedPropertyMatcher(String propertyName, String expected) {
            super(propertyName, null);
            setMatcher(CoreMatchers.equalTo(expected));
        }

        @Override
        public boolean matches(Object item) {
            invocationCount++;
            return super.matches(item);
        }

        public int getInvocationCount() {
            return invocationCount;
        }
    }
}

