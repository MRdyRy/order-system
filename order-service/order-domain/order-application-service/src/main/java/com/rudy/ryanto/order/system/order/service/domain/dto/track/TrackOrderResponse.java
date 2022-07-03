package com.rudy.ryanto.order.system.order.service.domain.dto.track;

import com.rudy.ryanto.order.system.domain.valueobject.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class TrackOrderResponse {
    @NotNull
    private final UUID orderTrackingID;
    @NotNull
    private final OrderStatus orderStatus;
    @NotNull
    private final List<String> failureMessages;
}
