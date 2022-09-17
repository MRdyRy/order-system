package com.rudy.ryanto.order.system.order.service.application.controller;

import com.rudy.ryanto.order.system.order.service.domain.dto.create.CreateOrderCommand;
import com.rudy.ryanto.order.system.order.service.domain.dto.create.CreateOrderResponse;
import com.rudy.ryanto.order.system.order.service.domain.dto.track.TrackOrderQuery;
import com.rudy.ryanto.order.system.order.service.domain.dto.track.TrackOrderResponse;
import com.rudy.ryanto.order.system.order.service.domain.ports.input.service.OrderApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/order", produces = "application/vnd.api.v1+json")
public class OrderController {

    private final OrderApplicationService orderApplicationService;

    public OrderController(OrderApplicationService orderApplicationService) {
        this.orderApplicationService = orderApplicationService;
    }

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody  CreateOrderCommand createOrderCommand){
        log.info("Creating Order for customer:{} at restaurant:{} ",createOrderCommand.getCustomerId(),createOrderCommand.getRestaurantId());
        CreateOrderResponse orderResponse = orderApplicationService.createOrder(createOrderCommand);
        log.info("Order created with status is:{} and tracking id:{} ",orderResponse.getOrderStatus(), orderResponse.getOrderTrackingId());
        return ResponseEntity.ok(orderResponse);
    }


    @GetMapping("/{trackingId}")
    public ResponseEntity<TrackOrderResponse> getOrderByTrackingId(@PathVariable UUID trackingId){
        log.info("Tracking orderId :{} ",trackingId);
        TrackOrderResponse trackOrderResponse = orderApplicationService.trackOrder(TrackOrderQuery.builder().orderTrackingId(trackingId).build());
        log.info("Order ID:{} current status is:{}",trackOrderResponse.getOrderTrackingID(), trackOrderResponse.getOrderStatus());
        return ResponseEntity.ok(trackOrderResponse);
    }

}
