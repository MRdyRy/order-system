package com.rudy.ryanto.order.system.order.service.domain.entity;

import com.rudy.ryanto.order.system.domain.entity.AggregrateRoot;
import com.rudy.ryanto.order.system.domain.valueobject.CustomerId;

public class Customer extends AggregrateRoot<CustomerId> {

    public Customer() {
    }

    public Customer(CustomerId customerId) {
        super.setId(customerId);
    }
}
