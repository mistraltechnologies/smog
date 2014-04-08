package com.mistraltech.smog.core;

/**
 * Implemented by classes that expect to be provided with some path.
 * <p/>
 * This is useful where neither the path nor the provider of that path is known
 * at object construction.
 */
public interface PathAware {

    /**
     * Assign a path provider.
     *
     * @param pathProvider the path provider
     */
    void setPathProvider(PathProvider pathProvider);
}
