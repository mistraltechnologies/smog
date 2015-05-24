package com.mistraltech.smog.core;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.HashSet;
import java.util.Set;

/**
 * A helper class that interposes in the invocation of the matches() method of multiple matchers and
 * coordinates the building of a cumulative mismatch description for those that fail.
 */
public final class MatchAccumulator {
    /**
     * The text that appears between elements of a mismatch description.
     */
    public static final String MISMATCH_CONJUNCTIVE_ADVERB = "\n     and: ";

    /**
     * Indicates whether the matchers tested so far have all succeeded.
     */
    private boolean currentlyMatching = true;

    /**
     * Builds the mismatch description.
     */
    private Description mismatchDescription;

    /**
     * Identifies those matchers that have been tested. This allows clients to ask whether
     * a given matcher has been tested previously.
     */
    private Set<Matcher<?>> appliedMatchers;

    /**
     * Constructor.
     *
     * @param mismatchDescription the description object used for recording the mismatch description
     */
    public MatchAccumulator(Description mismatchDescription) {
        this.mismatchDescription = mismatchDescription;
        this.appliedMatchers = new HashSet<Matcher<?>>();
    }

    /**
     * Invokes the {@link Matcher#matches(java.lang.Object)} method on the supplied matcher against a given target object
     * and appends relevant text to the mismatch description if the match fails.
     *
     * @param matcher the matcher to invoke
     * @param item the target object to be matched
     * @param <P> the type of item to be matched
     * @return this instance, to allow multiple calls to be chained
     */
    public <P> MatchAccumulator matches(Matcher<?> matcher, P item) {
        if (!matcher.matches(item)) {
            handleMismatch(matcher, item);
        }

        appliedMatchers.add(matcher);
        return this;
    }

    private <P> void handleMismatch(Matcher<?> matcher, P item) {
        if (!currentlyMatching)
        {
            // This is not the first failure so add some joining text to mismatch description
            mismatchDescription.appendText(MISMATCH_CONJUNCTIVE_ADVERB);
        }

        matcher.describeMismatch(item, mismatchDescription);

        currentlyMatching = false;
    }

    /**
     * The cumulative currentlyMatching result.
     *
     * @return true if this instance was constructed with currentlyMatching equal to or
     * defaulted to true and all subsequent currentlyMatching() invocations were successful; false otherwise.
     */
    public boolean result() {
        return currentlyMatching;
    }

    /**
     * Indicates whether the provided matcher has been applied (i.e. its matches method invoked
     * and its result accumulated).
     *
     * @param matcher the subject of the query
     * @return true if the matches method of the supplied matcher has been invoked; false otherwise
     */
    public boolean hasBeenApplied(Matcher<?> matcher) {
        return appliedMatchers.contains(matcher);
    }
}
