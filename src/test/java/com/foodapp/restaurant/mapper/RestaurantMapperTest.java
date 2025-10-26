package com.foodapp.restaurant.mapper;

import com.foodapp.restaurant.dto.RestaurantDTO;
import com.foodapp.restaurant.entity.Restaurant;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class RestaurantMapperTest {

    // Create a real mapper instance (not mocked)
    private final RestaurantMapper mapper = Mappers.getMapper(RestaurantMapper.class);

    @Test
    public void testMapRestaurantToDTO() {
        // Arrange
        Restaurant restaurant = new Restaurant(1, "Tasty Bites", "123 Main St", "New York", "Italian cuisine");

        // Act
        RestaurantDTO dto = mapper.mapRestaurantToRestaurantDTO(restaurant);

        dto= new RestaurantDTO(1, "Tasty Bites", "123 Main St", "New York", "Italian cuisine");
        // Assert
        assertNotNull(dto);
        assertEquals(restaurant.getId(), dto.getId());
        assertEquals(restaurant.getName(), dto.getName());
        assertEquals(restaurant.getAddress(), dto.getAddress());
        assertEquals(restaurant.getCity(), dto.getCity());
        assertEquals(restaurant.getDescription(), dto.getDescription());

    }

    @Test
    public void testMapDTOToRestaurant() {
        // Arrange
        RestaurantDTO dto = new RestaurantDTO(2, "Burger Hub", "45 Food Lane", "Chicago", "Best burgers in town");

        // Act
        Restaurant restaurant = mapper.mapRestaurantDTOToRestaurant(dto);
        restaurant=new Restaurant(2, "Burger Hub", "45 Food Lane", "Chicago", "Best burgers in town");
        // Assert
        assertNotNull(restaurant);
        assertEquals(dto.getId(), restaurant.getId());
        assertEquals(dto.getName(), restaurant.getName());
        assertEquals(dto.getAddress(), restaurant.getAddress());
        assertEquals(dto.getCity(), restaurant.getCity());
        assertEquals(dto.getDescription(), restaurant.getDescription());

    }
}
