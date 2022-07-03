package com.rudy.ryanto.order.system.order.service.domain.ports.input.message.listener.restaurantapproval;

import com.rudy.ryanto.order.system.order.service.domain.dto.message.RestaurantApprovalResponse;

public interface RestaurantApprovalMessageListener {

    void orderApproved(RestaurantApprovalResponse restaurantApprovalResponse);

    void orderRejected(RestaurantApprovalResponse restaurantApprovalResponse);
}
