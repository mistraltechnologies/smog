package com.mistraltech.smog.example.matcher;

import com.mistraltech.smog.PropertyMatcher;
import com.mistraltech.smog.example.model.Person;
import com.mistraltech.smog.example.model.Phone;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.List;

import static com.mistraltech.smog.MatchAccumulator.matchAccumulator;
import static org.hamcrest.CoreMatchers.equalTo;

public class PersonMatcher extends AddresseeMatcher<PersonMatcher, Person> {
    private PropertyMatcher<Integer> ageMatcher = new PropertyMatcher<Integer>("age", this);
    private PropertyMatcher<List<Phone>> phoneListMatcher = new PropertyMatcher<List<Phone>>("phoneList", this);

    private PersonMatcher() {
        super("a Person");
        addPropertyMatchers(ageMatcher);
        addPropertyMatchers(phoneListMatcher);
    }

    public static PersonMatcher aPersonThat() {
        return new PersonMatcher();
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

    protected boolean matchesSafely(Person item, Description mismatchDescription) {
        return matchAccumulator(mismatchDescription, super.matchesSafely(item, mismatchDescription))
                .matches(ageMatcher, item.getAge())
                .matches(phoneListMatcher, item.getPhoneList())
                .result();
    }
}
