package com.substring.foodies.service;

import com.substring.foodies.dto.AddItemToCartRequest;
import com.substring.foodies.dto.CartDto;
import com.substring.foodies.dto.CartItemsDto;
import com.substring.foodies.entity.Cart;
import com.substring.foodies.entity.CartItems;
import com.substring.foodies.entity.FoodItems;
import com.substring.foodies.entity.User;
import com.substring.foodies.exception.ResourceNotFound;
import com.substring.foodies.repository.CartRepository;
import com.substring.foodies.repository.FoodItemRepository;
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
    private ModelMapper modelMapper;

    @Override
    public CartDto addItemToCart(AddItemToCartRequest addItemToCartRequest) {

        String userId = addItemToCartRequest.getUserId();
        Long foodItemId = addItemToCartRequest.getFoodItemId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFound("User Not Found"));

        FoodItems foodItem = foodItemRepository.findById(foodItemId)
                .orElseThrow(() -> new ResourceNotFound("Food Item Not Found"));

        if(!foodItem.isAvailable())
        {
            throw new ResourceNotFound("Food Item is currently not available");
        }


        // Get or create cart
        Cart cart = cartRepository.findByCreatorId(userId)
                .orElseGet(() -> cartRepository.save(Cart.builder()
                        .creator(user)
                        .build()));

        boolean isFoodItemFromSameRestaurant = true;
        for(CartItems items: cart.getCartItems())
        {
            if(!items.getFoodItems().getRestaurant().getId().equals(foodItem.getRestaurant().getId()))
            {
                isFoodItemFromSameRestaurant = false;
                break;
            }
        }

        if(!isFoodItemFromSameRestaurant)
        {
            throw new ResourceNotFound("Food item is not from same restaurant. Please clear the cart.");
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

        Cart cart = cartRepository.findByCreatorId(userId).orElseThrow(()->new ResourceNotFound("No user found"));
        return modelMapper.map(cart, CartDto.class);

    }

    @Override
    public CartDto removeItemFromCart(int cartItemId, String userId) {

        Cart cart = cartRepository.findByCreatorId(userId).orElseThrow(()->new ResourceNotFound("No user found"));
        boolean isExisting = false;
        for (CartItems items: cart.getCartItems())
        {
            if(cartItemId == items.getId())
            {
                cart.getCartItems().remove(items);
                isExisting = true;
                cartRepository.save(cart);
                break;
            }
        }

        if(isExisting == false)
        {
            throw new ResourceNotFound("Item not found");
        }

        return modelMapper.map(cart, CartDto.class);
    }

    @Override
    public CartDto reduceItemFromCart(int cartItemId, String userId) {
        Cart cart = cartRepository.findByCreatorId(userId).orElseThrow(()->new ResourceNotFound("No user found"));
        boolean isExisting = false;
        for (CartItems items: cart.getCartItems())
        {
            if(cartItemId == items.getId())
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

        if(isExisting == false)
        {
            throw new ResourceNotFound("Item not found");
        }

        return modelMapper.map(cart, CartDto.class);
    }

    @Override
    public List<CartItemsDto> getCartItems(String userId) {

        Cart cart = cartRepository.findByCreatorId(userId).orElseThrow(()->new ResourceNotFound("User Not Found"));

        List<CartItems> cartItems = cart.getCartItems();

        return  cartItems.stream().map(items->modelMapper.map(items, CartItemsDto.class)).toList();
    }

    @Override
    public void clearCart(String userId) {
        Cart cart = cartRepository.findByCreatorId(userId)
                .orElseThrow(() -> new ResourceNotFound("Cart not found for user"));

        cart.getCartItems().clear(); // removes all cart items
        cartRepository.save(cart);  // persist the change
    }
}
