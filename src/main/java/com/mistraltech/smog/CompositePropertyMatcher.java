package com.mistraltech.smog;

import org.hamcrest.Description;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A base class for a matcher that composes a list of other related matchers, all of
 * which can be applied to a given matching target.
 *
 * @param <T> type of matchable target object
 */
public abstract class CompositePropertyMatcher<T> extends PathAwareDiagnosingMatcher<T> {
    private String matchedObjectDescription;
    private List<PropertyMatcher> propertyMatcherList = new ArrayList<PropertyMatcher>();

    /**
     * Constructor.
     *
     * @param matchedObjectDescription a description of the concrete class that is to be matched. E.g.
     * if this class will be used to match a Widget object, the description should be "a Widget". This is
     * required to generate a meaningful and readable describeTo message.
     */
    protected CompositePropertyMatcher(String matchedObjectDescription) {
        this.matchedObjectDescription = matchedObjectDescription;
    }

    /**
     * Register one or more PropertyMatcher instances. Registered property matchers are used
     * to generate the describeTo text.
     *
     * @param matchers the PropertyMatcher instances
     */
    protected void addPropertyMatchers(PropertyMatcher... matchers) {
        Collections.addAll(propertyMatcherList, matchers);
    }

    public final void describeTo(Description description) {
        description.appendText(matchedObjectDescription).appendText(" that (");
        boolean first = true;

        for (PropertyMatcher<?> matcher : propertyMatcherList) {
            if (matcher.isSpecified()) {
                if (first) {
                    first = false;
                } else {
                    description.appendText(" and ");
                }

                matcher.describeTo(description);
            }
        }

        description.appendText(")");
    }
}
