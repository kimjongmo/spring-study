package com.spring.jpa.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Address {
    private String address1;
    private String address2;
    private String zipCode;

    public Address(String address1, String address2, String zipCode) {
        this.address1 = address1;
        this.address2 = address2;
        this.zipCode = zipCode;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getZipCode() {
        return zipCode;
    }

    @Override
    public String toString() {
        return "Address{" +
                "address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", zipCode='" + zipCode + '\'' +
                '}';
    }
}
