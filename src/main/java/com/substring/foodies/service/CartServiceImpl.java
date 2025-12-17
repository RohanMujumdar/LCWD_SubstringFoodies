package com.substring.foodies.service;

import com.substring.foodies.dto.AddItemToCartRequest;
import com.substring.foodies.dto.CartDto;
import com.substring.foodies.dto.CartItemsDto;
import com.substring.foodies.entity.*;
import com.substring.foodies.exception.BadItemRequestException;
import com.substring.foodies.exception.FoodItemUnavailableException;
import com.substring.foodies.exception.ResourceNotFound;
import com.substring.foodies.repository.CartRepository;
import com.substring.foodies.repository.FoodItemRepository;
import com.substring.foodies.repository.RestaurantRepository;
import com.substring.foodies.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CartDto addItemToCart(AddItemToCartRequest addItemToCartRequest) {

        if (addItemToCartRequest.getQuantity() <= 0) {
            throw new BadItemRequestException("Quantity must be greater than zero");
        }

        String userId = addItemToCartRequest.getUserId();
        String foodItemId = addItemToCartRequest.getFoodItemId();
        String restoId = addItemToCartRequest.getRestoId();

        Restaurant restaurant = restaurantRepository.findById(restoId).orElseThrow(() -> new ResourceNotFound(String.format("Restaurant not found with id = %s", restoId)));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFound(String.format("User not found with id = %s", userId)));

        FoodItems foodItem = foodItemRepository.findById(foodItemId)
                .orElseThrow(() -> new ResourceNotFound(String.format("Food item not found with id = %s", foodItemId)));

        // Get or create cart
        Cart cart = cartRepository.findByCreatorId(userId)
                .orElseGet(() -> cartRepository.save(Cart.builder()
                        .creator(user)
                        .restaurant(restaurant)
                        .build()));


        if(!cart.getRestaurant().getId().equals(restoId))
        {
            throw new BadItemRequestException(
                    "Cart already contains items from another restaurant"
            );
        }


        FoodItems restaurantFood = restaurant.getFoodItemsList()
                                .stream()
                                .filter(food->food.getId().equals(foodItemId))
                                .findFirst()
                                .orElseThrow(
                                        ()->new ResourceNotFound("Our restaurant does not contain the food item with id = "+foodItemId)
                                );


        if(!restaurantFood.isAvailable())
        {
            throw new FoodItemUnavailableException(String.format("Food item with id = %s is currently not availble",foodItemId));
        }

        // Check if item already exists in cart
        boolean existing = false;
        List<CartItems> cartItemsList = cart.getCartItems();
        for (CartItems item : cartItemsList) {
            if (item.getFoodItems().getId().equals(foodItemId)) {
                item.setQuantity(item.getQuantity() + addItemToCartRequest.getQuantity());
                existing = true;
                break;
            }
        }

        if (!existing) {
            CartItems newItem = CartItems.builder()
                    .cart(cart)
                    .foodItems(foodItem)
                    .quantity(addItemToCartRequest.getQuantity())
                    .build();
            cart.getCartItems().add(newItem);
        }

        // Save and return updated cart
        cartRepository.save(cart);

        return modelMapper.map(cart, CartDto.class);
    }


    @Override
    public CartDto getCart(String userId) {
        Cart cart = cartRepository.findByCreatorId(userId).orElseThrow(()->new ResourceNotFound(String.format("Cart not found for userId = %s", userId)));
        return modelMapper.map(cart, CartDto.class);
    }

    @Override
    public CartDto removeItemFromCart(String cartItemId, String userId) {

        Cart cart = cartRepository.findByCreatorId(userId).orElseThrow(()->new ResourceNotFound(String.format("Cart not found for userId = %s", userId)));
        boolean isExisting = false;
        for (CartItems items: cart.getCartItems())
        {
            if(cartItemId.equals(items.getId()))
            {
                cart.getCartItems().remove(items);
                isExisting = true;
                cartRepository.save(cart);
                break;
            }
        }

        if(!isExisting)
        {
            throw new ResourceNotFound(
                    String.format("Item not found in cart for itemId = %s", cartItemId)
            );
        }

        return modelMapper.map(cart, CartDto.class);
    }

    @Override
    public CartDto reduceItemFromCart(String cartItemId, String userId) {
        Cart cart = cartRepository.findByCreatorId(userId).orElseThrow(()->new ResourceNotFound(String.format("Cart not found for userId = %s", userId)));
        boolean isExisting = false;
        for (CartItems items: cart.getCartItems())
        {
            if(cartItemId.equals(items.getId()))
            {
                items.setQuantity(items.getQuantity() - 1);
                if(items.getQuantity() == 0)
                {
                    cart.getCartItems().remove(items);
                }
                isExisting = true;
                cartRepository.save(cart);
                break;
            }
        }

        if(!isExisting)
        {
            throw new ResourceNotFound(
                    String.format("Item not found in cart for itemId = %s", cartItemId)
            );
        }

        return modelMapper.map(cart, CartDto.class);
    }

    @Override
    public List<CartItemsDto> getCartItems(String userId) {

        Cart cart = cartRepository.findByCreatorId(userId).orElseThrow(()->new ResourceNotFound(String.format("Cart not found with id = %s", userId)));

        List<CartItems> cartItems = cart.getCartItems();

        return  cartItems.stream().map(items->modelMapper.map(items, CartItemsDto.class)).toList();
    }

    @Override
    public void clearCart(String userId) {
        Cart cart = cartRepository.findByCreatorId(userId)
                .orElseThrow(() -> new ResourceNotFound(String.format("Cart not found for userId = %s", userId)));

        cart.getCartItems().clear(); // removes all cart items
        cartRepository.save(cart);  // persist the change
    }
}
