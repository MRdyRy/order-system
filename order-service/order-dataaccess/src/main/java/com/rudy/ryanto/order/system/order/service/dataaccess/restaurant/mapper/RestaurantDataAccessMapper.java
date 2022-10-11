package com.rudy.ryanto.order.system.order.service.dataaccess.restaurant.mapper;

import com.rudy.ryanto.order.system.domain.valueobject.Money;
import com.rudy.ryanto.order.system.domain.valueobject.ProductId;
import com.rudy.ryanto.order.system.domain.valueobject.RestaurantId;
import com.rudy.ryanto.order.system.order.service.dataaccess.restaurant.entity.RestaurantEntity;
import com.rudy.ryanto.order.system.order.service.dataaccess.restaurant.exception.RestaurantDataAccessMapperException;
import com.rudy.ryanto.order.system.order.service.domain.entity.Product;
import com.rudy.ryanto.order.system.order.service.domain.entity.Restaurant;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RestaurantDataAccessMapper {

    public List<UUID> restaurantToRestaurantProducts(Restaurant restaurant){
        return restaurant.getProducts().stream()
                .map(product -> product.getId().getValue())
                .collect(Collectors.toList());
    }

    public Restaurant restaurantEntityToRestaurant(List<RestaurantEntity> restaurantEntities){
        RestaurantEntity restaurantEntity = restaurantEntities.stream()
                .findFirst()
                .orElseThrow(()->new RestaurantDataAccessMapperException("Restaurant could not be found!"));

        List<Product> restaurantProducts = restaurantEntities.stream().map(entity->
                new Product(
                        new ProductId(
                                entity.getProductId()), entity.getProductName(), new Money(entity.getProductPrice()))
        ).collect(Collectors.toList());

        return Restaurant.builder()
                .restaurantId(new RestaurantId(restaurantEntity.getRestaurantId()))
                .products(restaurantProducts)
                .isActive(restaurantEntity.getRestaurantActive())
                .build();
    }
}
