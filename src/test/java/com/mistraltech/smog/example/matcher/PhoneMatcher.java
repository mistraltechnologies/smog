package com.mistraltech.smog.example.matcher;

import com.mistraltech.smog.CompositePropertyMatcher;
import com.mistraltech.smog.PropertyMatcher;
import com.mistraltech.smog.example.model.Phone;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static com.mistraltech.smog.MatchAccumulator.matchAccumulator;
import static org.hamcrest.CoreMatchers.equalTo;

public class PhoneMatcher extends CompositePropertyMatcher<Phone> {
    private PropertyMatcher<String> codeMatcher = new PropertyMatcher<String>("code", this);
    private PropertyMatcher<String> numberMatcher = new PropertyMatcher<String>("number", this);

    private PhoneMatcher() {
        super("a Phone");
        addPropertyMatchers(codeMatcher, numberMatcher);
    }

    public static PhoneMatcher aPhoneThat() {
        return new PhoneMatcher();
    }

    public PhoneMatcher hasCode(String code) {
        return this.hasCode(equalTo(code));
    }

    public PhoneMatcher hasCode(Matcher<? super String> codeMatcher) {
        this.codeMatcher.setMatcher(codeMatcher);
        return this;
    }

    public PhoneMatcher hasNumber(String number) {
        return this.hasNumber(equalTo(number));
    }

    public PhoneMatcher hasNumber(Matcher<? super String> numberMatcher) {
        this.numberMatcher.setMatcher(numberMatcher);
        return this;
    }

    @Override
    protected boolean matchesSafely(Phone item, Description mismatchDescription) {
        return matchAccumulator(mismatchDescription)
                .matches(codeMatcher, item.getCode())
                .matches(numberMatcher, item.getNumber())
                .result();
    }
}
