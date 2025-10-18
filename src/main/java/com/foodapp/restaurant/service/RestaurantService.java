package com.foodapp.restaurant.service;

import com.foodapp.restaurant.dto.RestaurantDTO;
import com.foodapp.restaurant.entity.Restaurant;
import com.foodapp.restaurant.mapper.RestaurantMapper;
import com.foodapp.restaurant.repo.RestaurantRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    @Autowired
    RestaurantRepo restaurantRepo;

    private final RestaurantMapper restaurantMapper;

    public RestaurantService(RestaurantMapper restaurantMapper) {
        this.restaurantMapper = restaurantMapper;
    }

    public List<RestaurantDTO> findAllRestaurants(){
        List<Restaurant> restaurants=restaurantRepo.findAll();
        return restaurants.stream().map(restaurantMapper::mapRestaurantToRestaurantDTO).collect(Collectors.toList());
    }

    public RestaurantDTO addRestaurantInDB(RestaurantDTO restaurantDTO){
        Restaurant addedRestaurant= restaurantRepo.save(restaurantMapper.mapRestaurantDTOToRestaurant(restaurantDTO));
        return restaurantMapper.mapRestaurantToRestaurantDTO(addedRestaurant);
    }

    public ResponseEntity<RestaurantDTO> findRestaurantById(int id){
        Optional<Restaurant> restaurant=restaurantRepo.findById(id);
        return restaurant.map(value -> new ResponseEntity<>(restaurantMapper.mapRestaurantToRestaurantDTO(value), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }
}
