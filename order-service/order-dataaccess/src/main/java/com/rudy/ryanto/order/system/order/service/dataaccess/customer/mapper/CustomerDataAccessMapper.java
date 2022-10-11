package com.rudy.ryanto.order.system.order.service.dataaccess.customer.mapper;

import com.rudy.ryanto.order.system.domain.valueobject.CustomerId;
import com.rudy.ryanto.order.system.order.service.dataaccess.customer.entity.CustomerEntity;
import com.rudy.ryanto.order.system.order.service.domain.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataAccessMapper {
    public Customer customerEntityToCustomer(CustomerEntity customerEntity){
        return new Customer(new CustomerId(customerEntity.getId()));
    }
}
