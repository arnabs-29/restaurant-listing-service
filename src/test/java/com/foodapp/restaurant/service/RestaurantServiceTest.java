package com.foodapp.restaurant.service;

import com.foodapp.restaurant.dto.RestaurantDTO;
import com.foodapp.restaurant.entity.Restaurant;
import com.foodapp.restaurant.mapper.RestaurantMapper;
import com.foodapp.restaurant.repo.RestaurantRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RestaurantServiceTest {

    @Mock
    private RestaurantRepo restaurantRepo;

    @Mock
    private RestaurantMapper restaurantMapper;

    @InjectMocks
    private RestaurantService restaurantService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAllRestaurants(){
        List<Restaurant> mockRestaurants= Arrays.asList(
                new Restaurant(1,"Restaurant 1","Address 1","City 1","Description 1"),
                new Restaurant(2,"Restaurant 2","Address 2","City 2","Description 2"),
                new Restaurant(3,"Restaurant 3","Address 3","City 3","Description 3")
        );

        when(restaurantRepo.findAll()).thenReturn(mockRestaurants);

        // Mock the mapper behavior — this is critical
        when(restaurantMapper.mapRestaurantToRestaurantDTO(any(Restaurant.class)))
                .thenAnswer(invocation -> {
                    Restaurant r = invocation.getArgument(0);
                    return new RestaurantDTO(r.getId(), r.getName(), r.getAddress(), r.getCity(), r.getDescription());
                });
        List<RestaurantDTO> response=restaurantService.findAllRestaurants();


        // Assert
        assertEquals(mockRestaurants.size(), response.size());
        assertEquals("Restaurant 1", response.get(0).getName());
        assertEquals("City 2", response.get(1).getCity());
        assertEquals("Description 3", response.get(2).getDescription());

        // Verify interactions
        verify(restaurantRepo, times(1)).findAll();
        verify(restaurantMapper, times(mockRestaurants.size())).mapRestaurantToRestaurantDTO(any(Restaurant.class));
    }

    @Test
    public void testAddRestaurantInDB(){
        RestaurantDTO mockRestaurantDTO=new RestaurantDTO(1,"Restaurant 1","Address 1","City 1","Description 1");
        Restaurant mockRestaurant= new Restaurant(1, "Restaurant 1", "Address 1", "City 1", "Description 1");

        // Mock mapper behavior
        when(restaurantMapper.mapRestaurantDTOToRestaurant(mockRestaurantDTO)).thenReturn(mockRestaurant);
        when(restaurantMapper.mapRestaurantToRestaurantDTO(mockRestaurant)).thenReturn(mockRestaurantDTO);

        //Mock the repository behaviour
        when(restaurantRepo.save(mockRestaurant)).thenReturn(mockRestaurant);

        //call the service method
        RestaurantDTO savedRestaurantDTO= restaurantService.addRestaurantInDB(mockRestaurantDTO);

        //verify the result
        assertEquals(mockRestaurantDTO,savedRestaurantDTO);

        //verify that the repository method was called
        verify(restaurantRepo,times(1)).save(mockRestaurant);
        verify(restaurantMapper, times(1)).mapRestaurantDTOToRestaurant(mockRestaurantDTO);
        verify(restaurantMapper, times(1)).mapRestaurantToRestaurantDTO(mockRestaurant);
    }

    @Test
    public void testFindRestaurantById_Id_Exist(){
        //create a mock restaurant id
        int mockRestaurantId=1;

        //create a mock restaurant to be returned by the repository
        Restaurant mockRestaurant= new Restaurant(1, "Restaurant 1", "Address 1", "City 1", "Description 1");
        RestaurantDTO mockRestaurantDTO = new RestaurantDTO(1, "Restaurant 1", "Address 1", "City 1", "Description 1");

        //mock the repository and mapper behavior
        when(restaurantRepo.findById(mockRestaurantId)).thenReturn(Optional.of(mockRestaurant));
        when(restaurantMapper.mapRestaurantToRestaurantDTO(mockRestaurant)).thenReturn(mockRestaurantDTO);
        //call the service method
        ResponseEntity<RestaurantDTO> response=restaurantService.findRestaurantById(mockRestaurantId);

        //verify the response
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(mockRestaurantId,response.getBody().getId());

        //verify that the repository method was called
        verify(restaurantRepo,times(1)).findById(mockRestaurantId);
        verify(restaurantMapper, times(1)).mapRestaurantToRestaurantDTO(mockRestaurant);
    }

    @Test
    public void testFindRestaurantById_Id_Not_Exist(){
        //create a mock restaurant id
        int mockRestaurantId=1;


        //mock the repository and mapper behavior
        when(restaurantRepo.findById(mockRestaurantId)).thenReturn(Optional.empty());
        //call the service method
        ResponseEntity<RestaurantDTO> response=restaurantService.findRestaurantById(mockRestaurantId);

        //verify the response
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
        assertEquals(null,response.getBody());

        //verify that the repository method was called
        verify(restaurantRepo,times(1)).findById(mockRestaurantId);
    }
}
