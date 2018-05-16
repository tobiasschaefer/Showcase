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

    public String getStreet() {
        return street;
    }

    public String getStreetNo() {
        return streetNo;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCity() {
        return city;
    }
}
