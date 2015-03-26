package com.mistraltech.smog.examples.utils;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class MatcherTestUtils {
    private MatcherTestUtils() {
    }

    public static void assertMismatch(Object input, Matcher<?> matcher, String descriptionOfMismatch) {
        assertFalse("Expected mismatch", matcher.matches(input));

        Description actualDescriptionOfMismatch = new StringDescription();
        matcher.describeMismatch(input, actualDescriptionOfMismatch);
        assertEquals(singleToDoubleQuotes(descriptionOfMismatch), actualDescriptionOfMismatch.toString());
    }

    public static void assertMismatch(Object input, Matcher<?> matcher, Pattern descriptionOfMismatch) {
        assertFalse(matcher.matches(input));

        Description actualDescriptionOfMismatch = new StringDescription();
        matcher.describeMismatch(input, actualDescriptionOfMismatch);
        String actualDescriptionText = actualDescriptionOfMismatch.toString();

        boolean result = descriptionOfMismatch.matcher(actualDescriptionText).matches();

        assertTrue("matching: " + descriptionOfMismatch.pattern() + " against: " + actualDescriptionText, result);
    }

    public static void assertDescription(Matcher<?> matcher, String descriptionOfExpected) {
        Description actualDescriptionOfExpected = new StringDescription().appendDescriptionOf(matcher);
        assertEquals(singleToDoubleQuotes(descriptionOfExpected), actualDescriptionOfExpected.toString());
    }


    private static String singleToDoubleQuotes(String text) {
        return text.replace('\'', '"');
    }
}
