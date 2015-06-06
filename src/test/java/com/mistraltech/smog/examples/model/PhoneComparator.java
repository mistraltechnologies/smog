package com.mistraltech.smog.examples.model;

import java.util.Comparator;

public class PhoneComparator implements Comparator<Phone> {
    @Override
    public int compare(Phone o1, Phone o2) {
        return (o1.getCode() + o1.getNumber()).compareTo(o2.getCode() + o2.getNumber());
    }
}
