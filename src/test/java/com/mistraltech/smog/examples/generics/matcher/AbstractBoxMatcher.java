package com.mistraltech.smog.examples.generics.matcher;

import com.mistraltech.smog.core.CompositePropertyMatcher;
import com.mistraltech.smog.core.ReflectingPropertyMatcher;
import com.mistraltech.smog.core.PropertyMatcher;
import com.mistraltech.smog.examples.generics.model.Box;

import org.hamcrest.Matcher;

import static org.hamcrest.CoreMatchers.equalTo;

public abstract class AbstractBoxMatcher<P1, R, T extends Box<P1>> extends CompositePropertyMatcher<T>
{
    private PropertyMatcher<P1> contentsMatcher = new ReflectingPropertyMatcher<P1>("contents", this);

    protected AbstractBoxMatcher(final String matchedObjectDescription, final T template)
    {
        super(matchedObjectDescription);
        if (template != null)
        {
            hasContents(template.getContents());
        }
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
