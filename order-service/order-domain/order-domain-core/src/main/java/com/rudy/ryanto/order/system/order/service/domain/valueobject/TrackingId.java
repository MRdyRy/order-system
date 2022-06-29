package com.rudy.ryanto.order.system.order.service.domain.valueobject;

import com.rudy.ryanto.order.system.domain.valueobject.BaseId;

import java.util.UUID;

public class TrackingId extends BaseId<UUID> {
    public TrackingId(UUID value) {
        super(value);
    }
}
