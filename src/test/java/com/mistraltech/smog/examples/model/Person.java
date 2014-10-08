package com.mistraltech.smog.examples.model;

import java.util.Arrays;
import java.util.List;

public class Person extends Addressee {
    private int age;
    private Phone[] phones;

    public Person(String name, int age, Address address, Phone... phones) {
        super(name, address);
        this.age = age;
        this.phones = phones;
    }

    public int getAge() {
        return age;
    }

    public List<Phone> getPhoneList() {
        return Arrays.asList(phones);
    }

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", phones=" + Arrays.toString(phones) +
                "} " + super.toString();
    }
}
