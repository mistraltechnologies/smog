package com.mistraltech.smog.examples.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Person extends Addressee {
    private int age;
    private Map<PhoneType, Phone> phoneMap;

    public Person(String name, int age, Address address, Map<PhoneType, Phone> phoneMap) {
        super(name, address);
        this.age = age;
        this.phoneMap = phoneMap;
    }

    public int getAge() {
        return age;
    }

    public Phone[] getPhones() {
        return phoneMap.values().toArray(new Phone[phoneMap.size()]);
    }

    public List<Phone> getPhoneList() {
        List<Phone> phoneList = new ArrayList<Phone>(phoneMap.values());
        Collections.sort(phoneList, new PhoneComparator());
        return phoneList;
    }

    public Map<PhoneType, Phone> getPhoneMap() {
        return phoneMap;
    }

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", phoneMap=" + phoneMap +
                "} " + super.toString();
    }
}
