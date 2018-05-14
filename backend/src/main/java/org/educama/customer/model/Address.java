package org.educama.customer.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Embeddable;

/**
 * This represents the address entity used for database persistence.
 */
@Embeddable
public class Address {
    @NotEmpty
    private String street;

    @NotEmpty
    private String streetNo;

    @NotEmpty
    private String zipCode;

    @NotEmpty
    private String city;

    /**
     * Constructor for JPA.
     */
    protected Address() {
        //empty
    }

    public Address(String street, String streetNo, String zipCode, String city) {
        this.street = street;
        this.streetNo = streetNo;
        this.zipCode = zipCode;
        this.city = city;
    }

    public String street() {
        return street;
    }

    public String streetNo() {
        return streetNo;
    }

    public String zipCode() {
        return zipCode;
    }

    public String city() {
        return city;
    }
}
