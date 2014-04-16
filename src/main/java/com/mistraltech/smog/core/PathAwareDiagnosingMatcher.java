package com.mistraltech.smog.core;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.internal.ReflectiveTypeFinder;

/**
 * A variation on Hamcrest's {@link org.hamcrest.TypeSafeDiagnosingMatcher} that replaces the
 * {@link org.hamcrest.TypeSafeDiagnosingMatcher#matches(Object)} and
 * {@link org.hamcrest.TypeSafeDiagnosingMatcher#describeMismatch(Object, Description)} methods.
 * <p/>
 * A more elegant implementation would extend TypeSafeDiagnosingMatcher, but the
 * methods we want to override are final.
 *
 * @param <T> the type of object we expect to be matching against
 */
abstract class PathAwareDiagnosingMatcher<T> extends BaseMatcher<T> implements PathAware, PathProvider {
    private static final ReflectiveTypeFinder TYPE_FINDER = new ReflectiveTypeFinder("matchesSafely", 2, 0);
    // TODO make logging conjunctive adverb a parameter
    private static final String LOGGING_CONJUNCTIVE_ADVERB = " and ";

    private final Class<?> expectedType;
    private PathProvider pathProvider;

    protected abstract boolean matchesSafely(T item, Description mismatchDescription);

    protected PathAwareDiagnosingMatcher(ReflectiveTypeFinder typeFinder) {
        this.expectedType = typeFinder.findExpectedType(getClass());
    }

    protected PathAwareDiagnosingMatcher() {
        this(TYPE_FINDER);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final boolean matches(Object item) {
        boolean matches = item != null
                && expectedType.isInstance(item)
                && matchesSafely((T) item, Description.NONE);

        if (!matches && pathProvider == null) {
            logMismatch(item);
        }

        return matches;
    }

    protected void logMismatch(Object item) {
        // TODO remove dependency on MatchAccumulator, make mismatch conjunctive adverb a parameter
        Description mismatchDescription = new TextSubstitutingDescription(
                MatchAccumulator.MISMATCH_CONJUNCTIVE_ADVERB, LOGGING_CONJUNCTIVE_ADVERB);
        describeMismatch(item, mismatchDescription);
        writeLog(String.format("%s didn't match - %s", this.getClass().getName(), mismatchDescription));
    }

    protected void writeLog(String text) {
        // No op
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void describeMismatch(Object item, Description mismatchDescription) {
        if (item == null || !expectedType.isInstance(item)) {
            mismatchDescription.appendText(getPath()).appendText(" ");
            super.describeMismatch(item, mismatchDescription);
        } else {
            matchesSafely((T) item, mismatchDescription);
        }
    }

    public String getPath() {
        return pathProvider != null ? pathProvider.getPath() : "";
    }

    public void setPathProvider(PathProvider pathProvider) {
        this.pathProvider = pathProvider;
    }
}
