package org.educama.customer.api.datastructure;

import org.apache.commons.lang3.RandomStringUtils;
import org.educama.customer.api.resource.SaveCustomerResource;
import org.educama.customer.model.Address;
import org.educama.customer.model.Customer;

import java.util.UUID;

/**
 * Builder to create Customers for model and dtos.
 */
public class CustomerBuilder {
    private UUID uuid = UUID.randomUUID();
    private String name = RandomStringUtils.randomAlphabetic(AddressBuilder.MEDIUM_RANDOM_STRING_LENGTH);
    private Address address = new AddressBuilder().buildModel();

    public CustomerBuilder id(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public CustomerBuilder name(String name) {
        this.name = name;
        return this;
    }

    public CustomerBuilder address(Address address) {
        this.address = address;
        return this;
    }

    public Customer buildModel() {
        return new Customer(uuid, name, address);
    }

    public SaveCustomerResource buildRes() {
        return new SaveCustomerResource(name, new AddressDS(address));
    }
}
