package com.mistraltech.smog.core;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MatchAccumulatorTest {
    private StringDescription mismatchDescription;

    @BeforeEach
    public void setUp() {
        mismatchDescription = new StringDescription();
    }

    @Test
    public void givesSuccessResultWhenNoMatchesAreProcessed() {
        MatchAccumulator matchAccumulator = new MatchAccumulator(mismatchDescription);

        assertTrue(matchAccumulator.result());
    }

    @Test
    public void givesFailureResultWhenAnyMatchFails() {
        MatchAccumulator matchAccumulator = new MatchAccumulator(mismatchDescription);

        matchAccumulator.matches(equalTo("A"), "A");
        matchAccumulator.matches(equalTo("B"), "-");
        matchAccumulator.matches(equalTo("C"), "C");

        assertFalse(matchAccumulator.result());
    }

    @Test
    public void givesSuccessResultWhenAllMatchesSucceed() {
        MatchAccumulator matchAccumulator = new MatchAccumulator(mismatchDescription);

        matchAccumulator.matches(equalTo("A"), "A");
        matchAccumulator.matches(equalTo("B"), "B");
        matchAccumulator.matches(equalTo("C"), "C");

        assertTrue(matchAccumulator.result());
    }

    @Test
    public void doesNotUpdateMismatchDescriptionWhenMatchesSucceed() {
        MatchAccumulator matchAccumulator = new MatchAccumulator(mismatchDescription);

        matchAccumulator.matches(equalTo("A"), "A");
        matchAccumulator.matches(equalTo("B"), "B");
        matchAccumulator.matches(equalTo("C"), "C");

        assertEquals("", mismatchDescription.toString());
    }

    @Test
    public void updatesMismatchDescriptionForFailingMatches() {
        MatchAccumulator matchAccumulator = new MatchAccumulator(mismatchDescription);

        matchAccumulator.matches(equalTo("A"), "a"); // mismatch
        matchAccumulator.matches(equalTo("B"), "B");
        matchAccumulator.matches(equalTo("C"), "c"); // mismatch
        matchAccumulator.matches(equalTo("D"), "d"); // mismatch
        matchAccumulator.matches(equalTo("E"), "E");

        String expectedMismatchDescription = "was \"a\""
                + MatchAccumulator.MISMATCH_CONJUNCTIVE_ADVERB
                + "was \"c\""
                + MatchAccumulator.MISMATCH_CONJUNCTIVE_ADVERB
                + "was \"d\"";

        assertEquals(expectedMismatchDescription, mismatchDescription.toString());
    }

    @Test
    public void canDetermineIfMatcherHasBeenApplied() {
        MatchAccumulator matchAccumulator = new MatchAccumulator(mismatchDescription);

        Matcher<Object> matcher1 = any(Object.class);
        Matcher<Object> matcher2 = any(Object.class);
        Matcher<Object> matcher3 = any(Object.class);

        Object item = new Object();
        matchAccumulator.matches(matcher1, item);
        matchAccumulator.matches(matcher2, item);

        assertTrue(matchAccumulator.hasBeenApplied(matcher1));
        assertTrue(matchAccumulator.hasBeenApplied(matcher2));
        assertFalse(matchAccumulator.hasBeenApplied(matcher3));
    }
}
