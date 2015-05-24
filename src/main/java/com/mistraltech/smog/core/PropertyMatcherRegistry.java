package com.mistraltech.smog.core;

/**
 * Allows a PropertyMatcher to be added to a register of {@link PropertyMatcher}s.
 */
public interface PropertyMatcherRegistry {
    void registerPropertyMatcher(PropertyMatcher<?> propertyMatcher);
}
