package com.mistraltech.smog.examples.generics.model;

public class Box<T>
{
    private T contents;

    public Box(T contents)
    {
        this.contents = contents;
    }

    public T getContents()
    {
        return contents;
    }
}
