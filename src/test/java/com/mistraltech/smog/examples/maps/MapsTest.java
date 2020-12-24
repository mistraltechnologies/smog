package com.mistraltech.smog.examples.maps;

import com.mistraltech.smog.examples.model.Person;
import com.mistraltech.smog.examples.model.Phone;
import com.mistraltech.smog.examples.model.PhoneType;
import org.hamcrest.Matcher;
import org.hamcrest.collection.IsMapContaining;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.mistraltech.smog.examples.maps.matcher.PersonMatcher.aPersonThat;
import static com.mistraltech.smog.examples.model.PersonBuilder.aPerson;
import static com.mistraltech.smog.examples.model.PhoneType.Home;
import static com.mistraltech.smog.examples.model.PhoneType.Work;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MapsTest {
    private final Phone homePhone = new Phone("1234", "12345");
    private final Phone workPhone = new Phone("4321", "12345");
    private final Person bob = aPerson()
            .withName("Bob")
            .addPhone(Home, homePhone)
            .addPhone(Work, workPhone)
            .build();

    @Test
    public void canAssignIsMapContainingByKeyMatcher() {
        Matcher<Map<? extends PhoneType, ?>> byKey = IsMapContaining.hasKey(PhoneType.Home);
        assertThat(bob, is(aPersonThat().hasPhoneMap(byKey)));
    }

    @Test
    public void canAssignIsMapContainingByValueMatcher() {
        Matcher<Map<?, ? extends Phone>> m = IsMapContaining.hasValue(homePhone);
        assertThat(bob, is(aPersonThat().hasPhoneMap(m)));
    }

    @Test
    public void canAssignIsMapContainingByEntryMatcher() {
        Matcher<Map<? extends PhoneType, ? extends Phone>> m = IsMapContaining.hasEntry(Work, workPhone);
        assertThat(bob, is(aPersonThat().hasPhoneMap(m)));
    }
}
