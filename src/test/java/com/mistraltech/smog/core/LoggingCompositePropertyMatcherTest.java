package com.mistraltech.smog.core;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoggingCompositePropertyMatcherTest {

    private Logger root;
    private Appender<ILoggingEvent> mockAppender;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void before() {
        createMockAppender();
    }

    @AfterEach
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
        assertEquals(Level.DEBUG, loggingEventCaptor.getValue().getLevel(), "level");
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

    private void removeMockAppender() {
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
