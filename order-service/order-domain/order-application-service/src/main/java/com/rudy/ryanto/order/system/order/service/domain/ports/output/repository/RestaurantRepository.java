package com.rudy.ryanto.order.system.order.service.domain.ports.output.repository;

import com.rudy.ryanto.order.system.order.service.domain.entity.Restaurant;

import java.util.Optional;

public interface RestaurantRepository {
    Optional<Restaurant> findRestaurantInfromation(Restaurant restaurant);
}
