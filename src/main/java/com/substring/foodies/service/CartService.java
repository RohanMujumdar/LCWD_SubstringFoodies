package com.substring.foodies.service;

import com.substring.foodies.dto.AddItemToCartRequest;
import com.substring.foodies.dto.CartDto;

public interface CartService {

    CartDto addItemToCart(AddItemToCartRequest addItemToCartRequest);

    CartDto getCart(String userId);

    CartDto removeItemFromCart(String cartItemId, String userId);

    CartDto clearDto(String userId);


}
