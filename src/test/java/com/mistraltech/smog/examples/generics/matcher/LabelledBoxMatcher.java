package com.mistraltech.smog.examples.generics.matcher;

import org.hamcrest.Matcher;

import com.mistraltech.smog.core.MatchAccumulator;
import com.mistraltech.smog.core.PropertyMatcher;
import com.mistraltech.smog.core.ReflectingPropertyMatcher;
import com.mistraltech.smog.examples.model.generics.LabelledBox;

import static org.hamcrest.CoreMatchers.equalTo;

public class LabelledBoxMatcher<P1, P2> extends BoxMatcher<P1, LabelledBoxMatcher<P1, P2>, LabelledBox<P1, P2>>
{
    private static final String MATCHED_OBJECT_DESCRIPTION = "a LabelledBox";

    private PropertyMatcher<P2> labelMatcher = new ReflectingPropertyMatcher<P2>("label", this);

    private LabelledBoxMatcher(final String matchedObjectDescription, final LabelledBox<P1, P2> template)
    {
        super(matchedObjectDescription, template);

        if (template != null)
        {
            hasLabel(template.getLabel());
        }
    }

    public static <T, L> LabelledBoxMatcher<T, L> aLabelledBoxThat()
    {
        return new LabelledBoxMatcher<T, L>(MATCHED_OBJECT_DESCRIPTION, null);
    }

    public static <T, L> LabelledBoxMatcher<T, L> aLabelledBoxLike(final LabelledBox<T, L> template)
    {
        return new LabelledBoxMatcher<T, L>(MATCHED_OBJECT_DESCRIPTION, template);
    }

    public LabelledBoxMatcher<P1, P2> hasLabel(final P2 label)
    {
        return this.hasLabel(equalTo(label));
    }

    public LabelledBoxMatcher<P1, P2> hasLabel(Matcher<? super P2> labelMatcher)
    {
        this.labelMatcher.setMatcher(labelMatcher);
        return this;
    }

    // This override is necessary only to allow CompositePropertyMatcher to determine the type that the matcher expects.
    @Override
    protected void matchesSafely(LabelledBox<P1, P2> item, MatchAccumulator matchAccumulator)
    {
        super.matchesSafely(item, matchAccumulator);
    }
}
