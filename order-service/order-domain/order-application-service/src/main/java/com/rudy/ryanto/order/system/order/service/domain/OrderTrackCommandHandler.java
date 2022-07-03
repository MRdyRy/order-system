package com.rudy.ryanto.order.system.order.service.domain;

import com.rudy.ryanto.order.system.order.service.domain.dto.track.TrackOrderQuery;
import com.rudy.ryanto.order.system.order.service.domain.dto.track.TrackOrderResponse;
import com.rudy.ryanto.order.system.order.service.domain.entity.Order;
import com.rudy.ryanto.order.system.order.service.domain.exception.OrderDomainException;
import com.rudy.ryanto.order.system.order.service.domain.exception.OrderNotFoundException;
import com.rudy.ryanto.order.system.order.service.domain.mapper.OrderDataMapper;
import com.rudy.ryanto.order.system.order.service.domain.ports.output.repository.OrderRepository;
import com.rudy.ryanto.order.system.order.service.domain.valueobject.TrackingId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component
public class OrderTrackCommandHandler {

    private final OrderDataMapper orderDataMapper;
    private final OrderRepository orderRepository;

    public OrderTrackCommandHandler(OrderDataMapper orderDataMapper, OrderRepository orderRepository) {
        this.orderDataMapper = orderDataMapper;
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery){
        Optional<Order> order = orderRepository.findByTrackingId(new TrackingId(trackOrderQuery.getOrderTrackingId()));
        if(order.isEmpty()){
            log.warn("Could not find track order with tracking id : {}",trackOrderQuery.getOrderTrackingId());
            throw new OrderNotFoundException("Could not track order with tracking id : {}"+trackOrderQuery.getOrderTrackingId());
        }

        return orderDataMapper.orderToTrackOrderResponse(order.get());
    }
}
