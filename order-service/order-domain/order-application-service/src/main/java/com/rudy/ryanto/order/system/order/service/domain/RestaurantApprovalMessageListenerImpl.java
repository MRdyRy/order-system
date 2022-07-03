package com.rudy.ryanto.order.system.order.service.domain;

import com.rudy.ryanto.order.system.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.rudy.ryanto.order.system.order.service.domain.ports.input.message.listener.restaurantapproval.RestaurantApprovalMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@Service
public class RestaurantApprovalMessageListenerImpl implements RestaurantApprovalMessageListener {
    @Override
    public void orderApproved(RestaurantApprovalResponse restaurantApprovalResponse) {

    }

    @Override
    public void orderRejected(RestaurantApprovalResponse restaurantApprovalResponse) {

    }
}
