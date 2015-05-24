package com.mistraltech.smog.core;

import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A type of {@link CompositePropertyMatcher} that logs mismatches to an SLF4J logger.
 *
 * @param <T> type of matchable target object
 */
public class LoggingCompositePropertyMatcher<T> extends CompositePropertyMatcher<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompositePropertyMatcher.class);
    private static final String LOGGING_CONJUNCTIVE_ADVERB = " and ";

    protected LoggingCompositePropertyMatcher(String matchedObjectDescription) {
        super(matchedObjectDescription);
    }

    @Override
    protected Description createLogMismatchDescription() {
        Description description = new TextSubstitutingDescription(MatchAccumulator.MISMATCH_CONJUNCTIVE_ADVERB, LOGGING_CONJUNCTIVE_ADVERB);
        description.appendText(String.format("%s didn't match - ", this.getClass().getName()));
        return description;
    }

    @Override
    protected void writeLog(String text) {
        LOGGER.debug(text);
    }

    /**
     * A type of Description that substitutes parts of the description matching a regular expression
     * with some other text.
     */
    private static class TextSubstitutingDescription extends StringDescription {
        final String regex;
        final String replacement;

        public TextSubstitutingDescription(String regex, String replacement) {
            this.regex = regex;
            this.replacement = replacement;
        }

        @Override
        public String toString() {
            return super.toString().replaceAll(regex, replacement);
        }
    }
}
