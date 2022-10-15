package com.rudy.ryanto.order.system.order.service.messaging.mapper;

import com.rudy.ryanto.order.system.domain.valueobject.OrderApprovalStatus;
import com.rudy.ryanto.order.system.domain.valueobject.PaymentStatus;
import com.rudy.ryanto.order.system.kafka.order.avro.model.*;
import com.rudy.ryanto.order.system.order.service.domain.dto.message.PaymentResponse;
import com.rudy.ryanto.order.system.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.rudy.ryanto.order.system.order.service.domain.entity.Order;
import com.rudy.ryanto.order.system.order.service.domain.event.OrderCanceledEvent;
import com.rudy.ryanto.order.system.order.service.domain.event.OrderCreateEvent;
import com.rudy.ryanto.order.system.order.service.domain.event.OrderPaidEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

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

    public RestaurantApprovalRequestAvroModel orderPaidEventToRestaurantApprovalRequestAvroModel(OrderPaidEvent orderPaidEvent){
        Order order = orderPaidEvent.getOrder();
        return RestaurantApprovalRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setOrderId(order.getId().getValue().toString())
                .setRestaurantId(order.getRestaurantId().getValue().toString())
                .setRestaurantOrderStatus(RestaurantOrderStatus.valueOf(order.getOrderStatus().name()))
                .setProducts(order.getItems().stream().map(orderItem ->
                        Product.newBuilder()
                                .setId(orderItem.getOrderId().getValue().toString())
                                .setQuantity(orderItem.getQuantity())
                                .build())
                        .collect(Collectors.toList()))
                .setPrice(order.getPrice().getAmmount())
                .setCreatedAt(orderPaidEvent.getCreateAt().toInstant())
                .setRestaurantOrderStatus(RestaurantOrderStatus.PAID)
                .build();
    }

    public PaymentResponse paymentResponseAvroModelToPaymentResponse(PaymentResponsetAvroModel paymentResponsetAvroModel){
        return PaymentResponse.builder()
                .id(paymentResponsetAvroModel.getId())
                .sagaId(paymentResponsetAvroModel.getSagaId())
                .paymentId(paymentResponsetAvroModel.getPaymentId())
                .customerId(paymentResponsetAvroModel.getCustomerId())
                .orderId(paymentResponsetAvroModel.getOrderId())
                .price(paymentResponsetAvroModel.getPrice())
                .createAt(paymentResponsetAvroModel.getCreatedAt())
                .paymentStatus(PaymentStatus.valueOf(paymentResponsetAvroModel.getPaymentStatus().name()))
                .failureMessages(paymentResponsetAvroModel.getFailureMessages())
                .build();
    }

    public RestaurantApprovalResponse restaurantApprovalResponseAvroModelToRestaurantApprovalResponse(
            RestaurantApprovalResponseAvroModel restaurantApprovalResponseAvroModel) {
        return RestaurantApprovalResponse.builder()
                .id(restaurantApprovalResponseAvroModel.getId())
                .sagaId(restaurantApprovalResponseAvroModel.getSagaId())
                .restaurantId(restaurantApprovalResponseAvroModel.getRestaurantId())
                .orderId(restaurantApprovalResponseAvroModel.getOrderId())
                .createdAt(restaurantApprovalResponseAvroModel.getCreatedAt())
                .failureMessages(restaurantApprovalResponseAvroModel.getFailureMessages())
                .orderApprovalStatus(OrderApprovalStatus.valueOf(
                        restaurantApprovalResponseAvroModel.getOrderApprovalStatus().name()
                )).build();
    }
}
