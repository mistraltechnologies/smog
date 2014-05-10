package com.mistraltech.smog.core;

/**
 * A simple register of {@link PropertyMatcher}s. On construction, a PropertyMatcher can optionally register
 * itself with a PropertyMatcherRegistry.
 */
public interface PropertyMatcherRegistry {
    void registerPropertyMatcher(PropertyMatcher<?> propertyMatcher);
}
