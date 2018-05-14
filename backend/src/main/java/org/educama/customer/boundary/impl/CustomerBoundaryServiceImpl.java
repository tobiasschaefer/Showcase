package org.educama.customer.boundary.impl;

import org.educama.common.exceptions.ResourceNotFoundException;
import org.educama.customer.boundary.CustomerBoundaryService;
import org.educama.customer.model.Address;
import org.educama.customer.model.Customer;
import org.educama.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static org.springframework.util.Assert.notNull;

/**
 * Boundary service implementation for customer.
 */
@Service
public class CustomerBoundaryServiceImpl implements CustomerBoundaryService {
    public static final Pageable DEFAULT_PAGEABLE = new PageRequest(0, 10);

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerBoundaryServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer createCustomer(String name, Address address) {
        notNull(name, "No name given for new customer to create");
        notNull(address, "No address given for new customer to create");

        Customer newCustomer = new Customer(name, address);

        return customerRepository.save(newCustomer);
    }

    @Override
    public Customer updateCustomer(UUID uuid, String name, Address address) {
        notNull(name, "No name given for customer to update");
        notNull(address, "No address given for customer to update");

        Customer customer = findCustomerByUuid(uuid);

        customer.name = name;
        customer.address = address;
        return customerRepository.save(customer);
    }

    @Override
    public void deleteCustomer(UUID uuid) {
        Customer customer = findCustomerByUuid(uuid);
        customerRepository.delete(customer.getId());
    }

    @Override
    public Customer findCustomerByUuid(UUID uuid) {
        notNull(uuid, "No UUID given for customer to look up");

        Customer customer = customerRepository.findByUuid(uuid);

        if (customer == null) {
            throw new ResourceNotFoundException("Customer not found");
        }

        return customer;
    }

    @Override
    public Page<Customer> findAllCustomers(Pageable pageable) {
        if (pageable == null) {
            pageable = DEFAULT_PAGEABLE;
        }

        return customerRepository.findAll(pageable);
    }

    @Override
    public Page<Customer> findSuggestionsForCustomer(String name, Pageable pageable) {
        notNull(name, "No name prefix given for customer to look up");
        notNull(pageable, "No pagination given for customer to look up");

        return customerRepository.findByNameStartingWithIgnoreCase(name, pageable);
    }

}
