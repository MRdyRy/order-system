package com.rudy.ryanto.order.system.order.service.domain;

import com.rudy.ryanto.order.system.order.service.domain.dto.create.CreateOrderCommand;
import com.rudy.ryanto.order.system.order.service.domain.dto.create.CreateOrderResponse;
import com.rudy.ryanto.order.system.order.service.domain.dto.track.TrackOrderQuery;
import com.rudy.ryanto.order.system.order.service.domain.dto.track.TrackOrderResponse;
import com.rudy.ryanto.order.system.order.service.domain.ports.input.service.OrderApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@Service
class OrderApplicationServiceImpl implements OrderApplicationService {

    private final OrderCreateCommandHandler orderCommandCreateHandler;

    private final OrderTrackCommandHandler orderTrackCommandHandler;

    OrderApplicationServiceImpl(OrderCreateCommandHandler orderCommandCreateHandler, OrderTrackCommandHandler orderTrackCommandHandler) {
        this.orderCommandCreateHandler = orderCommandCreateHandler;
        this.orderTrackCommandHandler = orderTrackCommandHandler;
    }

    @Override
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        return orderCommandCreateHandler.createOrder(createOrderCommand);
    }

    @Override
    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
        return orderTrackCommandHandler.trackOrder(trackOrderQuery);
    }
}
