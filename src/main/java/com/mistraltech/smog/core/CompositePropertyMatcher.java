package com.mistraltech.smog.core;

import org.hamcrest.Description;

import java.util.ArrayList;
import java.util.List;

/**
 * A base class for a matcher that composes a list of other related matchers, all of
 * which can be applied to a given matching target.
 * <p>
 * Implements PropertyMatcherRegister, allowing nested PropertyMatchers to be added.
 * <p>
 * Acts as a PathProvider for registered PropertyMatchers.
 *
 * @param <T> type of matchable target object
 */
public class CompositePropertyMatcher<T> extends PathAwareDiagnosingMatcher<T> implements PropertyMatcherRegistry {
    private String matchedObjectDescription;
    private List<PropertyMatcher<?>> propertyMatcherList = new ArrayList<PropertyMatcher<?>>();

    /**
     * Constructor.
     *
     * @param matchedObjectDescription a description of the concrete class that is to be matched. E.g.
     * if this class will be used to match a Widget object, the description should be "a Widget". This is
     * required to generate a meaningful and readable describeTo message.
     */
    protected CompositePropertyMatcher(String matchedObjectDescription) {
        if (matchedObjectDescription == null) {
            throw new IllegalArgumentException("matchedObjectDescription is required but was null");
        }

        this.matchedObjectDescription = matchedObjectDescription;
    }

    /**
     * Register a PropertyMatcher instance. Registered property matchers are used
     * to generate the describeTo text. If a given property matcher does not have an assigned
     * path provider, this instance will be assigned.
     *
     * @param propertyMatcher the PropertyMatcher instances
     */
    @Override
    public void registerPropertyMatcher(PropertyMatcher<?> propertyMatcher) {
        if (propertyMatcher.getPathProvider() == null) {
            propertyMatcher.setPathProvider(this);
        }

        propertyMatcherList.add(propertyMatcher);
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

    @Override
    protected final boolean matchesSafely(T item, Description mismatchDescription) {
        MatchAccumulator matchAccumulator = new MatchAccumulator(mismatchDescription);

        // Give subclasses an opportunity to match PropertyMatchers manually
        matchesSafely(item, matchAccumulator);

        // Any remaining PropertyMatchers that have not been matched manually by subclasses
        // are matched automatically against 'item'.
        // Note this is only likely to be the right thing if the property matchers
        // are able to determine their own property value from the parent object, such
        // as is done by ReflectingPropertyMatcher.
        for (PropertyMatcher<?> propertyMatcher : propertyMatcherList) {
            if (!matchAccumulator.hasBeenApplied(propertyMatcher)) {
                matchAccumulator.matches(propertyMatcher, item);
            }
        }

        return matchAccumulator.result();
    }

    protected void matchesSafely(T item, MatchAccumulator matchAccumulator) {
    }
}
