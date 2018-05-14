package org.educama.customer.api.datastructure;

import org.apache.commons.lang3.RandomStringUtils;
import org.educama.customer.model.Address;

/**
 * Builder to create Addresses for model and dtos.
 */
public class AddressBuilder {
    public static final int MEDIUM_RANDOM_STRING_LENGTH = 16;
    public static final int SHORT_RANDOM_STRING_LENGTH = 8;
    private String street = RandomStringUtils.randomAlphanumeric(MEDIUM_RANDOM_STRING_LENGTH);
    private String streetNo = RandomStringUtils.randomAlphanumeric(SHORT_RANDOM_STRING_LENGTH);
    private String zipCode = RandomStringUtils.randomAlphanumeric(SHORT_RANDOM_STRING_LENGTH);
    private String city = RandomStringUtils.randomAlphabetic(MEDIUM_RANDOM_STRING_LENGTH);

    public AddressBuilder street(String street) {
        this.street = street;
        return this;
    }

    public AddressBuilder streetNo(String streetNo) {
        this.streetNo = streetNo;
        return this;
    }

    public AddressBuilder zipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public AddressBuilder city(String city) {
        this.city = city;
        return this;
    }

    public AddressDS buildRes() {
        return new AddressDS(street, streetNo, zipCode, city);
    }

    public Address buildModel() {
        return new Address(street, streetNo, zipCode, city);
    }
}
