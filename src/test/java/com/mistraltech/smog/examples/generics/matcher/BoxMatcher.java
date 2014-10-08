package com.mistraltech.smog.examples.generics.matcher;

import com.mistraltech.smog.core.CompositePropertyMatcher;
import com.mistraltech.smog.core.MatchAccumulator;
import com.mistraltech.smog.core.ReflectingPropertyMatcher;
import com.mistraltech.smog.core.PropertyMatcher;
import com.mistraltech.smog.examples.model.generics.Box;

import org.hamcrest.Matcher;

import static org.hamcrest.CoreMatchers.equalTo;

public class BoxMatcher<P1, R, T extends Box<P1>> extends CompositePropertyMatcher<T>
{
    private PropertyMatcher<P1> contentsMatcher = new ReflectingPropertyMatcher<P1>("contents", this);

    protected BoxMatcher(final String matchedObjectDescription, final T template)
    {
        super(matchedObjectDescription);
        if (template != null)
        {
            hasContents(template.getContents());
        }
    }

    private static class TypeBoundBoxMatcher<P1> extends BoxMatcher<P1, TypeBoundBoxMatcher<P1>, Box<P1>>
    {
        protected TypeBoundBoxMatcher(String matchedObjectDescription, Box<P1> template)
        {
            super(matchedObjectDescription, template);
        }

        @Override
        protected void matchesSafely(Box<P1> item, MatchAccumulator matchAccumulator)
        {
            super.matchesSafely(item, matchAccumulator);
        }
    }

    public static <P1> BoxMatcher<P1, TypeBoundBoxMatcher<P1>, Box<P1>> aBoxThat()
    {
        return aBoxLike(null);
    }

    public static <P1> BoxMatcher<P1, TypeBoundBoxMatcher<P1>, Box<P1>> aBoxLike(final Box<P1> template)
    {
        return new TypeBoundBoxMatcher<P1>("a Box", template);
    }

    @SuppressWarnings("unchecked")
    private R self()
    {
        return (R) this;
    }

    public R hasContents(final P1 contents)
    {
        return this.hasContents(equalTo(contents));
    }

    public R hasContents(Matcher<? super P1> contentsMatcher)
    {
        this.contentsMatcher.setMatcher(contentsMatcher);
        return self();
    }
}
