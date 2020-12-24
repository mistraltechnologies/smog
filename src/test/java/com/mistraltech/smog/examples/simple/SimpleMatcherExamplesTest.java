package com.mistraltech.smog.examples.simple;

import com.mistraltech.smog.examples.model.Address;
import com.mistraltech.smog.examples.model.Addressee;
import com.mistraltech.smog.examples.model.Person;
import com.mistraltech.smog.examples.model.Phone;
import com.mistraltech.smog.examples.model.PostCode;
import com.mistraltech.smog.examples.simple.matcher.PhoneMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.collection.IsCollectionWithSize;
import org.hamcrest.collection.IsEmptyCollection;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.hamcrest.core.CombinableMatcher;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static com.mistraltech.smog.examples.model.PersonBuilder.aPerson;
import static com.mistraltech.smog.examples.model.PhoneType.Home;
import static com.mistraltech.smog.examples.model.PhoneType.Work;
import static com.mistraltech.smog.examples.simple.matcher.AddressMatcher.anAddressThat;
import static com.mistraltech.smog.examples.simple.matcher.AddresseeMatcher.anAddresseeThat;
import static com.mistraltech.smog.examples.simple.matcher.PersonMatcher.aPersonThat;
import static com.mistraltech.smog.examples.simple.matcher.PhoneMatcher.aPhoneThat;
import static com.mistraltech.smog.examples.simple.matcher.PostCodeMatcher.aPostCodeThat;
import static com.mistraltech.smog.examples.utils.MatcherTestUtils.assertDescription;
import static com.mistraltech.smog.examples.utils.MatcherTestUtils.assertMismatch;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SimpleMatcherExamplesTest {

    private Person bob = aPerson()
            .withName("bob")
            .withAge(34)
            .withAddress(new Address(21, new PostCode("out", "in")))
            .addPhone(Home, new Phone("123", "456456"))
            .addPhone(Work, new Phone("123", "123123"))
            .build();

    private Person dennis = aPerson()
            .withName("dennis")
            .withAge(36)
            .withAddress(new Address(21, new PostCode("out", "in")))
            .build();

    private Person teaCosyPete = aPerson()
            .withName("pete")
            .withAddress(null)
            .build();


    @Test
    public void testBothMatcher() throws Exception {
        Matcher<String> matcher = CombinableMatcher.both(equalTo("abc")).and(equalTo("bc"));

        assertDescription(matcher, "('abc' and 'bc')");
        assertMismatch("abc", matcher, "'bc' was 'abc'");
    }

    @Test
    public void testAnyOfMatcher() throws Exception {
        Matcher<String> matcher = anyOf(equalTo("ab"), equalTo("bc"), equalTo("ac"));

        assertDescription(matcher, "('ab' or 'bc' or 'ac')");
        assertMismatch("abc", matcher, "was 'abc'");
    }

    @Test
    public void testSimpleMatcherSucceedsWhenMatches() {
        Matcher<Person> matcher = is(aPersonThat().hasName("bob"));

        assertDescription(matcher, "is a Person that (has name ('bob'))");
        assertThat(bob, matcher);
    }

    @Test
    public void testSimpleMatcherFailsWhenMismatches() {
        Matcher<Person> matcher = is(aPersonThat().hasName("bob"));

        assertMismatch(dennis, matcher, "name was 'dennis' (expected 'bob')");
    }

    @Test
    public void testSimpleMatcherFailsWhenMatchingWrongType() {
        Matcher<Addressee> matcher = is(anAddresseeThat());

        assertFalse(matcher.matches("a string"));
    }

    @Test
    public void testNestedMatcherSucceedsWhenMatches() {
        Matcher<Person> matcher = is(aPersonThat()
                .hasName("bob")
                .hasAddress(anAddressThat()
                        .hasHouseNumber(21)));

        assertDescription(matcher, "is a Person that (has name ('bob') and has address (an Address that (has houseNumber (<21>))))");
        assertThat(bob, matcher);
    }

    @Test
    public void testNestedMatcherFailsWhenMismatches() {
        Matcher<Person> matcher = is(aPersonThat()
                .hasName("bob")
                .hasAddress(anAddressThat()
                        .hasHouseNumber(99)));

        assertMismatch(bob, matcher, "address.houseNumber was <21> (expected <99>)");
    }

    @Test
    public void testDeepCompositeCustomMatcher() {
        Matcher<Person> matcher =
                is(aPersonThat()
                        .hasName("bob")
                        .hasAddress(anAddressThat()
                                .hasHouseNumber(21)
                                .hasPostCode(aPostCodeThat()
                                        .hasOuter(containsString("y")))));

        assertDescription(matcher, "is a Person that (has name ('bob') and has address " +
                "(an Address that (has houseNumber (<21>) and has postCode " +
                "(a Postcode that (has outer (a string containing 'y'))))))");

        assertMismatch(bob, matcher, "address.postCode.outer was 'out' (expected a string containing 'y')");
    }

    @Test
    public void testDeepCompositeCustomMatcherWithManyMismatches() {
        Matcher<Person> matcher =
                is(aPersonThat()
                        .hasName("obo")
                        .hasAddress(anAddressThat()
                                .hasHouseNumber(22)
                                .hasPostCode(aPostCodeThat()
                                        .hasInner(startsWith("x"))
                                        .hasOuter(containsString("y")))));

        String descriptionOfMismatch = "name was 'bob' (expected 'obo')\n" +
                "     and: address.houseNumber was <21> (expected <22>)\n" +
                "     and: address.postCode.inner was 'in' (expected a string starting with 'x')\n" +
                "     and: address.postCode.outer was 'out' (expected a string containing 'y')";

        assertMismatch(bob, matcher, descriptionOfMismatch);
    }

    @Test
    public void testDeepCompositeCustomMatcherWithNull() {
        Matcher<Person> matcher =
                is(aPersonThat()
                        .hasName("Tea Cosy")
                        .hasAddress(anAddressThat()
                                .hasHouseNumber(21)
                                .hasPostCode(aPostCodeThat()
                                        .hasInner("x"))));

        String descriptionOfMismatch = "name was 'pete' (expected 'Tea Cosy')\n" +
                "     and: address was null";

        assertMismatch(teaCosyPete, matcher, descriptionOfMismatch);
    }

    @Test
    public void testEmptyListMatching() {
        Matcher<Person> matcher =
                is(aPersonThat()
                        .hasPhoneList(IsEmptyCollection.<Phone>empty()));

        Pattern mismatchDescriptionPattern = Pattern.compile("phoneList .* \\(expected an empty collection\\)");

        assertMismatch(bob, matcher, mismatchDescriptionPattern);
    }

    @Test
    public void testListSizeMatching() {
        Matcher<Person> matcher =
                is(aPersonThat()
                        .hasPhoneList(IsCollectionWithSize.<Phone>hasSize(1)));

        String descriptionOfMismatch = "phoneList collection size was <2> (expected a collection with size <1>)";

        assertMismatch(bob, matcher, descriptionOfMismatch);
    }

    @Test
    public void testListContainsMatching() {
        Matcher<Person> matcher =
                is(aPersonThat()
                        .hasPhoneList(IsIterableContainingInOrder.contains(
                                new PhoneMatcher[]{
                                        aPhoneThat().hasCode("123").hasNumber("123123"),
                                        aPhoneThat().hasCode("123").hasNumber("456654")
                                }
                        )));

        String descriptionOfMismatch = "phoneList item 1: number was '456456' (expected '456654') " +
                "(expected iterable containing [" +
                "a Phone that (has code ('123') and has number ('123123')), " +
                "a Phone that (has code ('123') and has number ('456654'))" +
                "])";

        assertMismatch(bob, matcher, descriptionOfMismatch);
    }

    @Test
    public void testCallingMatcherDirectly() {
        Matcher<Person> youngPerson = aPersonThat().hasAge(34);
        Matcher<Person> oldPerson = aPersonThat().hasAge(36);

        assertTrue(youngPerson.matches(bob));
        assertFalse(oldPerson.matches(bob));
    }
}
