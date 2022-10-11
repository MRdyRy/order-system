package com.rudy.ryanto.order.system.order.service.dataaccess.restaurant.adapter;

import com.rudy.ryanto.order.system.order.service.dataaccess.restaurant.entity.RestaurantEntity;
import com.rudy.ryanto.order.system.order.service.dataaccess.restaurant.mapper.RestaurantDataAccessMapper;
import com.rudy.ryanto.order.system.order.service.dataaccess.restaurant.repository.RestaurantJPARepository;
import com.rudy.ryanto.order.system.order.service.domain.entity.Restaurant;
import com.rudy.ryanto.order.system.order.service.domain.ports.output.repository.RestaurantRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final RestaurantJPARepository restaurantJPARepository;
    private final RestaurantDataAccessMapper restaurantDataAccessMapper;

    public RestaurantRepositoryImpl(RestaurantJPARepository restaurantJPARepository, RestaurantDataAccessMapper restaurantDataAccessMapper) {
        this.restaurantJPARepository = restaurantJPARepository;
        this.restaurantDataAccessMapper = restaurantDataAccessMapper;
    }

    @Override
    public Optional<Restaurant> findRestaurantInfromation(Restaurant restaurant) {
        List<UUID>restaurantProducts = restaurantDataAccessMapper.restaurantToRestaurantProducts(restaurant);
        Optional<List<RestaurantEntity>>restaurantEntities = restaurantJPARepository.findByRestaurantIdAndProductIdIn(restaurant.getId().getValue(),restaurantProducts);
        return restaurantEntities.map(restaurantDataAccessMapper::restaurantEntityToRestaurant);
    }
}
