package com.rudy.ryanto.order.system.order.service.domain.event;

import com.rudy.ryanto.order.system.order.service.domain.entity.Order;

import java.time.ZonedDateTime;

public class OrderCreateEvent extends OrderEvent{

    public OrderCreateEvent(Order order, ZonedDateTime createAt) {
        super(order, createAt);
    }
}
