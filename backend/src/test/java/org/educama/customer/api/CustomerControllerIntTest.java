package org.educama.customer.api;

import org.educama.common.exceptions.ResourceNotFoundException;
import org.educama.customer.api.datastructure.CustomerBuilder;
import org.educama.customer.api.resource.CustomerListResource;
import org.educama.customer.api.resource.CustomerResource;
import org.educama.customer.api.resource.SaveCustomerResource;
import org.educama.customer.api.resource.assembler.CustomerListResourceAssembler;
import org.educama.customer.api.resource.assembler.CustomerResourceAssembler;
import org.educama.customer.boundary.impl.CustomerBoundaryServiceImpl;
import org.educama.customer.model.Customer;
import org.educama.customer.repository.CustomerRepository;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Integration test for {@link CustomerController} endpoints functionality.
 */
@SpringBootTest(classes = {CustomerController.class, CustomerResourceAssembler.class,
        CustomerListResourceAssembler.class, CustomerBoundaryServiceImpl.class})
public class CustomerControllerIntTest {
    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();
    private static final int ENTRIES_PER_PAGE = 10;

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    private CustomerController cut;

    @MockBean
    private CustomerRepository repo;

    @Before
    public void setUp() {
        when(repo.save(any(Customer.class))).thenAnswer(answer -> answer.getArgumentAt(0, Customer.class));
    }

    @Test
    public void shouldCreateNewCustomer() {
        SaveCustomerResource customer = new CustomerBuilder().buildRes();

        CustomerResource response = cut.createCustomer(customer).getBody();

        assertThat(response.name).isEqualTo(customer.name);
    }

    @Test
    public void shouldNotCreateCustomerWithMissingCustomerToSave() {
        CustomerResource response = cut.createCustomer(null).getBody();

        assertThat(response).isNull();
    }

    @Test
    public void shouldUpdateCustomer() {
        UUID customerId = UUID.randomUUID();

        when(repo.findByUuid(customerId)).thenReturn(new CustomerBuilder().id(customerId).buildModel());

        CustomerResource updatedCustomer = cut.updateCustomer(customerId, new CustomerBuilder().name("newName").buildRes()).getBody();

        assertThat(updatedCustomer).extracting("uuid", "name")
                .containsExactly(customerId, "newName");
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldThrowExceptionWithMissingCustomer() {
        UUID customerId = UUID.randomUUID();

        when(repo.findByUuid(customerId)).thenReturn(null);

        cut.updateCustomer(customerId, new CustomerBuilder().buildRes());
    }

    @Test
    public void shouldNotUpdateCustomerWithMissingCustomerToUpdate() {
        CustomerResource updatedCustomer = cut.updateCustomer(UUID.randomUUID(), null).getBody();

        assertThat(updatedCustomer).isNull();
    }

    @Test
    public void shouldDeleteCustomer() {
        UUID customerId = UUID.randomUUID();

        Customer customer = new CustomerBuilder().buildModel();
        when(repo.findByUuid(customerId)).thenReturn(customer);

        cut.deleteCustomer(customerId);

        verify(repo).delete(customer.getId());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldThrowExceptionWithMissingCustomerToDelete() {
        cut.deleteCustomer(UUID.randomUUID());
    }

    @Test
    public void shouldRetrieveAllCustomers() {
        PageRequest pageable = new PageRequest(0, ENTRIES_PER_PAGE);

        Customer customer = new CustomerBuilder().buildModel();
        when(repo.findAll(pageable)).thenReturn(new PageImpl<>(Collections.singletonList(customer)));

        CustomerListResource customers = cut.findAll(null);

        assertThat(customers.getCustomers()).extracting("name").containsExactly(customer.name);
    }

    @Test
    public void shouldRetrieveAllCustomersWithNoPaginationGiven() {
        Customer customer = new CustomerBuilder().buildModel();

        when(repo.findAll(CustomerBoundaryServiceImpl.DEFAULT_PAGEABLE))
                .thenReturn(new PageImpl<>(Collections.singletonList(customer)));

        CustomerListResource customers = cut.findAll(null);

        assertThat(customers.getCustomers()).extracting("name").containsExactly(customer.name);
    }

    @Test
    public void shouldFindNoCustomerWithNoIdGiven() {
        CustomerResource customer = cut.findOneByUuid(null).getBody();

        assertThat(customer).isNull();
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldThrowExceptionWithNoCustomerFound() {
        cut.findOneByUuid(UUID.randomUUID()).getBody();
    }

    @Test
    public void shouldReturnCustomerWithId() {
        UUID customerId = UUID.randomUUID();

        Customer customer = new CustomerBuilder().id(customerId).buildModel();
        when(repo.findByUuid(customerId)).thenReturn(customer);

        CustomerResource foundCustomer = cut.findOneByUuid(customerId).getBody();

        assertThat(foundCustomer.name).isEqualTo(customer.name);
    }

    @Test
    public void shouldSuggestCustomers() {
        PageRequest pageable = new PageRequest(0, ENTRIES_PER_PAGE);

        Customer customer = new CustomerBuilder().buildModel();
        when(repo.findByNameStartingWithIgnoreCase("first", pageable)).thenReturn(new PageImpl<>(Collections.singletonList(customer)));

        CustomerListResource customers = cut.findSuggestions("first", pageable);

        assertThat(customers.getCustomers()).extracting("name").containsExactly(customer.name);
    }
}
