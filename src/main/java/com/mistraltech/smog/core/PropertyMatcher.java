package com.mistraltech.smog.core;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * A wrapper for a Hamcrest matcher that matches a property of the target object. Knows the name
 * of the property being matched and helps with the mismatch description if the match fails.
 * <p/>
 * For example, if a Person class has an Address property, the PersonMatcher class
 * will declare a PropertyMatcher<Address> field that holds a matcher for Address.
 * <p/>
 * The PropertyMatcher may be empty, in which case the represented property will not be matched.
 *
 * @param <T>
 */
public class PropertyMatcher<T> extends BaseMatcher<T> implements PathProvider {
    /**
     * A contained matcher for matching the property that this PropertyMatcher represents in
     * the target object.
     * <p/>
     * May be null, in which case the property will be ignored.
     */
    private Matcher<? super T> matcher;

    /**
     * The name of the property in the target object that this PropertyMatcher represents
     * and matches against.
     */
    private String propertyName;

    /**
     * A path provider that tells us where the property represented by this PropertyMatcher
     * exists in the object graph.
     * <p/>
     * E.g. if this PropertyMatcher represents the houseNumber property on an Address object, the
     * path might be "person.address" or "company.address".
     */
    private PathProvider pathProvider;

    /**
     * Constructor.
     *
     * @param propertyName name of the attribute that this PropertyMatcher matches against in the target object
     * @param pathProvider provides this PropertyMatcher with its path context. I.e. the property path that leads
     * to the object containing this attribute in the target object graph
     */
    public PropertyMatcher(String propertyName, PathProvider pathProvider) {
        this.propertyName = propertyName;
        this.pathProvider = pathProvider;
    }

    /**
     * Sets the matcher that the property that this instance represents in the target object graph must match.
     * <p/>
     * If matcher is null, this PropertyMatcher will be ignored.
     * <p/>
     * If the matcher is {@link PathAware}, this instance will become its {@link PathProvider}.
     *
     * @param matcher the matcher
     */
    public void setMatcher(Matcher<? super T> matcher) {
        this.matcher = matcher;
        if (matcher instanceof PathAware) {
            ((PathAware) matcher).setPathProvider(this);
        }
    }

    public String getPath() {
        String pathContext = pathProvider.getPath();
        return pathContext + (pathContext.length() > 0 ? "." : "") + propertyName;
    }

    public boolean matches(Object item) {
        return matcher == null || matcher.matches(item);
    }

    public boolean isSpecified() {
        return matcher != null;
    }

    public void describeTo(Description description) {
        description.appendText("has ").appendText(propertyName).appendText(" (");

        if (matcher != null) {
            description.appendDescriptionOf(matcher);
        } else {
            description.appendText("<any>");
        }

        description.appendText(")");
    }

    public void describeMismatch(Object item, Description mismatchDescription) {
        if (matcher instanceof PathAware) {
            // PathAware matchers can take care of their own describeMismatch
            matcher.describeMismatch(item, mismatchDescription);
        } else {
            // Non-PathAware matchers need their mismatch description augmenting with
            // the path of the property being matched and the expected value...
            mismatchDescription
                    .appendText(getPath())
                    .appendText(" ");

            matcher.describeMismatch(item, mismatchDescription);

            mismatchDescription
                    .appendText(" (expected ")
                    .appendDescriptionOf(matcher)
                    .appendText(")");
        }

        // Note: the PathAware/non-PathAware distinction may be artificial.
        // Really we are interested in whether the matcher can fully describe itself in terms
        // of its path within a matched object graph. It may be better to check if the matcher extends
        // PathAwareDiagnosingMatcher.
    }
}
