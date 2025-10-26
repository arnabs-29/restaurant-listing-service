package com.foodapp.restaurant.controller;

import com.foodapp.restaurant.dto.RestaurantDTO;
import com.foodapp.restaurant.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurant")
@CrossOrigin
public class RestaurantController {

    @Autowired
    RestaurantService restaurantService;

    @GetMapping("/allRestaurant")
    public ResponseEntity<List<RestaurantDTO>> fetchAllRestaurants(){
        List<RestaurantDTO> listRest= restaurantService.findAllRestaurants();
        return new ResponseEntity<>(listRest, HttpStatus.OK);
    }

    @GetMapping("/fetchById/{id}")
    public ResponseEntity<RestaurantDTO> fetchRestaurantById(@PathVariable Integer id ){
        return restaurantService.findRestaurantById(id);
    }

    @PostMapping("/addRestaurant")
    public ResponseEntity<RestaurantDTO> saveRestaurant(@RequestBody RestaurantDTO restaurantDTO){
        RestaurantDTO addedRestaurant=restaurantService.addRestaurantInDB(restaurantDTO);
        return new ResponseEntity<>(addedRestaurant,HttpStatus.CREATED);
    }
}
