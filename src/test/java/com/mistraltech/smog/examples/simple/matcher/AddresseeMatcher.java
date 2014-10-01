package com.mistraltech.smog.examples.simple.matcher;

import com.mistraltech.smog.core.CompositePropertyMatcher;
import com.mistraltech.smog.core.MatchAccumulator;
import com.mistraltech.smog.core.PropertyMatcher;
import com.mistraltech.smog.core.ReflectingPropertyMatcher;
import com.mistraltech.smog.examples.simple.model.Address;
import com.mistraltech.smog.examples.simple.model.Addressee;
import org.hamcrest.Matcher;

import static org.hamcrest.CoreMatchers.equalTo;

public abstract class AddresseeMatcher<R, T extends Addressee> extends CompositePropertyMatcher<T> {
    private PropertyMatcher<String> nameMatcher = new PropertyMatcher<String>("name", this);
    private PropertyMatcher<Address> addressMatcher = new ReflectingPropertyMatcher<Address>("address", this);

    protected AddresseeMatcher(String matchedObjectDescription) {
        super(matchedObjectDescription);
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
}
