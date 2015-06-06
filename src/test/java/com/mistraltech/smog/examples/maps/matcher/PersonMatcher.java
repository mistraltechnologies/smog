package com.mistraltech.smog.examples.maps.matcher;

import com.mistraltech.smog.core.CompositePropertyMatcher;
import com.mistraltech.smog.core.MatchAccumulator;
import com.mistraltech.smog.core.PropertyMatcher;
import com.mistraltech.smog.core.ReflectingPropertyMatcher;
import com.mistraltech.smog.core.annotation.Matches;
import com.mistraltech.smog.examples.model.Address;
import com.mistraltech.smog.examples.model.Person;
import com.mistraltech.smog.examples.model.Phone;
import com.mistraltech.smog.examples.model.PhoneType;
import org.hamcrest.Matcher;

import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;

@Matches(Person.class)
public final class PersonMatcher extends CompositePropertyMatcher<Person> {
    private static final String MATCHED_OBJECT_DESCRIPTION = "a Person";
    private final PropertyMatcher<Address> addressMatcher = new ReflectingPropertyMatcher<Address>("address", this);
    private final PropertyMatcher<String> nameMatcher = new ReflectingPropertyMatcher<String>("name", this);
    private final PropertyMatcher<Map<PhoneType, Phone>> phoneMapMatcher =
            new ReflectingPropertyMatcher<Map<PhoneType, Phone>>("phoneMap", this);
    private final PropertyMatcher<Integer> ageMatcher = new ReflectingPropertyMatcher<Integer>("age", this);

    private PersonMatcher(final String matchedObjectDescription, final Person template) {
        super(matchedObjectDescription);
        if (template != null) {
            hasAddress(template.getAddress());
            hasName(template.getName());
            hasPhoneMap(template.getPhoneMap());
            hasAge(template.getAge());
        }
    }

    public static PersonMatcher aPersonThat() {
        return new PersonMatcher(MATCHED_OBJECT_DESCRIPTION, null);
    }

    public static PersonMatcher aPersonLike(final Person template) {
        return new PersonMatcher(MATCHED_OBJECT_DESCRIPTION, template);
    }

    public PersonMatcher hasAddress(final Address address) {
        return hasAddress(equalTo(address));
    }

    public PersonMatcher hasAddress(final Matcher<? super Address> addressMatcher) {
        this.addressMatcher.setMatcher(addressMatcher);
        return this;
    }

    public PersonMatcher hasName(final String name) {
        return hasName(equalTo(name));
    }

    public PersonMatcher hasName(final Matcher<? super String> nameMatcher) {
        this.nameMatcher.setMatcher(nameMatcher);
        return this;
    }

    public PersonMatcher hasPhoneMap(final Map<PhoneType, Phone> phoneMap) {
        return hasPhoneMap(equalTo(phoneMap));
    }

    public PersonMatcher hasPhoneMap(final Matcher<? super Map<PhoneType, Phone>> phoneMapMatcher) {
        this.phoneMapMatcher.setMatcher(phoneMapMatcher);
        return this;
    }

    public PersonMatcher hasAge(final int age) {
        return hasAge(equalTo(age));
    }

    public PersonMatcher hasAge(final Matcher<? super Integer> ageMatcher) {
        this.ageMatcher.setMatcher(ageMatcher);
        return this;
    }

    @Override
    protected void matchesSafely(final Person item, final MatchAccumulator matchAccumulator) {
        super.matchesSafely(item, matchAccumulator);
    }
}
