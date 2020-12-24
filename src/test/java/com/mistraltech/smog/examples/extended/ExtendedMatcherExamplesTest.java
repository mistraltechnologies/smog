package com.mistraltech.smog.examples.extended;

import com.mistraltech.smog.examples.model.Addressee;
import com.mistraltech.smog.examples.model.Person;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import static com.mistraltech.smog.examples.extended.matcher.AddresseeMatcher.anAddresseeLike;
import static com.mistraltech.smog.examples.extended.matcher.AddresseeMatcher.anAddresseeThat;
import static com.mistraltech.smog.examples.extended.matcher.PersonMatcher.aPersonLike;
import static com.mistraltech.smog.examples.extended.matcher.PersonMatcher.aPersonThat;
import static com.mistraltech.smog.examples.model.PersonBuilder.aPerson;
import static com.mistraltech.smog.examples.utils.MatcherTestUtils.assertDescription;
import static com.mistraltech.smog.examples.utils.MatcherTestUtils.assertMismatch;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ExtendedMatcherExamplesTest {
    private Person bob = aPerson()
            .withName("bob")
            .withAge(34)
            .withAddress(null)
            .build();

    private Person dennis = aPerson()
            .withName("dennis")
            .withAge(36)
            .withAddress(null)
            .build();


    @Test
    public void testExtensiblePersonMatcherSucceedsWhenMatches() {
        Matcher<Person> matcher = is(aPersonThat().hasName("bob"));

        assertDescription(matcher, "is a Person that (has name ('bob'))");
        assertThat(bob, matcher);
    }

    @Test
    public void testExtensiblePersonMatcherFailsWhenMismatches() {
        Matcher<Person> matcher = is(aPersonThat().hasName("bob"));

        assertMismatch(dennis, matcher, "name was 'dennis' (expected 'bob')");
    }

    @Test
    public void testAPersonLikeReturnsPopulatedMatcher() {
        Matcher<Person> matcher = is(aPersonLike(dennis).hasName("bob"));

        // Name matches, but age has not been overridden and doesn't match
        assertMismatch(bob, matcher, "age was <34> (expected <36>)");
    }

    @Test
    public void testExtensibleAddresseeMatcherSucceedsWhenMatches() {
        Matcher<Addressee> matcher = is(anAddresseeThat().hasName("bob"));

        assertDescription(matcher, "is an Addressee that (has name ('bob'))");
        assertThat(bob, matcher);
    }

    @Test
    public void testExtensibleAddresseeMatcherFailsWhenMismatches() {
        Matcher<Addressee> matcher = is(anAddresseeThat().hasName("bob"));

        assertMismatch(dennis, matcher, "name was 'dennis' (expected 'bob')");
    }

    @Test
    public void testAnAddresseeLikeReturnsPopulatedMatcher() {
        Matcher<Addressee> matcher = is(anAddresseeLike(dennis));

        // Name mismatches
        assertMismatch(bob, matcher, "name was 'bob' (expected 'dennis')");
    }
}
