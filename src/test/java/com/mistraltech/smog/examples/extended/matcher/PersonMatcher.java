package com.mistraltech.smog.examples.extended.matcher;

import java.util.List;

import org.hamcrest.Matcher;

import com.mistraltech.smog.core.MatchAccumulator;
import com.mistraltech.smog.core.PropertyMatcher;
import com.mistraltech.smog.core.ReflectingPropertyMatcher;
import com.mistraltech.smog.examples.model.Person;
import com.mistraltech.smog.examples.model.Phone;

import static org.hamcrest.CoreMatchers.equalTo;

public class PersonMatcher<R, T extends Person> extends AddresseeMatcher<R, T>
{
    private PropertyMatcher<Integer> ageMatcher = new PropertyMatcher<Integer>("age", this);
    private PropertyMatcher<List<Phone>> phoneListMatcher = new ReflectingPropertyMatcher<List<Phone>>("phoneList", this);

    protected PersonMatcher(final String matchedObjectDescription, final T template)
    {
        super(matchedObjectDescription, template);
        if (template != null)
        {
            hasAge(template.getAge());
        }
    }

    private static class TypeBoundPersonMatcher extends PersonMatcher<TypeBoundPersonMatcher, Person>
    {
        protected TypeBoundPersonMatcher(String matchedObjectDescription, Person template)
        {
            super(matchedObjectDescription, template);
        }

        @Override
        protected void matchesSafely(Person item, MatchAccumulator matchAccumulator)
        {
            super.matchesSafely(item, matchAccumulator);
        }
    }

    public static <P1> PersonMatcher<TypeBoundPersonMatcher, Person> aPersonThat()
    {
        return aPersonLike(null);
    }

    public static <P1> PersonMatcher<TypeBoundPersonMatcher, Person> aPersonLike(final Person template)
    {
        return new TypeBoundPersonMatcher("a Person", template);
    }

    @SuppressWarnings("unchecked")
    private R self()
    {
        return (R) this;
    }

    public PersonMatcher hasAge(int age)
    {
        return this.hasAge(equalTo(age));
    }

    public PersonMatcher hasAge(Matcher<? super Integer> ageMatcher)
    {
        this.ageMatcher.setMatcher(ageMatcher);
        return this;
    }

    public PersonMatcher hasPhoneList(Matcher<? super List<? extends Phone>> phoneListMatcher)
    {
        this.phoneListMatcher.setMatcher(phoneListMatcher);
        return this;
    }

    protected void matchesSafely(T item, MatchAccumulator matchAccumulator)
    {
        super.matchesSafely(item, matchAccumulator);
        matchAccumulator.matches(ageMatcher, item.getAge());
    }
}
