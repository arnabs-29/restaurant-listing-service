package com.foodapp.restaurant.controller;

import com.foodapp.restaurant.dto.RestaurantDTO;
import com.foodapp.restaurant.service.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RestaurantControllerTest {

    @InjectMocks
    RestaurantController restaurantController;

    @Mock
    RestaurantService restaurantService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testFetchAllRestaurants(){

        List<RestaurantDTO> mockRestaurants= Arrays.asList(
                new RestaurantDTO(1,"Restaurant 1","Address 1","City 1","Description 1"),
                new RestaurantDTO(2,"Restaurant 2","Address 2","City 2","Description 2"),
                new RestaurantDTO(3,"Restaurant 3","Address 3","City 3","Description 3")
        );

        when(restaurantService.findAllRestaurants()).thenReturn(mockRestaurants);

        //Call the controller method
        ResponseEntity<List<RestaurantDTO>> response= restaurantController.fetchAllRestaurants();

        //Verify the response
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(mockRestaurants,response.getBody());

        //Verify that the service method was called
        verify(restaurantService,times(1)).findAllRestaurants();
    }

    @Test
    public void testSaveRestaurant(){
        //create a restaurant to be saved
        RestaurantDTO mockRestaurant = new RestaurantDTO(1,"Restaurant 1","Address 1","City 1","Description 1");
        //mock the service behaviour
        when(restaurantService.addRestaurantInDB(mockRestaurant)).thenReturn(mockRestaurant);
        //call the controller method
        ResponseEntity<RestaurantDTO> response= restaurantController.saveRestaurant(mockRestaurant);
        //Verify the response
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(mockRestaurant,response.getBody());

        //Verify that the service method was called
        verify(restaurantService,times(1)).addRestaurantInDB(mockRestaurant);
    }

    @Test
    public void testFetchRestaurantById(){
        //Create a mock restaurant ID
        int mockRestaurantId = 1;

        //Create a mock restaurant to be returned by the service
        RestaurantDTO mockRestaurant= new RestaurantDTO(1,"Restaurant 1","Address 1","City 1","Description 1");

        //mock the service behaviour
        when(restaurantService.findRestaurantById(mockRestaurantId)).thenReturn(new ResponseEntity<>(mockRestaurant,HttpStatus.OK));

        ResponseEntity<RestaurantDTO> response=restaurantController.fetchRestaurantById(mockRestaurantId);
        //verify the response
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(mockRestaurant,response.getBody());

        //Verify that the service method was called
        verify(restaurantService,times(1)).findRestaurantById(mockRestaurantId);

    }
}
