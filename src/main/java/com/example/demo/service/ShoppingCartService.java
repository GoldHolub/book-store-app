package com.example.demo.service;

import com.example.demo.dto.shoppingcard.ShoppingCartDto;
import com.example.demo.dto.shoppingcard.cartitem.CartItemRequestDto;
import com.example.demo.dto.shoppingcard.cartitem.CartItemResponseDto;
import com.example.demo.dto.shoppingcard.cartitem.CartItemUpdateDataRequestDto;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface ShoppingCartService {
    ShoppingCartDto findShoppingCart(Authentication authentication, Pageable pageable);

    CartItemResponseDto addBookToShoppingCart(Authentication authentication,
                                              CartItemRequestDto requestDto);

    CartItemResponseDto updateBookQuantityInTheShoppingCart(Authentication authentication,
            Long cartItemId,
            CartItemUpdateDataRequestDto requestDto);

    void deleteBookFromTheShoppingCart(Authentication authentication, Long cartItemId);

    ShoppingCart createNewShoppingCart(User user);
}
