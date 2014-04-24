package com.mistraltech.smog.core;


import org.hamcrest.StringDescription;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class MatchAccumulatorTest {
    private StringDescription mismatchDescription;

    @Before
    public void setUp() {
        mismatchDescription = new StringDescription();
    }

    @Test
    public void givesSuccessResultWhenNoResultSpecifiedAtConstructionAndNoMatchesFail() {
        MatchAccumulator matchAccumulator = MatchAccumulator.matchAccumulator(mismatchDescription);

        assertTrue(matchAccumulator.result());
    }

    @Test
    public void givesSuccessResultWhenSucceedingAtConstructionAndNoMatchesFail() {
        MatchAccumulator matchAccumulator = MatchAccumulator.matchAccumulator(mismatchDescription, true);

        assertTrue(matchAccumulator.result());
    }

    @Test
    public void givesFailureResultWhenFailingAtConstruction() {
        MatchAccumulator matchAccumulator = MatchAccumulator.matchAccumulator(mismatchDescription, false);

        assertFalse(matchAccumulator.result());
    }

    @Test
    public void givesFailureResultWhenAnyMatchFails() {
        MatchAccumulator matchAccumulator = MatchAccumulator.matchAccumulator(mismatchDescription);

        matchAccumulator.matches(equalTo("A"), "A");
        matchAccumulator.matches(equalTo("B"), "-");
        matchAccumulator.matches(equalTo("C"), "C");

        assertFalse(matchAccumulator.result());
    }

    @Test
    public void givesSuccessResultWhenAllMatchesSucceed() {
        MatchAccumulator matchAccumulator = MatchAccumulator.matchAccumulator(mismatchDescription);

        matchAccumulator.matches(equalTo("A"), "A");
        matchAccumulator.matches(equalTo("B"), "B");
        matchAccumulator.matches(equalTo("C"), "C");

        assertTrue(matchAccumulator.result());
    }

    @Test
    public void doesNotUpdateMismatchDescriptionWhenMatchesSucceed() {
        MatchAccumulator matchAccumulator = MatchAccumulator.matchAccumulator(mismatchDescription);

        matchAccumulator.matches(equalTo("A"), "A");
        matchAccumulator.matches(equalTo("B"), "B");
        matchAccumulator.matches(equalTo("C"), "C");

        assertEquals("", mismatchDescription.toString());
    }

    @Test
    public void updatesMismatchDescriptionForFailingMatches() {
        MatchAccumulator matchAccumulator = MatchAccumulator.matchAccumulator(mismatchDescription);

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
}
