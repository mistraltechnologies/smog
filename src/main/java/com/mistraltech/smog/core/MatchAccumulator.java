package com.mistraltech.smog.core;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * A helper class that interposes in the invocation of the matches() method of multiple matchers and
 * coordinates the building of a cumulative mismatch description for those that fail.
 */
public final class MatchAccumulator {
    public static final String MISMATCH_JOINING_STRING = "\n     and: ";

    private boolean matches = true;
    private Description mismatchDescription;

    private MatchAccumulator(Description mismatchDescription, boolean currentlyMatching) {
        this.mismatchDescription = mismatchDescription;
        this.matches = currentlyMatching;
    }

    private MatchAccumulator(Description mismatchDescription) {
        this(mismatchDescription, true);
    }

    /**
     * Factory method for use by non-inheriting matchers.
     *
     * @param mismatchDescription the description object used for recording the mismatch description
     * @return a new MatchAccumulator instance
     */
    public static MatchAccumulator matchAccumulator(Description mismatchDescription) {
        return new MatchAccumulator(mismatchDescription);
    }

    /**
     * Factory method for use by inheriting matchers - that is, matchers that inherit some of their
     * properties from a superclass.
     * <p/>
     * In this case it is desirable to construct the MatchAccumulator with
     * an indicator of whether preceding property matches (performed by the matcher superclass) have
     * passed or failed.
     *
     * @param mismatchDescription the description object used for recording the mismatch description
     * @param currentlyMatching a flag to indicate if, in the case that this instance is participating as
     * a sub-scope of a larger matching process, the larger matching process is already failing.
     * @return a new MatchAccumulator instance
     */
    public static MatchAccumulator matchAccumulator(Description mismatchDescription, boolean currentlyMatching) {
        return new MatchAccumulator(mismatchDescription, currentlyMatching);
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
    public <P> MatchAccumulator matches(Matcher<? super P> matcher, P item) {
        boolean localMatches = matcher.matches(item);

        if (!localMatches) {
            if (!matches) // i.e. this is not the first failure
            {
                mismatchDescription.appendText(MISMATCH_JOINING_STRING);
            }

            matcher.describeMismatch(item, mismatchDescription);
            matches = false;
        }

        return this;
    }

    /**
     * The cumulative matches result.
     *
     * @return true if this instance was constructed with currentlyMatching equal to or
     * defaulted to true and all subsequent matches() invocations were successful; false otherwise.
     */
    public boolean result() {
        return matches;
    }
}
