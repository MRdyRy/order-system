package com.rudy.ryanto.order.system.domain.valueobject;

import java.util.UUID;

public class ProductId extends BaseId<UUID> {
    public ProductId(UUID value) {
        super(value);
    }
}
