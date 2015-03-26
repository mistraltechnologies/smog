package com.mistraltech.smog.examples.model;

public class Phone {
    String code;
    String number;

    public Phone(String code, String number) {
        assert (number != null);
        this.code = code;
        this.number = number;
    }

    public String getCode() {
        return code;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "code='" + code + '\'' +
                ", number='" + number + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Phone phone = (Phone) o;

        if (code != null ? !code.equals(phone.code) : phone.code != null) {
            return false;
        }
        return number.equals(phone.number);

    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + number.hashCode();
        return result;
    }
}
