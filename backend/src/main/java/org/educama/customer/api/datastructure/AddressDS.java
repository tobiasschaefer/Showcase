package org.educama.customer.api.datastructure;

import org.educama.customer.model.Address;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Re-usable data structure used by resources.
 */
public class AddressDS {

    @NotEmpty
    public String street;

    @NotEmpty
    public String streetNo;

    @NotEmpty
    public String zipCode;

    @NotEmpty
    public String city;

    /*
     * For Jackson Parser
     */
    private AddressDS() {
        //empty
    }

    public AddressDS(Address address) {
        this.street = address.getStreet();
        this.streetNo = address.getStreetNo();
        this.zipCode = address.getZipCode();
        this.city = address.getCity();
    }

    public AddressDS(String street, String streetNo, String zipCode, String city) {
        this.street = street;
        this.streetNo = streetNo;
        this.zipCode = zipCode;
        this.city = city;
    }

    public Address toAddress() {
        return new Address(this.street, this.streetNo, this.zipCode, this.city);
    }
}
