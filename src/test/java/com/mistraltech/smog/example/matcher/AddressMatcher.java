package com.mistraltech.smog.example.matcher;

import com.mistraltech.smog.CompositePropertyMatcher;
import com.mistraltech.smog.PropertyMatcher;
import com.mistraltech.smog.example.model.Address;
import com.mistraltech.smog.example.model.PostCode;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static com.mistraltech.smog.MatchAccumulator.matchAccumulator;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class AddressMatcher extends CompositePropertyMatcher<Address> {
    private PropertyMatcher<Integer> houseNumberMatcher = new PropertyMatcher<Integer>("number", this);
    private PropertyMatcher<PostCode> postCodeMatcher = new PropertyMatcher<PostCode>("postcode", this);

    private AddressMatcher() {
        super("an Address");
        addPropertyMatchers(houseNumberMatcher, postCodeMatcher);
    }

    public static AddressMatcher anAddressThat() {
        return new AddressMatcher();
    }

    public AddressMatcher hasHouseNumber(Integer houseNumber) {
        return this.hasHouseNumber(equalTo(houseNumber));
    }

    public AddressMatcher hasHouseNumber(Matcher<? super Integer> houseNumberMatcher) {
        this.houseNumberMatcher.setMatcher(houseNumberMatcher);
        return this;
    }

    public AddressMatcher hasPostCode(PostCode postCode) {
        this.hasPostCode(is(equalTo(postCode)));
        return this;
    }

    public AddressMatcher hasPostCode(Matcher<? super PostCode> postCodeMatcher) {
        this.postCodeMatcher.setMatcher(postCodeMatcher);
        return this;
    }

    @Override
    protected boolean matchesSafely(Address item, Description mismatchDescription) {
        return matchAccumulator(mismatchDescription)
                .matches(houseNumberMatcher, item.getHouseNumber())
                .matches(postCodeMatcher, item.getPostCode())
                .result();
    }
}
