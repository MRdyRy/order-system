package com.rudy.ryanto.order.system.order.service.domain;

import com.rudy.ryanto.order.system.domain.exception.DomainException;
import com.rudy.ryanto.order.system.order.service.domain.dto.create.CreateOrderCommand;
import com.rudy.ryanto.order.system.order.service.domain.dto.create.CreateOrderResponse;
import com.rudy.ryanto.order.system.order.service.domain.entity.Customer;
import com.rudy.ryanto.order.system.order.service.domain.entity.Order;
import com.rudy.ryanto.order.system.order.service.domain.entity.Restaurant;
import com.rudy.ryanto.order.system.order.service.domain.event.OrderCreateEvent;
import com.rudy.ryanto.order.system.order.service.domain.exception.OrderDomainException;
import com.rudy.ryanto.order.system.order.service.domain.mapper.OrderDataMapper;
import com.rudy.ryanto.order.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.rudy.ryanto.order.system.order.service.domain.ports.output.repository.OrderRepository;
import com.rudy.ryanto.order.system.order.service.domain.ports.output.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class OrderCommandCreateHandler {

    private final OrderDomainService orderDomainService;

    private final OrderRepository orderRepository;

    private final CustomerRepository customerRepository;

    private final RestaurantRepository restaurantRepository;

    private final OrderDataMapper orderDataMapper;

    public OrderCommandCreateHandler(OrderDomainService orderDomainService, OrderRepository orderRepository,
                                     CustomerRepository customerRepository, RestaurantRepository restaurantRepository,
                                     OrderDataMapper orderDataMapper) {
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.restaurantRepository = restaurantRepository;
        this.orderDataMapper = orderDataMapper;
    }

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand){
        checkCustomer(createOrderCommand.getCustomerId());
        Restaurant restaurant = checkRestaurant(createOrderCommand);
        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        OrderCreateEvent orderCreateEvent = orderDomainService.validateAndInitiateOrder(order, restaurant);
        Order orderResult = saveOrder(order);
        log.info("order is created :{}", orderResult);
        return orderDataMapper.orderToCreateOrderResponse(orderResult,"order created");
    }

    private Restaurant checkRestaurant(CreateOrderCommand createOrderCommand) {
        Restaurant restaurant = orderDataMapper.createOrderCommandToRestaurant(createOrderCommand);
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findRestaurantInfromation(restaurant);
        if(optionalRestaurant.isEmpty()){

            log.warn("Restaurant with id :{} is not Exist", restaurant.getId().getValue());
            throw new OrderDomainException("Restaurant with id : "+restaurant.getId().getValue()+" is not Exist");
        }

        return optionalRestaurant.get();
    }


    private void checkCustomer(UUID customerId) {
        Optional<Customer> customer = customerRepository.findCustomerId(customerId);
        if(customer.isEmpty()){

            log.warn("Customer with id :{} is not Exist", customerId);
            throw new OrderDomainException("Customer with id : "+customerId+" is not Exist");
        }
    }

    private Order saveOrder(Order order){
        Order orderResult =  orderRepository.save(order);
        if(null==orderResult){
            log.error("Could not save order");
            throw new DomainException("Could not save order!");
        }
        log.info("Order is saved, with id {}",orderResult.getId().getValue());
        return orderResult;
    }
}
