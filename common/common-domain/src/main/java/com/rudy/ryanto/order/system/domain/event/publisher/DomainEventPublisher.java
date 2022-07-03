package com.rudy.ryanto.order.system.domain.event.publisher;

import com.rudy.ryanto.order.system.domain.event.DomainEvent;

public interface DomainEventPublisher<T extends DomainEvent> {


    void publish(T domainEvent);
}
