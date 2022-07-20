package com.rudy.ryanto.order.system.order.service.domain;

import com.rudy.ryanto.order.system.domain.util.ConstanstUtils;
import com.rudy.ryanto.order.system.domain.valueobject.*;
import com.rudy.ryanto.order.system.order.service.domain.dto.create.CreateOrderCommand;
import com.rudy.ryanto.order.system.order.service.domain.dto.create.CreateOrderResponse;
import com.rudy.ryanto.order.system.order.service.domain.dto.create.OrderAddress;
import com.rudy.ryanto.order.system.order.service.domain.dto.create.OrderItem;
import com.rudy.ryanto.order.system.order.service.domain.entity.Customer;
import com.rudy.ryanto.order.system.order.service.domain.entity.Order;
import com.rudy.ryanto.order.system.order.service.domain.entity.Product;
import com.rudy.ryanto.order.system.order.service.domain.entity.Restaurant;
import com.rudy.ryanto.order.system.order.service.domain.exception.OrderDomainException;
import com.rudy.ryanto.order.system.order.service.domain.mapper.OrderDataMapper;
import com.rudy.ryanto.order.system.order.service.domain.ports.input.service.OrderApplicationService;
import com.rudy.ryanto.order.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.rudy.ryanto.order.system.order.service.domain.ports.output.repository.OrderRepository;
import com.rudy.ryanto.order.system.order.service.domain.ports.output.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = OrderTestConfiguration.class)
public class OrderApplicationServiceTest {

    @Autowired
    private OrderApplicationService orderApplicationService;

    @Autowired
    private OrderDataMapper orderDataMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private CreateOrderCommand createOrderCommand;
    private CreateOrderCommand createOrderCommandWrongPrice;
    private CreateOrderCommand createOrderCommandWrongProductPrice;
    private final UUID CUSTOMER_ID = UUID.fromString("181fca9e-01dd-11ed-b939-0242ac120002");
    private final UUID RESTAURANT_ID = UUID.fromString("31dc8d3c-01dd-11ed-b939-0242ac120002");
    private final UUID PRODUCT_ID = UUID.fromString("64b826b2-01dd-11ed-b939-0242ac120002");
    private final UUID ORDER_ID = UUID.fromString("6cabfa10-01dd-11ed-b939-0242ac120002");
    private final BigDecimal PRICE = new BigDecimal("300.00");

    @BeforeAll
    public void init(){
        createOrderCommand = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(OrderAddress.builder()
                        .city("test city")
                        .postalCode("1123")
                        .street("test street")
                        .build())
                .price(PRICE)
                .items(List.of(
                        OrderItem.builder()
                                .price(new BigDecimal("100.00"))
                                .quantity(1)
                                .subTotal(new BigDecimal("100.00"))
                                .productId(PRODUCT_ID)
                                .build(),
                        OrderItem.builder()
                                .price(new BigDecimal("100.00"))
                                .quantity(2)
                                .subTotal(new BigDecimal("200.00"))
                                .productId(PRODUCT_ID)
                                .build()
                ))
                .build();

        createOrderCommandWrongPrice = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(OrderAddress.builder()
                        .city("test city")
                        .postalCode("1123")
                        .street("test street")
                        .build())
                .price(new BigDecimal("100.00"))
                .items(List.of(
                        OrderItem.builder()
                                .price(new BigDecimal("100.00"))
                                .quantity(1)
                                .subTotal(new BigDecimal("100.00"))
                                .productId(PRODUCT_ID)
                                .build(),
                        OrderItem.builder()
                                .price(new BigDecimal("50.00"))
                                .quantity(4)
                                .subTotal(new BigDecimal("200.00"))
                                .productId(PRODUCT_ID)
                                .build()
                ))
                .build();

        createOrderCommandWrongProductPrice = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(OrderAddress.builder()
                        .city("test city")
                        .postalCode("1123")
                        .street("test street")
                        .build())
                .price(new BigDecimal("310.00"))
                .items(List.of(
                        OrderItem.builder()
                                .price(new BigDecimal("110.00"))
                                .quantity(1)
                                .subTotal(new BigDecimal("110.00"))
                                .productId(PRODUCT_ID)
                                .build(),
                        OrderItem.builder()
                                .price(new BigDecimal("50.00"))
                                .quantity(4)
                                .subTotal(new BigDecimal("200.00"))
                                .productId(PRODUCT_ID)
                                .build()
                ))
                .build();

        Customer customer = new Customer();
        customer.setId(new CustomerId(CUSTOMER_ID));

        Restaurant restaurantResponse = Restaurant.builder()
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .products(List.of(
                        new Product(new ProductId(PRODUCT_ID),"product-1",new Money(new BigDecimal("100.00"))),
                        new Product(new ProductId(PRODUCT_ID),"product-2",new Money(new BigDecimal("100.00"))))
                )
                .isActive(true)
                .build();

        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        order.setId(new OrderId(ORDER_ID));


        when(customerRepository.findCustomerId(CUSTOMER_ID))
                .thenReturn(Optional.of(customer));
        when(restaurantRepository.findRestaurantInfromation(orderDataMapper.createOrderCommandToRestaurant(createOrderCommand)))
                .thenReturn(Optional.of(restaurantResponse));
        when(orderRepository.save(any(Order.class)))
                .thenReturn(order);
    }


    @Test
    public void createOrderTest(){
        CreateOrderResponse createOrderResponse = orderApplicationService.createOrder(createOrderCommand);
        assertEquals(createOrderResponse.getOrderStatus(),OrderStatus.PENDING);
        assertEquals(createOrderResponse.getMessage(), ConstanstUtils.MESSAGE.ORDER_CREATED);
        assertNotNull(createOrderResponse.getOrderTrackingId());
    }

    @Test
    public void createOrderWrongTotalPriceTest(){
        OrderDomainException orderDomainException = assertThrows(OrderDomainException.class,
                ()-> orderApplicationService.createOrder(createOrderCommandWrongPrice));
        assertNotNull(orderDomainException.getMessage());
    }


    @Test
    public void createOrderWrongProductPriceTest(){
        OrderDomainException orderDomainException = assertThrows(OrderDomainException.class,
                ()-> orderApplicationService.createOrder(createOrderCommandWrongProductPrice));
        assertNotNull(orderDomainException.getMessage());
    }

    @Test
    public void createOrderWithPassiveRestaurantTest(){
        Restaurant restaurantResponse = Restaurant.builder()
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .products(List.of(
                        new Product(new ProductId(PRODUCT_ID),"product-1",new Money(new BigDecimal("100.00"))),
                        new Product(new ProductId(PRODUCT_ID),"product-2",new Money(new BigDecimal("100.00"))))
                )
                .isActive(false)
                .build();
        when(restaurantRepository.findRestaurantInfromation(orderDataMapper.createOrderCommandToRestaurant(createOrderCommand)))
                .thenReturn(Optional.of(restaurantResponse));
        OrderDomainException orderDomainException = assertThrows(OrderDomainException.class,
                ()->orderApplicationService.createOrder(createOrderCommand));
        assertNotNull(orderDomainException.getMessage());
        assertEquals(orderDomainException.getMessage(),"Restaurant with id : "+RESTAURANT_ID+" is not active!");
    }
}
