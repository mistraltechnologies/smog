package com.mistraltech.smog.examples.collections;


import com.mistraltech.smog.examples.model.Person;
import com.mistraltech.smog.examples.model.Phone;
import org.hamcrest.Matcher;
import org.hamcrest.collection.IsEmptyCollection;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsInstanceOf;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.mistraltech.smog.examples.collections.matcher.PersonMatcher.aPersonThat;
import static com.mistraltech.smog.examples.model.PersonBuilder.aPerson;
import static com.mistraltech.smog.examples.model.PhoneType.Home;
import static com.mistraltech.smog.examples.model.PhoneType.Work;
import static com.mistraltech.smog.examples.simple.matcher.PhoneMatcher.aPhoneThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.core.IsIterableContaining.hasItem;
import static org.hamcrest.core.IsIterableContaining.hasItems;

public class CollectionsTest {

    private final Person bob = aPerson()
            .withName("Bob")
            .addPhone(Home, new Phone("1234", "12345"))
            .addPhone(Work, new Phone("4321", "12345"))
            .build();

    private final Person bill = aPerson()
            .withName("Bill")
            .build();


    @Test
    public void canAssignIsEmptyCollectionMatcher() {
        Matcher<Collection<? extends Phone>> m = IsEmptyCollection.empty();
        assertThat(bill, is(aPersonThat().hasPhoneList(m)));
        assertThat(bob, is(not(aPersonThat().hasPhoneList(m))));
    }

    @Test
    public void canAssignIsEmptyCollectionOfMatcher() {
        Matcher<Collection<Phone>> m = IsEmptyCollection.emptyCollectionOf(Phone.class);
        assertThat(bob, is(not(aPersonThat().hasPhoneList(m))));
    }

    @Test
    public void canAssignEveryMatcher() {
        Matcher<Iterable<? extends Phone>> m = everyItem(aPhoneThat());
        assertThat(bob, is(aPersonThat().hasPhoneList(m)));
    }

    @Test
    public void canAssignIsCollectionContainingItemMatcher() {
        Matcher<Iterable<? super Phone>> m = hasItem(aPhoneThat().hasCode("1234"));
        assertThat(bob, is(aPersonThat().hasPhoneList(m)));
    }

    @Test
    public void canAssignIsCollectionContainingItemsMatcher() {
        Matcher<Iterable<Phone>> m = hasItems(aPhoneThat().hasCode("1234"), aPhoneThat().hasCode("4321"));
        assertThat(bob, is(aPersonThat().hasPhoneList(m)));
    }

    @Test
    public void canAssignIsEqualMatcher() {
        Phone[] phones = new Phone[]{
                new Phone("1234", "12345"),
                new Phone("4321", "12345")
        };

        Matcher<List<Phone>> m = IsEqual.equalTo(Arrays.asList(phones));
        assertThat(bob, is(aPersonThat().hasPhoneList(m)));
    }

    @Test
    public void canAssignIsInstanceOfMatcher() {
        Matcher<Object> m = IsInstanceOf.instanceOf(List.class);
        assertThat(bob, is(aPersonThat().hasPhoneList(m)));
    }

    @Test
    public void canAssignIsIterableContainingMatcher() {
        Matcher<Iterable<? extends Phone>> m = IsIterableContainingInOrder.contains(
                aPhoneThat().hasCode("1234"), aPhoneThat().hasCode("4321"));
        assertThat(bob, is(aPersonThat().hasPhoneList(m)));
    }

    @Test
    public void canAssignIsIterableContainingInAnyOrderMatcher() {
        Matcher<Iterable<? extends Phone>> m = IsIterableContainingInAnyOrder.containsInAnyOrder(
                aPhoneThat().hasCode("4321"), aPhoneThat().hasCode("1234"));
        assertThat(bob, is(aPersonThat().hasPhoneList(m)));
    }
}
