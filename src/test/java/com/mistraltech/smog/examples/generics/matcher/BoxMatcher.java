package com.mistraltech.smog.examples.generics.matcher;

import com.mistraltech.smog.core.MatchAccumulator;
import com.mistraltech.smog.examples.generics.model.Box;

public class BoxMatcher<P1> extends AbstractBoxMatcher<P1, BoxMatcher<P1>, Box<P1>>
{
    public BoxMatcher(final String matchedObjectDescription, final Box<P1> template)
    {
        super(matchedObjectDescription, template);
    }

    public static <P1> BoxMatcher<P1> aBoxThat()
    {
        return aBoxLike(null);
    }

    public static <P1> BoxMatcher<P1> aBoxLike(final Box<P1> template)
    {
        return new BoxMatcher<P1>("a Box", template);
    }

    // This override is necessary only to allow CompositePropertyMatcher to determine the type that the matcher expects.
    @Override
    protected void matchesSafely(Box<P1> item, MatchAccumulator matchAccumulator)
    {
        super.matchesSafely(item, matchAccumulator);
    }
}
