package com.mistraltech.smog;

import org.hamcrest.StringDescription;

/**
 * A type of Description that substitutes parts of the description matching a regular expression
 * with some other text.
 */
class TextSubstitutingDescription extends StringDescription
{
    final String regex;
    final String replacement;

    public TextSubstitutingDescription(String regex, String replacement)
    {
        this.regex = regex;
        this.replacement = replacement;
    }

    @Override
    public String toString()
    {
        return super.toString().replaceAll(regex, replacement);
    }
}
