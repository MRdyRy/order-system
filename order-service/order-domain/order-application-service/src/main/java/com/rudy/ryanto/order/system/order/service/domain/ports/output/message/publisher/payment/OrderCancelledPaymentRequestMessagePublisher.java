package com.rudy.ryanto.order.system.order.service.domain.ports.output.message.publisher.payment;

import com.rudy.ryanto.order.system.domain.event.publisher.DomainEventPublisher;
import com.rudy.ryanto.order.system.order.service.domain.event.OrderCanceledEvent;

public interface OrderCancelledPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCanceledEvent> {
}
