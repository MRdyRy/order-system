package com.rudy.ryanto.order.system.order.service.dataaccess.order.mapper;

import com.rudy.ryanto.order.system.domain.valueobject.*;
import com.rudy.ryanto.order.system.order.service.dataaccess.order.entity.OrderAddressEntity;
import com.rudy.ryanto.order.system.order.service.dataaccess.order.entity.OrderEntity;
import com.rudy.ryanto.order.system.order.service.dataaccess.order.entity.OrderItemEntity;
import com.rudy.ryanto.order.system.order.service.domain.entity.Order;
import com.rudy.ryanto.order.system.order.service.domain.entity.OrderItem;
import com.rudy.ryanto.order.system.order.service.domain.entity.Product;
import com.rudy.ryanto.order.system.order.service.domain.valueobject.OrderItemId;
import com.rudy.ryanto.order.system.order.service.domain.valueobject.StreetAddress;
import com.rudy.ryanto.order.system.order.service.domain.valueobject.TrackingId;
import org.aspectj.weaver.ast.Literal;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.rudy.ryanto.order.system.order.service.domain.entity.Order.FAILURE_MESSAGE_DELIMITER;

@Component
public class OrderDataAccessMapper {

    public OrderEntity orderToOrderEntity(Order order){
        OrderEntity orderEntity = OrderEntity.builder()
                .orderStatus(order.getOrderStatus())
                .id(order.getId().getValue())
                .customerId(order.getCustomerId().getValue())
                .restaurantId(order.getRestaurantId().getValue())
                .trackingId(order.getTrackingId().getValue())
                .orderAddressEntity(mappAddress(order.getDeliveryAddress()))
                .price(order.getPrice().getAmmount())
                .items(mapOrderItemsEntity(order.getItems()))
                .failureMessage(order.getFailureMessage()!=null ? String.join(FAILURE_MESSAGE_DELIMITER,order.getFailureMessage()): "")
                .build();

        orderEntity.getOrderAddressEntity().setOrder(orderEntity);
        orderEntity.getItems().forEach(orderItemEntity -> orderItemEntity.setOrder(orderEntity));
        return orderEntity;
    }

    public Order orderEntityToOrder(OrderEntity orderEntity){
        return Order.builder()
                .id(new OrderId(orderEntity.getId()))
                .customerId(new CustomerId(orderEntity.getCustomerId()))
                .restaurantId(new RestaurantId(orderEntity.getRestaurantId()))
                .trackingId(new TrackingId(orderEntity.getTrackingId()))
                .deliveryAddress(addresEntityToDeliveryAddress(orderEntity.getOrderAddressEntity()))
                .price(new Money(orderEntity.getPrice()))
                .items(orderItemEntityToOrderItem(orderEntity.getItems()))
                .orderStatus(orderEntity.getOrderStatus())
                .failureMessage(orderEntity.getFailureMessage().isEmpty()? new ArrayList<>()
                        : new ArrayList<>(Arrays.asList(
                                orderEntity.getFailureMessage()
                                        .split(FAILURE_MESSAGE_DELIMITER))))
                .build();
    }

    private List<OrderItem> orderItemEntityToOrderItem(List<OrderItemEntity> items) {
        return items.stream()
                .map(orderItemEntity -> OrderItem.builder()
                        .orderItemId(new OrderItemId(orderItemEntity.getId()))
                        .product(new Product(new ProductId(orderItemEntity.getProducId())))
                        .price(new Money(orderItemEntity.getPrice()))
                        .quantity(orderItemEntity.getQuantity())
                        .subTotal(new Money(orderItemEntity.getSubTotal()))
                        .build())
                .collect(Collectors.toList());
    }

    private StreetAddress addresEntityToDeliveryAddress(OrderAddressEntity orderAddressEntity) {
        return new StreetAddress(orderAddressEntity.getId(),
                orderAddressEntity.getStreet(),
                orderAddressEntity.getPostalCode(),
                orderAddressEntity.getCity());
    }

    private List<OrderItemEntity> mapOrderItemsEntity(List<OrderItem> items) {
        return items.stream()
                .map(orderItem -> OrderItemEntity.builder()
                        .id(orderItem.getId().getValue())
                        .producId(orderItem.getProduct().getId().getValue())
                        .price(orderItem.getPrice().getAmmount())
                        .quantity(orderItem.getQuantity())
                        .subTotal(orderItem.getSubTotal().getAmmount())
                .build())
                .collect(Collectors.toList());
    }


    private OrderAddressEntity mappAddress(StreetAddress deliveryAddress) {
        return OrderAddressEntity.builder()
                .postalCode(deliveryAddress.getPostalCode())
                .city(deliveryAddress.getCity())
                .street(deliveryAddress.getCity())
                .id(deliveryAddress.getId())
                .build();
    }
}
