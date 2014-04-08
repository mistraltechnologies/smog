package com.mistraltech.smog.example.matcher;

import com.mistraltech.smog.core.CompositePropertyMatcher;
import com.mistraltech.smog.core.PropertyMatcher;
import com.mistraltech.smog.example.model.PostCode;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static com.mistraltech.smog.core.MatchAccumulator.matchAccumulator;
import static org.hamcrest.CoreMatchers.equalTo;

public class PostCodeMatcher extends CompositePropertyMatcher<PostCode> {
    private PropertyMatcher<String> innerMatcher = new PropertyMatcher<String>("inner", this);
    private PropertyMatcher<String> outerMatcher = new PropertyMatcher<String>("outer", this);

    private PostCodeMatcher() {
        super("a Postcode");
        addPropertyMatchers(outerMatcher, innerMatcher);
    }

    public static PostCodeMatcher aPostCodeThat() {
        return new PostCodeMatcher();
    }

    public PostCodeMatcher hasInner(String inner) {
        return this.hasInner(equalTo(inner));
    }

    public PostCodeMatcher hasInner(Matcher<? super String> innerMatcher) {
        this.innerMatcher.setMatcher(innerMatcher);
        return this;
    }

    public PostCodeMatcher hasOuter(String outer) {
        return this.hasOuter(equalTo(outer));
    }

    public PostCodeMatcher hasOuter(Matcher<? super String> outerMatcher) {
        this.outerMatcher.setMatcher(outerMatcher);
        return this;
    }

    @Override
    protected boolean matchesSafely(com.mistraltech.smog.example.model.PostCode item, Description mismatchDescription) {
        return matchAccumulator(mismatchDescription)
                .matches(outerMatcher, item.getOuter())
                .matches(innerMatcher, item.getInner())
                .result();
    }
}
