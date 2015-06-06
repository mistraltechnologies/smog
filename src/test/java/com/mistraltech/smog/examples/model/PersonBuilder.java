package com.mistraltech.smog.examples.model;

import java.util.HashMap;
import java.util.Map;

public class PersonBuilder {
    private String name = "Bob";
    private int age = 42;
    private Address address = new Address(21, new PostCode("out", "in"));
    private Map<PhoneType, Phone> phoneMap = new HashMap<PhoneType, Phone>();

    private PersonBuilder() {
    }

    public static PersonBuilder aPerson() {
        return new PersonBuilder();
    }

    public PersonBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public PersonBuilder withAge(int age) {
        this.age = age;
        return this;
    }

    public PersonBuilder withAddress(Address address) {
        this.address = address;
        return this;
    }

    public PersonBuilder addPhone(PhoneType type, Phone phone) {
        phoneMap.put(type, phone);
        return this;
    }

    public Person build() {
        return new Person(name, age, address, phoneMap);
    }
}
