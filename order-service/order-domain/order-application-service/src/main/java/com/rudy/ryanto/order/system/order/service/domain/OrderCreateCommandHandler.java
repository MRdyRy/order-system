package com.rudy.ryanto.order.system.order.service.domain;

import com.rudy.ryanto.order.system.domain.util.ConstanstUtils;
import com.rudy.ryanto.order.system.order.service.domain.dto.create.CreateOrderCommand;
import com.rudy.ryanto.order.system.order.service.domain.dto.create.CreateOrderResponse;
import com.rudy.ryanto.order.system.order.service.domain.event.OrderCreateEvent;
import com.rudy.ryanto.order.system.order.service.domain.mapper.OrderDataMapper;
import com.rudy.ryanto.order.system.order.service.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderCreateCommandHandler {

    private final OrderCreateHelper orderCreateHelper;

    private final OrderDataMapper orderDataMapper;

    private final OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher;

    public OrderCreateCommandHandler(OrderCreateHelper orderCreateHelper,
                                     OrderDataMapper orderDataMapper,
                                     OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher) {
        this.orderCreateHelper = orderCreateHelper;
        this.orderDataMapper = orderDataMapper;
        this.orderCreatedPaymentRequestMessagePublisher = orderCreatedPaymentRequestMessagePublisher;
    }


    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand){
        OrderCreateEvent orderCreateEvent = orderCreateHelper.persistOrder(createOrderCommand);
        log.info("Order is created with id : {}", orderCreateEvent.getOrder().getId().getValue());
        orderCreatedPaymentRequestMessagePublisher.publish(orderCreateEvent);
        return orderDataMapper.orderToCreateOrderResponse(orderCreateEvent.getOrder(), ConstanstUtils.MESSAGE.ORDER_CREATED);
    }
}
