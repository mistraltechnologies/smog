package com.mistraltech.smog.examples.arrays;

import com.mistraltech.smog.examples.model.Person;
import com.mistraltech.smog.examples.model.Phone;
import org.hamcrest.Matcher;
import org.hamcrest.collection.IsArrayContaining;
import org.hamcrest.collection.IsArrayContainingInAnyOrder;
import org.hamcrest.collection.IsArrayWithSize;
import org.junit.Test;

import static com.mistraltech.smog.examples.arrays.matcher.PersonMatcher.aPersonThat;
import static com.mistraltech.smog.examples.model.PersonBuilder.aPerson;
import static com.mistraltech.smog.examples.model.PhoneType.Home;
import static com.mistraltech.smog.examples.model.PhoneType.Work;
import static com.mistraltech.smog.examples.simple.matcher.PhoneMatcher.aPhoneThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ArraysTest {
    private final Person bob = aPerson()
            .withName("Bob")
            .addPhone(Home, new Phone("1234", "12345"))
            .addPhone(Work, new Phone("4321", "12345"))
            .build();

    private final Person bill = aPerson()
            .withName("Bill")
            .build();

    @Test
    public void canAssignIsArrayWithSizeMatcher() {
        Matcher<Phone[]> withTwoElements = IsArrayWithSize.arrayWithSize(2);
        assertThat(bob, is(aPersonThat().hasPhones(withTwoElements)));

        Matcher<Phone[]> empty = IsArrayWithSize.emptyArray();
        assertThat(bill, is(aPersonThat().hasPhones(empty)));
    }

    @Test
    public void canAssignIsArrayContainingMatcher() {
        Matcher<Phone[]> m = IsArrayContaining.hasItemInArray(aPhoneThat().hasCode("4321"));
        assertThat(bob, is(aPersonThat().hasPhones(m)));
    }

    @Test
    public void canAssignIsArrayContainingInAnyOrderMatcher() {
        Matcher<Phone[]> m = IsArrayContainingInAnyOrder.arrayContainingInAnyOrder(
                aPhoneThat().hasCode("4321"), aPhoneThat().hasCode("1234"));
        assertThat(bob, is(aPersonThat().hasPhones(m)));
    }
}
