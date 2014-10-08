package com.mistraltech.smog.examples.model;

public class Address {
    private Integer number;
    private PostCode postCode;

    public Address(Integer houseNumber, PostCode postCode) {
        this.number = houseNumber;
        this.postCode = postCode;
    }

    public Integer getHouseNumber() {
        return number;
    }

    public PostCode getPostCode() {
        return postCode;
    }

    @Override
    public String toString() {
        return "Address{" +
                "houseNumber=" + number +
                ", postCode=" + postCode +
                '}';
    }
}
