package com.mistraltech.smog.examples.model;

public class Addressee {
    private String name;
    private Address address;

    public Addressee(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "Addressee{" +
                "name='" + name + '\'' +
                ", address=" + address +
                '}';
    }
}
