package com.mistraltech.smog.example.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Person extends Addressee
{
    private int age;
    private Phone[] phones;

    public Person(String name, int age, Address address, Phone... phones)
    {
        super(name, address);
        this.age = age;
        this.phones = phones;
    }

    public int getAge()
    {
        return age;
    }

    public List<Phone> getPhoneList()
    {
        return Arrays.asList(phones);
    }

    public Set<Phone> getPhoneSet()
    {
        return new HashSet<Phone>(getPhoneList());
    }

    public Phone[] getPhones() {
        return phones;
    }

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", phones=" + Arrays.toString(phones) +
                "} " + super.toString();
    }
}
