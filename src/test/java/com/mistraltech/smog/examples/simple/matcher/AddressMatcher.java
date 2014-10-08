package com.mistraltech.smog.examples.simple.matcher;

import com.mistraltech.smog.core.CompositePropertyMatcher;
import com.mistraltech.smog.core.MatchAccumulator;
import com.mistraltech.smog.core.PropertyMatcher;
import com.mistraltech.smog.examples.model.Address;
import com.mistraltech.smog.examples.model.PostCode;
import org.hamcrest.Matcher;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class AddressMatcher extends CompositePropertyMatcher<Address> {
    private PropertyMatcher<Integer> houseNumberMatcher = new PropertyMatcher<Integer>("houseNumber", this);
    private PropertyMatcher<PostCode> postCodeMatcher = new PropertyMatcher<PostCode>("postCode", this);

    private AddressMatcher() {
        super("an Address");
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
    protected void matchesSafely(Address item, MatchAccumulator matchAccumulator) {
        matchAccumulator
                .matches(houseNumberMatcher, item.getHouseNumber())
                .matches(postCodeMatcher, item.getPostCode());
    }
}
