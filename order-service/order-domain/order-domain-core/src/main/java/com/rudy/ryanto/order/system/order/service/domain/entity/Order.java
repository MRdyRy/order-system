package com.rudy.ryanto.order.system.order.service.domain.entity;

import com.rudy.ryanto.order.system.domain.entity.AggregrateRoot;
import com.rudy.ryanto.order.system.domain.valueobject.*;
import com.rudy.ryanto.order.system.order.service.domain.exception.OrderDomainException;
import com.rudy.ryanto.order.system.order.service.domain.valueobject.OrderItemId;
import com.rudy.ryanto.order.system.order.service.domain.valueobject.StreetAddress;
import com.rudy.ryanto.order.system.order.service.domain.valueobject.TrackingId;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Order extends AggregrateRoot<OrderId> {
    private final CustomerId customerId;
    private final RestaurantId restaurantId;
    private final StreetAddress deliveryAddress;
    private final Money price;
    private final List<OrderItem> items;
    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessage;

    public void initializeOrder(){
        setId(new OrderId(UUID.randomUUID())); //set random UUID to OrderID
        trackingId = new TrackingId(UUID.randomUUID()); //set random UUID to tracking ID
        orderStatus = OrderStatus.PENDING; // initialize first order status as PENDING
        initializeOrderItems();
    }

    public void validateOrder(){
        validateInitialOrder();
        validateTotalPrice();
        validateItemsPrice();
    }


    public void pay(){
        if(orderStatus!=OrderStatus.PENDING){
            throw new OrderDomainException("Order is not correct state for pay operation!");
        }
        orderStatus = OrderStatus.PAID;
    }

    public void approve(){
        if(orderStatus!=OrderStatus.PAID){
            throw new OrderDomainException("Order is not correct state for approve operation!");
        }
        orderStatus = OrderStatus.APPROVED;
    }

    public void initCancel(List<String> failureMessage){
        if(orderStatus!=OrderStatus.PAID){
            throw new OrderDomainException("Order is not correct state for initCancel operation!");
        }
        orderStatus = OrderStatus.CANCELLING;
        updateFailureMessages(failureMessage);
    }

    public void cancel(List<String> failureMessage){
        if(!(orderStatus == OrderStatus.CANCELLING || orderStatus == OrderStatus.PENDING)){
            throw new OrderDomainException("Order is not correct state for cancel operation!");
        }
        orderStatus = OrderStatus.CANCELLED;
        updateFailureMessages(failureMessage);
    }

    private void updateFailureMessages(List<String> failureMessage) {
        if(this.failureMessage != null && failureMessage!=null){
            this.failureMessage.addAll(failureMessage.stream().filter(message -> !message.isEmpty()).collect(Collectors.toList()));
        }
        if(this.failureMessage==null){
            this.failureMessage = failureMessage;
        }
    }

    private void validateItemsPrice() {
        Money orderItemsTotal = items.stream().map(orderItem -> {
           validateOrderItem(orderItem);
           return orderItem.getSubTotal();
        }).reduce(Money.ZERO,Money::add);

        if(!price.equals(orderItemsTotal)){
            throw new OrderDomainException("Total price "+price.getAmmount()+" is not equal to Order Item total: "+orderItemsTotal+" !");
        }
    }

    private void validateOrderItem(OrderItem orderItem) {
        if(!orderItem.isPriceValid()){
            throw new OrderDomainException("Order item price : "+orderItem.getPrice().getAmmount()+" is not valid " +
                    "for product "+orderItem.getProduct().getId().getValue());
        }
    }

    private void validateTotalPrice() {
        if(null==price || !price.isGreaterThanZero()){
            throw new OrderDomainException("Total price must be greater than zero!");
        }
    }

    private void validateInitialOrder() {
        if(null!=orderStatus || null!=getId()){
            throw new OrderDomainException("Order is not in correct state for initialization!");
        }
    }

    private void initializeOrderItems() {
        long itemId = 1;
        for (OrderItem item : items) {
            item.initializeOrderItem(super.getId(),new OrderItemId(itemId++));
        }
    }

    private Order(Builder builder) {
        super.setId(builder.orderId);
        customerId = builder.customerId;
        restaurantId = builder.restaurantId;
        deliveryAddress = builder.deliveryAddress;
        price = builder.price;
        items = builder.items;
        trackingId = builder.trackingId;
        orderStatus = builder.orderStatus;
        failureMessage = builder.failureMessage;
    }

    public static Builder builder() {
        return new Builder();
    }


    public CustomerId getCustomerId() {
        return customerId;
    }

    public RestaurantId getRestaurantId() {
        return restaurantId;
    }

    public StreetAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public Money getPrice() {
        return price;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public TrackingId getTrackingId() {
        return trackingId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<String> getFailureMessage() {
        return failureMessage;
    }

    public static final class Builder {
        private OrderId orderId;
        private CustomerId customerId;
        private RestaurantId restaurantId;
        private StreetAddress deliveryAddress;
        private Money price;
        private List<OrderItem> items;
        private TrackingId trackingId;
        private OrderStatus orderStatus;
        private List<String> failureMessage;

        private Builder() {
        }

        public Builder id(OrderId val) {
            orderId = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder deliveryAddress(StreetAddress val) {
            deliveryAddress = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder items(List<OrderItem> val) {
            items = val;
            return this;
        }

        public Builder trackingId(TrackingId val) {
            trackingId = val;
            return this;
        }

        public Builder orderStatus(OrderStatus val) {
            orderStatus = val;
            return this;
        }

        public Builder failureMessage(List<String> val) {
            failureMessage = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
