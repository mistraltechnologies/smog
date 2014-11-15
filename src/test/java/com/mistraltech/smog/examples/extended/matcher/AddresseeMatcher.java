package com.mistraltech.smog.examples.extended.matcher;

import com.mistraltech.smog.core.CompositePropertyMatcher;
import com.mistraltech.smog.core.MatchAccumulator;
import com.mistraltech.smog.core.PropertyMatcher;
import com.mistraltech.smog.core.ReflectingPropertyMatcher;
import com.mistraltech.smog.examples.model.Address;
import com.mistraltech.smog.examples.model.Addressee;
import org.hamcrest.Matcher;

import static org.hamcrest.CoreMatchers.equalTo;

public class AddresseeMatcher<R extends AddresseeMatcher<R, T>, T extends Addressee> extends CompositePropertyMatcher<T> {
    private static final String MATCHED_OBJECT_DESCRIPTION = "an Addressee";

    private PropertyMatcher<String> nameMatcher = new PropertyMatcher<String>("name", this);
    private PropertyMatcher<Address> addressMatcher = new ReflectingPropertyMatcher<Address>("address", this);

    protected AddresseeMatcher(final String matchedObjectDescription, final T template) {
        super(matchedObjectDescription);
        if (template != null) {
            hasName(template.getName());
            hasAddress(template.getAddress());
        }
    }

    public static AddresseeMatcherType anAddresseeThat() {
        return new AddresseeMatcherType(MATCHED_OBJECT_DESCRIPTION, null);
    }

    public static AddresseeMatcherType anAddresseeLike(final Addressee template) {
        return new AddresseeMatcherType(MATCHED_OBJECT_DESCRIPTION, template);
    }

    @SuppressWarnings("unchecked")
    private R self() {
        return (R) this;
    }

    public R hasName(String name) {
        return this.hasName(equalTo(name));
    }

    public R hasName(Matcher<? super String> nameMatcher) {
        this.nameMatcher.setMatcher(nameMatcher);
        return self();
    }

    public R hasAddress(Address address) {
        return this.hasAddress(equalTo(address));
    }

    public R hasAddress(Matcher<? super Address> addressMatcher) {
        this.addressMatcher.setMatcher(addressMatcher);
        return self();
    }

    protected void matchesSafely(T item, MatchAccumulator matchAccumulator) {
        matchAccumulator.matches(nameMatcher, item.getName());
    }

    public static class AddresseeMatcherType extends AddresseeMatcher<AddresseeMatcherType, Addressee> {
        protected AddresseeMatcherType(String matchedObjectDescription, Addressee template) {
            super(matchedObjectDescription, template);
        }
    }
}
