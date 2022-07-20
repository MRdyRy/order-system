package com.rudy.ryanto.order.system.order.service.domain;

import com.rudy.ryanto.order.system.order.service.domain.entity.Order;
import com.rudy.ryanto.order.system.order.service.domain.entity.Product;
import com.rudy.ryanto.order.system.order.service.domain.entity.Restaurant;
import com.rudy.ryanto.order.system.order.service.domain.event.OrderCanceledEvent;
import com.rudy.ryanto.order.system.order.service.domain.event.OrderCreateEvent;
import com.rudy.ryanto.order.system.order.service.domain.event.OrderPaidEvent;
import com.rudy.ryanto.order.system.order.service.domain.exception.OrderDomainException;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService{
    public static final String ZONE_ID = "UTC";

    @Override
    public OrderCreateEvent validateAndInitiateOrder(Order order, Restaurant restaurant) {
        validateRestaurant(restaurant);
        setOrderProductInformation(order,restaurant);
        order.validateOrder();
        order.initializeOrder();
        log.info("Order is initiated : {}", order);
        return new OrderCreateEvent(order, ZonedDateTime.now(ZoneId.of(ZONE_ID)));
    }

    private void setOrderProductInformation(Order order, Restaurant restaurant) {
        order.getItems().forEach(orderItem -> restaurant.getProducts().forEach(restaurantProduct->{
            Product currentProduct = orderItem.getProduct();
            if(currentProduct.getId().equals(restaurantProduct.getId())){
                currentProduct.updateConfirmedProduct(restaurantProduct);
            }
        }));
    }

    private void validateRestaurant(Restaurant restaurant) {
        if(!restaurant.isActive()){
            throw new OrderDomainException("Restaurant with id : "+restaurant.getId().getValue()+" is not active!");
        }
    }

    @Override
    public OrderPaidEvent payOrder(Order order) {
        order.pay();
        log.info("Order :{} is paid",order);
        return new OrderPaidEvent(order, ZonedDateTime.now(ZoneId.of(ZONE_ID)));
    }

    @Override
    public void approveOrder(Order order) {
        order.approve();
        log.info("Order :{} is approve!", order);

    }

    @Override
    public OrderCanceledEvent cancelOrderPayment(Order order, List<String> failureMessages) {
        order.initCancel(failureMessages);
        log.info("Order payment for :{} is cancelling !",order );
        return new OrderCanceledEvent(order,ZonedDateTime.now(ZoneId.of(ZONE_ID)));
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {
        order.cancel(failureMessages);
        log.info("Order :{} is canceled!", order);
    }
}
