package com.rudy.ryanto.order.system.order.service.domain.mapper;

import com.rudy.ryanto.order.system.domain.valueobject.CustomerId;
import com.rudy.ryanto.order.system.domain.valueobject.Money;
import com.rudy.ryanto.order.system.domain.valueobject.ProductId;
import com.rudy.ryanto.order.system.domain.valueobject.RestaurantId;
import com.rudy.ryanto.order.system.order.service.domain.dto.create.CreateOrderCommand;
import com.rudy.ryanto.order.system.order.service.domain.dto.create.CreateOrderResponse;
import com.rudy.ryanto.order.system.order.service.domain.dto.create.OrderAddress;
import com.rudy.ryanto.order.system.order.service.domain.dto.track.TrackOrderQuery;
import com.rudy.ryanto.order.system.order.service.domain.dto.track.TrackOrderResponse;
import com.rudy.ryanto.order.system.order.service.domain.entity.Order;
import com.rudy.ryanto.order.system.order.service.domain.entity.OrderItem;
import com.rudy.ryanto.order.system.order.service.domain.entity.Product;
import com.rudy.ryanto.order.system.order.service.domain.entity.Restaurant;
import com.rudy.ryanto.order.system.order.service.domain.valueobject.StreetAddress;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderDataMapper {

    public Restaurant createOrderCommandToRestaurant(CreateOrderCommand createOrderCommand){
        return Restaurant.builder()
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .products(createOrderCommand.getItems().stream().map(orderItem ->
                        new Product(new ProductId(orderItem.getProductId())))
                        .collect(Collectors.toList()))
                .build();
    }

    public Order createOrderCommandToOrder(CreateOrderCommand createOrderCommand){
        return Order.builder()
                .customerId(new CustomerId(createOrderCommand.getCustomerId()))
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .deliveryAddress(orderAddressToStreetAddress(createOrderCommand.getAddress()))
                .price(new Money(createOrderCommand.getPrice()))
                .items(orderItemsToOrderItemEntities(createOrderCommand.getItems()))
                .build();
    }

    public CreateOrderResponse orderToCreateOrderResponse(Order order, String message){
        return CreateOrderResponse.builder()
                .message(message)
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus()).build();
    }

    private List<OrderItem> orderItemsToOrderItemEntities(List<com.rudy.ryanto.order.system.order.service.domain.dto.create.OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem -> OrderItem.builder()
                        .product(new Product(new ProductId(orderItem.getProductId())))
                        .price(new Money(orderItem.getPrice()))
                        .quantity(orderItem.getQuantity())
                        .subTotal(new Money(orderItem.getSubTotal())).build())
                .collect(Collectors.toList());

    }

    private StreetAddress orderAddressToStreetAddress(OrderAddress address) {
        return new StreetAddress(UUID.randomUUID(), address.getStreet(),address.getPostalCode(),address.getCity());
    }

    public TrackOrderResponse orderToTrackOrderResponse(Order order){
        return TrackOrderResponse.builder()
                .orderStatus(order.getOrderStatus())
                .orderTrackingID(order.getTrackingId().getValue())
                .failureMessages(order.getFailureMessage())
                .build();
    }
}
