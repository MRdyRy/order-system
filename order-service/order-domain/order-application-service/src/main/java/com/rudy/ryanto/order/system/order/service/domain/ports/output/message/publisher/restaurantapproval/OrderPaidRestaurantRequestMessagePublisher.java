package com.rudy.ryanto.order.system.order.service.domain.ports.output.message.publisher.restaurantapproval;

import com.rudy.ryanto.order.system.domain.event.publisher.DomainEventPublisher;
import com.rudy.ryanto.order.system.order.service.domain.event.OrderPaidEvent;

public interface OrderPaidRestaurantRequestMessagePublisher extends DomainEventPublisher<OrderPaidEvent> {
}
