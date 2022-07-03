package com.rudy.ryanto.order.system.order.service.domain.ports.output.repository;

import com.rudy.ryanto.order.system.order.service.domain.entity.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {

    Optional<Customer> findCustomerId(UUID customerId);
}
