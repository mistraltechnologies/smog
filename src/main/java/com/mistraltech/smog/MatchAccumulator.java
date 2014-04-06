package com.mistraltech.smog;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * A helper class that interposes in the invocation of the matches() method of multiple matchers and
 * coordinates the building of a cumulative mismatch description for those that fail.
 */
public final class MatchAccumulator {
    public static final String    MISMATCH_JOINING_STRING = "\n     and: ";

    private boolean matches = true;
    private Description mismatchDescription;

    /**
     * Constructor.
     *
     * @param mismatchDescription the description object used for recording the mismatch description
     * @param currentlyMatching a flag to indicate if, in the case that this instance is participating as
     * a sub-scope of a larger matching process, the larger matching process is already failing.
     */
    private MatchAccumulator(Description mismatchDescription, boolean currentlyMatching) {
        this.mismatchDescription = mismatchDescription;
        this.matches = currentlyMatching;
    }

    private MatchAccumulator(Description mismatchDescription) {
        this(mismatchDescription, true);
    }

    public static MatchAccumulator matchAccumulator(Description mismatchDescription) {
        return new MatchAccumulator(mismatchDescription);
    }

    public static MatchAccumulator matchAccumulator(Description mismatchDescription, boolean currentlyMatching) {
        return new MatchAccumulator(mismatchDescription, currentlyMatching);
    }

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
