package com.mistraltech.smog.examples.simple.matcher;

import com.mistraltech.smog.core.CompositePropertyMatcher;
import com.mistraltech.smog.core.MatchAccumulator;
import com.mistraltech.smog.core.PropertyMatcher;
import com.mistraltech.smog.examples.simple.model.PostCode;
import org.hamcrest.Matcher;

import static org.hamcrest.CoreMatchers.equalTo;

public class PostCodeMatcher extends CompositePropertyMatcher<PostCode> {
    private PropertyMatcher<String> innerMatcher = new PropertyMatcher<String>("inner", this);
    private PropertyMatcher<String> outerMatcher = new PropertyMatcher<String>("outer", this);

    private PostCodeMatcher() {
        super("a Postcode");
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
    protected void matchesSafely(PostCode item, MatchAccumulator matchAccumulator) {
        matchAccumulator
                .matches(outerMatcher, item.getOuter())
                .matches(innerMatcher, item.getInner());
    }
}
