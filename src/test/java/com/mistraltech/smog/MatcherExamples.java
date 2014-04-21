package com.mistraltech.smog;

import com.mistraltech.smog.example.model.Address;
import com.mistraltech.smog.example.model.Person;
import com.mistraltech.smog.example.model.Phone;
import com.mistraltech.smog.example.model.PostCode;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.collection.IsCollectionWithSize;
import org.hamcrest.collection.IsEmptyCollection;
import org.hamcrest.core.CombinableMatcher;
import org.junit.Test;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import static com.mistraltech.smog.example.matcher.AddressMatcher.anAddressThat;
import static com.mistraltech.smog.example.matcher.PersonMatcher.aPersonThat;
import static com.mistraltech.smog.example.matcher.PhoneMatcher.aPhoneThat;
import static com.mistraltech.smog.example.matcher.PostCodeMatcher.aPostCodeThat;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MatcherExamples {

    @Test
    public void testBothMatcher() throws Exception {
        Matcher matcher = CombinableMatcher.<String>both(equalTo("abc")).and(equalTo("bc"));

        assertDescription(matcher, "('abc' and 'bc')");
        assertMismatch(matcher, "abc", "'bc' was 'abc'");
    }

    @Test
    public void testAnyOfMatcher() throws Exception {
        Matcher matcher = anyOf(equalTo("ab"), equalTo("bc"), equalTo("ac"));

        assertDescription(matcher, "('ab' or 'bc' or 'ac')");
        assertMismatch(matcher, "abc", "was 'abc'");
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

        Person input = new Person("bob", 36, new Address(21, new PostCode("out", "in")));

        assertDescription(matcher, "is a Person that (has name ('bob') and has address " +
                "(an Address that (has number (<21>) and has postcode " +
                "(a Postcode that (has outer (a string containing 'y'))))))");

        assertMismatch(matcher, input, "address.postcode.outer was 'out' (expected a string containing 'y')");
    }

    @Test
    public void testDeepCompositeCustomMatcherWithManyMismatches() {
        Matcher<Person> matcher =
                is(aPersonThat()
                        .hasName("obo")
                        .hasAddress(anAddressThat()
                                .hasHouseNumber(21)
                                .hasPostCode(aPostCodeThat()
                                        .hasInner(startsWith("x"))
                                        .hasOuter(containsString("y")))));

        Person input = new Person("bob", 36, new Address(22, new PostCode("out", "in")));

        String descriptionOfMismatch = "name was 'bob' (expected 'obo')\n" +
                "     and: address.number was <22> (expected <21>)\n" +
                "     and: address.postcode.outer was 'out' (expected a string containing 'y')\n" +
                "     and: address.postcode.inner was 'in' (expected a string starting with 'x')";

        assertMismatch(matcher, input, descriptionOfMismatch);
    }

    @Test
    public void testDeepCompositeCustomMatcherWithNull() {
        Matcher<Person> matcher =
                is(aPersonThat()
                        .hasName("obo")
                        .hasAddress(anAddressThat()
                                .hasHouseNumber(21)
                                .hasPostCode(aPostCodeThat()
                                        .hasInner("x"))));

        Person input = new Person("bob", 36, null);

        String descriptionOfMismatch = "name was 'bob' (expected 'obo')\n" +
                "     and: address was null";

        assertMismatch(matcher, input, descriptionOfMismatch);
    }

    private void assertDescription(Matcher matcher, String descriptionOfExpected) {
        Description actualDescriptionOfExpected = new StringDescription().appendDescriptionOf(matcher);
        assertEquals(singleToDoubleQuotes(descriptionOfExpected), actualDescriptionOfExpected.toString());
    }

    @Test
    public void testEmptyListMatching() {
        Matcher<Person> matcher =
                is(aPersonThat()
                        .hasPhoneList(IsEmptyCollection.<Phone>empty()));

        Person input = new Person("bob", 36, null, new Phone("123", "456456"), new Phone("123", "123123"));

        Pattern mismatchDescriptionPattern = Pattern.compile("phoneList .* \\(expected an empty collection\\)");

        assertMismatch(matcher, input, mismatchDescriptionPattern);
    }

    @Test
    public void testListSizeMatching() {
        Matcher<Person> matcher =
                is(aPersonThat()
                        .hasPhoneList(IsCollectionWithSize.<Phone>hasSize(1)));

        Person input = new Person("bob", 36, null, new Phone("123", "456456"), new Phone("123", "123123"));

        String descriptionOfMismatch = "phoneList collection size was <2> (expected a collection with size <1>)";

        assertMismatch(matcher, input, descriptionOfMismatch);
    }

    @Test
    public void testListContainsMatching() {
        Matcher<Person> matcher =
                is(aPersonThat()
                        .hasPhoneList(contains(
                                aPhoneThat().hasCode("123").hasNumber("456456"),
                                aPhoneThat().hasCode("123").hasNumber("123321"))));

        Person input = new Person("bob", 36, null, new Phone("123", "456456"), new Phone("123", "123123"));

        String descriptionOfMismatch = "phoneList item 1: number was '123123' (expected '123321') " +
                "(expected iterable containing [" +
                "a Phone that (has code ('123') and has number ('456456')), " +
                "a Phone that (has code ('123') and has number ('123321'))" +
                "])";

        assertMismatch(matcher, input, descriptionOfMismatch);
    }

    private <T> void assertMismatch(Matcher<T> matcher, T input, String descriptionOfMismatch) {
        assertFalse(matcher.matches(input));

        Description actualDescriptionOfMismatch = new StringDescription();
        matcher.describeMismatch(input, actualDescriptionOfMismatch);
        assertEquals(singleToDoubleQuotes(descriptionOfMismatch), actualDescriptionOfMismatch.toString());
    }

    private <T> void assertMismatch(Matcher<T> matcher, T input, Pattern descriptionOfMismatch) {
        assertFalse(matcher.matches(input));

        Description actualDescriptionOfMismatch = new StringDescription();
        matcher.describeMismatch(input, actualDescriptionOfMismatch);
        String actualDescriptionText = actualDescriptionOfMismatch.toString();

        boolean result = descriptionOfMismatch.matcher(actualDescriptionText).matches();

        assertTrue("matching: " + descriptionOfMismatch.pattern() + " against: " + actualDescriptionText, result);
    }

    private String singleToDoubleQuotes(String text) {
        return text.replace('\'', '"');
    }
}
