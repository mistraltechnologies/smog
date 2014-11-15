package com.mistraltech.smog.examples.extended.matcher;

import com.mistraltech.smog.core.MatchAccumulator;
import com.mistraltech.smog.core.PropertyMatcher;
import com.mistraltech.smog.core.ReflectingPropertyMatcher;
import com.mistraltech.smog.examples.model.Person;
import com.mistraltech.smog.examples.model.Phone;
import org.hamcrest.Matcher;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

public class PersonMatcher<R extends PersonMatcher<R, T>, T extends Person> extends AddresseeMatcher<R, T> {
    private static final String MATCHED_OBJECT_DESCRIPTION = "a Person";

    private PropertyMatcher<Integer> ageMatcher = new PropertyMatcher<Integer>("age", this);
    private PropertyMatcher<List<Phone>> phoneListMatcher = new ReflectingPropertyMatcher<List<Phone>>("phoneList", this);

    protected PersonMatcher(final String matchedObjectDescription, final T template) {
        super(matchedObjectDescription, template);
        if (template != null) {
            hasAge(template.getAge());
            if (template.getPhoneList().size() > 0) {
                hasPhoneList(contains(template.getPhoneList().toArray()));
            } else {
                hasPhoneList(empty());
            }
        }
    }

    public static PersonMatcherType aPersonThat() {
        return new PersonMatcherType(MATCHED_OBJECT_DESCRIPTION, null);
    }

    public static PersonMatcherType aPersonLike(final Person template) {
        return new PersonMatcherType(MATCHED_OBJECT_DESCRIPTION, template);
    }

    @SuppressWarnings("unchecked")
    private R self() {
        return (R) this;
    }

    public PersonMatcher hasAge(int age) {
        return this.hasAge(equalTo(age));
    }

    public PersonMatcher hasAge(Matcher<? super Integer> ageMatcher) {
        this.ageMatcher.setMatcher(ageMatcher);
        return this;
    }

    public PersonMatcher hasPhoneList(Matcher<? super List<? extends Phone>> phoneListMatcher) {
        this.phoneListMatcher.setMatcher(phoneListMatcher);
        return this;
    }

    protected void matchesSafely(T item, MatchAccumulator matchAccumulator) {
        super.matchesSafely(item, matchAccumulator);
        matchAccumulator.matches(ageMatcher, item.getAge());
    }

    public static class PersonMatcherType extends PersonMatcher<PersonMatcherType, Person> {
        protected PersonMatcherType(String matchedObjectDescription, Person template) {
            super(matchedObjectDescription, template);
        }
    }
}
