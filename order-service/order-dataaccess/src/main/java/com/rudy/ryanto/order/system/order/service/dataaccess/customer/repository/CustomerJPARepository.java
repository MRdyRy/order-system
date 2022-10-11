package com.rudy.ryanto.order.system.order.service.dataaccess.customer.repository;

import com.rudy.ryanto.order.system.order.service.dataaccess.customer.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerJPARepository extends JpaRepository<CustomerEntity, UUID> {
}
