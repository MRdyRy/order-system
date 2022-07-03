package com.rudy.ryanto.order.system.order.service.domain.exception;

import com.rudy.ryanto.order.system.domain.exception.DomainException;

public class OrderNotFoundException extends DomainException {
    public OrderNotFoundException(String message) {
        super(message);
    }

    public OrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
