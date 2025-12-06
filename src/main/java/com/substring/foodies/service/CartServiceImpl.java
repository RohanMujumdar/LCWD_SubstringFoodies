package com.substring.foodies.service;

import com.substring.foodies.dto.AddItemToCartRequest;
import com.substring.foodies.dto.CartDto;
import com.substring.foodies.dto.CartItemsDto;
import com.substring.foodies.entity.Cart;
import com.substring.foodies.entity.CartItems;
import com.substring.foodies.entity.FoodItems;
import com.substring.foodies.entity.User;
import com.substring.foodies.exception.BadItemRequestException;
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
                .orElseThrow(() -> new ResourceNotFound(String.format("User not found with id = %s", userId)));

        FoodItems foodItem = foodItemRepository.findById(foodItemId)
                .orElseThrow(() -> new ResourceNotFound(String.format("Food item not found with id = %s", foodItemId)));

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
            throw new BadItemRequestException("Only items from the same restaurant can be added to the cart. Please clear the cart and try again.");
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
    public CartDto removeItemFromCart(int cartItemId, String userId) {

        Cart cart = cartRepository.findByCreatorId(userId).orElseThrow(()->new ResourceNotFound(String.format("Cart not found for userId = %s", userId)));
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

        if(!isExisting)
        {
            throw new ResourceNotFound(
                    String.format("Item not found in cart for itemId = %s", cartItemId)
            );
        }

        return modelMapper.map(cart, CartDto.class);
    }

    @Override
    public CartDto reduceItemFromCart(int cartItemId, String userId) {
        Cart cart = cartRepository.findByCreatorId(userId).orElseThrow(()->new ResourceNotFound(String.format("Cart not found for userId = %s", userId)));
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
