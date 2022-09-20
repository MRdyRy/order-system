package com.rudy.ryanto.order.system.order.service.dataaccess.order.adapter;

import com.rudy.ryanto.order.system.order.service.dataaccess.order.mapper.OrderDataAccessMapper;
import com.rudy.ryanto.order.system.order.service.dataaccess.order.repository.OrderJPARepository;
import com.rudy.ryanto.order.system.order.service.domain.entity.Order;
import com.rudy.ryanto.order.system.order.service.domain.ports.output.repository.OrderRepository;
import com.rudy.ryanto.order.system.order.service.domain.valueobject.TrackingId;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJPARepository orderJPARepositoryl;
    private final OrderDataAccessMapper orderDataAccessMapper;

    public OrderRepositoryImpl(OrderJPARepository orderJPARepositoryl, OrderDataAccessMapper orderDataAccessMapper) {
        this.orderJPARepositoryl = orderJPARepositoryl;
        this.orderDataAccessMapper = orderDataAccessMapper;
    }

    @Override
    public Order save(Order order) {
        return orderDataAccessMapper.orderEntityToOrder(
                orderJPARepositoryl.save(orderDataAccessMapper.orderToOrderEntity(order)));
    }

    @Override
    public Optional<Order> findByTrackingId(TrackingId trackingId) {
        return orderJPARepositoryl.findByTrackingId(trackingId.getValue()).map(orderDataAccessMapper::orderEntityToOrder);
    }
}
