package com.mistraltech.smog.examples.model;

public class PostCode {
    private String outer;
    private String inner;

    public PostCode(String outer, String inner) {
        this.outer = outer;
        this.inner = inner;
    }

    public String getOuter() {
        return outer;
    }

    public String getInner() {
        return inner;
    }

    @Override
    public String toString() {
        return "PostCode{" +
                "outer='" + outer + '\'' +
                ", inner='" + inner + '\'' +
                '}';
    }
}
