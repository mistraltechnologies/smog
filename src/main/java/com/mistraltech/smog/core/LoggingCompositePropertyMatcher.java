package com.mistraltech.smog.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A type of {@link CompositePropertyMatcher} that logs mismatches to an SLF4J logger.
 *
 * @param <T> type of matchable target object
 */
public abstract class LoggingCompositePropertyMatcher<T> extends CompositePropertyMatcher<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompositePropertyMatcher.class);
    private static final String LOGGING_CONJUNCTIVE_ADVERB = " and ";

    protected LoggingCompositePropertyMatcher(String matchedObjectDescription) {
        super(matchedObjectDescription);
    }

    @Override
    protected void writeLog(String text) {
        LOGGER.debug(text);
    }
}
