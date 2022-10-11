package com.rudy.ryanto.order.system.order.service.dataaccess.customer.adapter;

import com.rudy.ryanto.order.system.order.service.dataaccess.customer.mapper.CustomerDataAccessMapper;
import com.rudy.ryanto.order.system.order.service.dataaccess.customer.repository.CustomerJPARepository;
import com.rudy.ryanto.order.system.order.service.domain.entity.Customer;
import com.rudy.ryanto.order.system.order.service.domain.ports.output.repository.CustomerRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJPARepository customerJPARepository;
    private final CustomerDataAccessMapper customerDataAccessMapper;

    public CustomerRepositoryImpl(CustomerJPARepository customerJPARepository,
                                  CustomerDataAccessMapper customerDataAccessMapper) {
        this.customerJPARepository = customerJPARepository;
        this.customerDataAccessMapper = customerDataAccessMapper;
    }

    @Override
    public Optional<Customer> findCustomerId(UUID customerId) {
        return customerJPARepository.findById(customerId).map(customerDataAccessMapper::customerEntityToCustomer);
    }
}
