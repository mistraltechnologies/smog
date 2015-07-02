package com.mistraltech.smog.core;

/**
 * Implemented by classes that are capable of providing a path.
 * <p>
 * No interpretation is put on the meaning of the path, nor its format which
 * is represented simply as a String.
 */
public interface PathProvider {

    /**
     * Gets the path.
     *
     * @return the path as a string
     */
    String getPath();
}
