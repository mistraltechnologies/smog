package com.mistraltech.smog.example.matcher;

import com.mistraltech.smog.core.CompositePropertyMatcher;
import com.mistraltech.smog.core.PropertyMatcher;
import com.mistraltech.smog.example.model.Address;
import com.mistraltech.smog.example.model.Addressee;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static com.mistraltech.smog.core.MatchAccumulator.matchAccumulator;
import static org.hamcrest.CoreMatchers.equalTo;

public abstract class AddresseeMatcher<R, T extends Addressee> extends CompositePropertyMatcher<T> {
    private PropertyMatcher<String> nameMatcher = new PropertyMatcher<String>("name", this);
    private PropertyMatcher<Address> addressMatcher = new PropertyMatcher<Address>("address", this);

    protected AddresseeMatcher(String matchedObjectDescription) {
        super(matchedObjectDescription);
        addPropertyMatchers(nameMatcher, addressMatcher);
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

    protected boolean matchesSafely(T item, Description mismatchDescription) {
        return matchAccumulator(mismatchDescription)
                .matches(nameMatcher, item.getName())
                .matches(addressMatcher, item.getAddress())
                .result();
    }
}
