package com.mistraltech.smog.examples.generics;

import com.mistraltech.smog.examples.generics.matcher.BoxMatcher;
import com.mistraltech.smog.examples.generics.matcher.LabelledBoxMatcher;
import com.mistraltech.smog.examples.model.generics.Box;
import com.mistraltech.smog.examples.model.generics.LabelledBox;
import org.hamcrest.Matcher;
import org.junit.Test;

import java.util.regex.Pattern;

import static com.mistraltech.smog.examples.utils.MatcherTestUtils.assertDescription;
import static com.mistraltech.smog.examples.utils.MatcherTestUtils.assertMismatch;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class GenericsMatcherExamplesTest {
    @Test
    public void testBoxMatcherSucceedsWhenMatches() {
        Matcher<Box<Integer>> matcher = is(BoxMatcher.<Integer>aBoxThat().hasContents(5));

        Box<Integer> input = new Box<Integer>(5);

        assertDescription(matcher, "is a Box that (has contents (<5>))");
        assertThat(input, matcher);
    }

    @Test
    public void testBoxMatcherFailsWhenMismatches() {
        Matcher<Box<Integer>> matcher = is(BoxMatcher.<Integer>aBoxThat().hasContents(6));

        Box<Integer> input = new Box<Integer>(5);

        assertMismatch(input, matcher, "contents was <5> (expected <6>)");
    }

    @Test
    public void testBoxMatcherFailsForIncorrectTypeParam() {
        Matcher<Box<Integer>> matcher = is(BoxMatcher.<Integer>aBoxThat().hasContents(5));

        Box<String> input = new Box<String>("5");

        assertMismatch(input, matcher, "contents was '5' (expected <5>)");
    }

    @Test
    public void testBoxMatcherFailsForIncorrectInstanceType() {
        Matcher<Box<Integer>> matcher = is(BoxMatcher.<Integer>aBoxThat());

        assertMismatch("input", matcher, " was 'input'");
    }

    @Test
    public void testLabelledBoxMatcherSucceedsWhenMatches() {
        Matcher<LabelledBox<Integer, String>> matcher = is(LabelledBoxMatcher.<Integer, String>aLabelledBoxThat().hasContents(42).hasLabel("Meaning Of Life"));

        LabelledBox<Integer, String> input = new LabelledBox<Integer, String>(42, "Meaning Of Life");

        assertDescription(matcher, "is a LabelledBox that (has contents (<42>) and has label ('Meaning Of Life'))");
        assertThat(input, matcher);
    }

    @Test
    public void testLabelledBoxMatcherFailsWhenMismatches() {
        Matcher<LabelledBox<Integer, String>> matcher = is(LabelledBoxMatcher.<Integer, String>aLabelledBoxThat().hasContents(42).hasLabel("My Age"));

        LabelledBox<Integer, String> input = new LabelledBox<Integer, String>(5, "Meaning Of Life");

        assertMismatch(input, matcher, "contents was <5> (expected <42>)\n     and: label was 'Meaning Of Life' (expected 'My Age')");
    }

    @Test
    public void testLabelledBoxMatcherFailsForIncorrectTypeParam() {
        Matcher<LabelledBox<Integer, String>> matcher = is(LabelledBoxMatcher.<Integer, String>aLabelledBoxThat().hasContents(5).hasLabel("Meaning Of Life"));

        LabelledBox<Integer, Integer> input = new LabelledBox<Integer, Integer>(5, 42);

        assertMismatch(input, matcher, "label was <42> (expected 'Meaning Of Life')");
    }

    @Test
    public void testLabelledBoxMatcherFailsForIncorrectInstanceType() {
        Matcher<LabelledBox<Integer, String>> matcher = is(LabelledBoxMatcher.<Integer, String>aLabelledBoxThat());

        Box<Integer> input = new Box<Integer>(5);

        assertMismatch(input, matcher, Pattern.compile(" was <.*\\.Box@.*>"));
    }
}
