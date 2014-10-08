package com.mistraltech.smog.examples.extended.matcher;

import org.hamcrest.Matcher;

import com.mistraltech.smog.core.CompositePropertyMatcher;
import com.mistraltech.smog.core.MatchAccumulator;
import com.mistraltech.smog.core.PropertyMatcher;
import com.mistraltech.smog.core.ReflectingPropertyMatcher;
import com.mistraltech.smog.examples.model.Address;
import com.mistraltech.smog.examples.model.Addressee;

import static org.hamcrest.CoreMatchers.equalTo;

public class AddresseeMatcher<R, T extends Addressee> extends CompositePropertyMatcher<T>
{
    private PropertyMatcher<String> nameMatcher = new PropertyMatcher<String>("name", this);
    private PropertyMatcher<Address> addressMatcher = new ReflectingPropertyMatcher<Address>("address", this);

    protected AddresseeMatcher(final String matchedObjectDescription, final T template)
    {
        super(matchedObjectDescription);
        if (template != null)
        {
            hasName(template.getName());
            hasAddress(template.getAddress());
        }
    }

    private static class TypeBoundAddresseeMatcher extends AddresseeMatcher<TypeBoundAddresseeMatcher, Addressee>
    {
        protected TypeBoundAddresseeMatcher(String matchedObjectDescription, Addressee template)
        {
            super(matchedObjectDescription, template);
        }

        @Override
        protected void matchesSafely(Addressee item, MatchAccumulator matchAccumulator)
        {
            super.matchesSafely(item, matchAccumulator);
        }
    }

    public static <P1> AddresseeMatcher<TypeBoundAddresseeMatcher, Addressee> anAddresseeThat()
    {
        return anAddresseeLike(null);
    }

    public static <P1> AddresseeMatcher<TypeBoundAddresseeMatcher, Addressee> anAddresseeLike(final Addressee template)
    {
        return new TypeBoundAddresseeMatcher("an Addressee", template);
    }

    @SuppressWarnings("unchecked")
    private R self()
    {
        return (R) this;
    }

    public R hasName(String name)
    {
        return this.hasName(equalTo(name));
    }

    public R hasName(Matcher<? super String> nameMatcher)
    {
        this.nameMatcher.setMatcher(nameMatcher);
        return self();
    }

    public R hasAddress(Address address)
    {
        return this.hasAddress(equalTo(address));
    }

    public R hasAddress(Matcher<? super Address> addressMatcher)
    {
        this.addressMatcher.setMatcher(addressMatcher);
        return self();
    }

    protected void matchesSafely(T item, MatchAccumulator matchAccumulator)
    {
        matchAccumulator.matches(nameMatcher, item.getName());
    }
}
