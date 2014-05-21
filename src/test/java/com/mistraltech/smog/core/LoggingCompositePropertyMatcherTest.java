package com.mistraltech.smog.core;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class LoggingCompositePropertyMatcherTest {

    private Logger root;
    private Appender<ILoggingEvent> mockAppender;

    @Before
    @SuppressWarnings("unchecked")
    public void before() {
        createMockAppender();
    }

    @After
    public void after() {
        removeMockAppender();
    }

    @Test
    public void messageIsLoggedWhenMatchFails() {
        CompositePropertyMatcher<Item> cpm = new LoggingCompositePropertyMatcher<Item>("parent");
        addPropertyMatcher(cpm, "val1", "foo");
        addPropertyMatcher(cpm, "val2", "foo");

        final Appender<ILoggingEvent> mockAppender = createMockAppender();

        cpm.matches(new Item("bar"));

        ArgumentCaptor<ILoggingEvent> loggingEventCaptor = ArgumentCaptor.forClass(ILoggingEvent.class);
        verify(mockAppender).doAppend(loggingEventCaptor.capture());

        assertThat("message", loggingEventCaptor.getValue().getFormattedMessage(),
                endsWith("didn't match - val1 was \"bar\" (expected \"foo\") and val2 was \"bar\" (expected \"foo\")"));
        assertEquals("level", Level.DEBUG, loggingEventCaptor.getValue().getLevel());
    }

    private void addPropertyMatcher(CompositePropertyMatcher<Item> cpm, String propertyName, String value) {
        PropertyMatcher<String> propertyMatcher = new ReflectingPropertyMatcher<String>(propertyName, cpm);
        propertyMatcher.setMatcher(equalTo(value));
    }

    @SuppressWarnings("unchecked")
    private Appender<ILoggingEvent> createMockAppender() {
        mockAppender = mock(Appender.class);
        when(mockAppender.getName()).thenReturn("MOCK");

        root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.addAppender(mockAppender);

        return mockAppender;
    }

    private void removeMockAppender()
    {
        if (root != null && mockAppender != null) {
            root.detachAppender(mockAppender);
        }
    }

    private static class Item {
        private String value;

        private Item(String value) {
            this.value = value;
        }

        @SuppressWarnings("UnusedDeclaration")
        public String getVal1() {
            return value;
        }

        @SuppressWarnings("UnusedDeclaration")
        public String getVal2() {
            return value;
        }
    }
}
