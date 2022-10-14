package com.rudy.ryanto.order.system.order.service.messaging.mapper;

import com.rudy.ryanto.order.system.kafka.order.avro.model.PaymentOrderStatus;
import com.rudy.ryanto.order.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.rudy.ryanto.order.system.order.service.domain.entity.Order;
import com.rudy.ryanto.order.system.order.service.domain.event.OrderCanceledEvent;
import com.rudy.ryanto.order.system.order.service.domain.event.OrderCreateEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderMessagingDataMapper {

    public PaymentRequestAvroModel orderCreateEventPaymentRequestAvroModel(OrderCreateEvent orderCreateEvent){
        Order order = orderCreateEvent.getOrder();
        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setCustomerId(order.getCustomerId().getValue().toString())
                .setOrderId(order.getId().getValue().toString())
                .setPrice(order.getPrice().getAmmount())
                .setCreatedAt(orderCreateEvent.getCreateAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.PENDING)
                .build();
    }

    public PaymentRequestAvroModel orderCancelledEventPaymentRequestAvroModel(OrderCanceledEvent orderCanceledEvent){
        Order order = orderCanceledEvent.getOrder();
        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setCustomerId(order.getCustomerId().getValue().toString())
                .setOrderId(order.getId().getValue().toString())
                .setPrice(order.getPrice().getAmmount())
                .setCreatedAt(orderCanceledEvent.getCreateAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.CANCELLED)
                .build();
    }
}
