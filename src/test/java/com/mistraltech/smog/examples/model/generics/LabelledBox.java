package com.mistraltech.smog.examples.model.generics;


public class LabelledBox<T, L> extends Box<T> {
    private L label;

    public LabelledBox(T contents, L label) {
        super(contents);
        this.label = label;
    }

    public L getLabel() {
        return label;
    }
}
