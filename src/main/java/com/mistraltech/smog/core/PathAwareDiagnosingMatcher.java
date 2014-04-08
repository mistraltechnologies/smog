package com.mistraltech.smog.core;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.internal.ReflectiveTypeFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A variation on Hamcrest's {@link org.hamcrest.TypeSafeDiagnosingMatcher} that replaces the
 * {@link org.hamcrest.TypeSafeDiagnosingMatcher#matches(Object)} and
 * {@link org.hamcrest.TypeSafeDiagnosingMatcher#describeMismatch(Object, Description)} methods.
 * <p/>
 * It would have been preferable to extend TypeSafeDiagnosingMatcher, but unfortunately these
 * methods are marked as final.
 * <p/>
 * That is not a criticism! TypeSafeDiagnosingMatcher was cleverly conceived and implemented and the concepts
 * it introduced form the foundation of this library.
 * <p/>
 * TODO - take the dependency on slf4j out
 *
 * @param <T> the type of object we expect to be matching against
 */
abstract class PathAwareDiagnosingMatcher<T> extends BaseMatcher<T> implements PathAware, PathProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(PathAwareDiagnosingMatcher.class);
    private static final ReflectiveTypeFinder TYPE_FINDER = new ReflectiveTypeFinder("matchesSafely", 2, 0);

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
                && matchesSafely((T) item, new Description.NullDescription());

        // Logging - useful for diagnosing mismatches in mock expectations
        if (!matches && pathProvider == null) {
            Description mismatchDescription = new TextSubstitutingDescription(MatchAccumulator.MISMATCH_JOINING_STRING, " and ");
            describeMismatch(item, mismatchDescription);
            LOGGER.debug(String.format("%s didn't match - %s", this.getClass().getName(), mismatchDescription));
        }

        return matches;
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
