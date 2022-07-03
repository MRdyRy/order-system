package com.rudy.ryanto.order.system.order.service.domain.entity;

import com.rudy.ryanto.order.system.domain.entity.BaseEntity;
import com.rudy.ryanto.order.system.domain.valueobject.Money;
import com.rudy.ryanto.order.system.domain.valueobject.ProductId;

public class Product extends BaseEntity<ProductId> {
    private String name;
    private Money price;

    public Product(ProductId productId, String name, Money price) {
        super.setId(productId);
        this.name = name;
        this.price = price;
    }
    public Product(ProductId productId) {
        super.setId(productId);
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    public void updateConfirmedProduct(Product restaurantProduct) {
        this.name = restaurantProduct.getName();
        this.price = restaurantProduct.getPrice();
    }
}
