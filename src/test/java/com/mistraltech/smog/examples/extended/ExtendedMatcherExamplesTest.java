package com.mistraltech.smog.examples.extended;

import org.hamcrest.Matcher;
import org.junit.Test;

import com.mistraltech.smog.examples.model.Address;
import com.mistraltech.smog.examples.model.Addressee;
import com.mistraltech.smog.examples.model.Person;
import com.mistraltech.smog.examples.model.PostCode;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import static com.mistraltech.smog.examples.extended.matcher.AddresseeMatcher.anAddresseeLike;
import static com.mistraltech.smog.examples.extended.matcher.AddresseeMatcher.anAddresseeThat;
import static com.mistraltech.smog.examples.extended.matcher.PersonMatcher.aPersonLike;
import static com.mistraltech.smog.examples.extended.matcher.PersonMatcher.aPersonThat;
import static com.mistraltech.smog.examples.utils.MatcherTestUtils.assertDescription;
import static com.mistraltech.smog.examples.utils.MatcherTestUtils.assertMismatch;

public class ExtendedMatcherExamplesTest
{
    @Test
    public void testExtensiblePersonMatcherSucceedsWhenMatches()
    {
        Matcher<Person> matcher = is(aPersonThat().hasName("bob"));

        Person input = new Person("bob", 36, new Address(21, new PostCode("out", "in")));

        assertDescription(matcher, "is a Person that (has name ('bob'))");
        assertThat(input, matcher);
    }

    @Test
    public void testExtensiblePersonMatcherFailsWhenMismatches()
    {
        Matcher<Person> matcher = is(aPersonThat().hasName("bob"));

        Person input = new Person("dennis", 36, new Address(21, new PostCode("out", "in")));

        assertMismatch(input, matcher, "name was 'dennis' (expected 'bob')");
    }

    @Test
    public void testAPersonLikeReturnsPopulatedMatcher()
    {
        Address address = new Address(21, new PostCode("out", "in"));
        Person person1 = new Person("dennis", 36, address);
        Person person2 = new Person("bob", 34, address);

        Matcher<Person> matcher = is(aPersonLike(person1).hasName("bob"));

        // Name matches, but age has not been overridden and doesn't match
        assertMismatch(person2, matcher, "age was <34> (expected <36>)");
    }

    @Test
    public void testExtensibleAddresseeMatcherSucceedsWhenMatches()
    {
        Matcher<Addressee> matcher = is(anAddresseeThat().hasName("bob"));

        Person input = new Person("bob", 36, new Address(21, new PostCode("out", "in")));

        assertDescription(matcher, "is an Addressee that (has name ('bob'))");
        assertThat(input, matcher);
    }

    @Test
    public void testExtensibleAddresseeMatcherFailsWhenMismatches()
    {
        Matcher<Addressee> matcher = is(anAddresseeThat().hasName("bob"));

        Person input = new Person("dennis", 36, new Address(21, new PostCode("out", "in")));

        assertMismatch(input, matcher, "name was 'dennis' (expected 'bob')");
    }

    @Test
    public void testAnAddresseeLikeReturnsPopulatedMatcher()
    {
        Address address = new Address(21, new PostCode("out", "in"));
        Person person1 = new Person("dennis", 36, address);
        Person person2 = new Person("bob", 34, address);

        Matcher<Addressee> matcher = is(anAddresseeLike(person1));

        // Name mismatches
        assertMismatch(person2, matcher, "name was 'bob' (expected 'dennis')");
    }
}
