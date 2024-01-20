package com.example.demo.controller;

import com.example.demo.dto.shoppingcard.ShoppingCartDto;
import com.example.demo.dto.shoppingcard.cartitem.CartItemRequestDto;
import com.example.demo.dto.shoppingcard.cartitem.CartItemResponseDto;
import com.example.demo.dto.shoppingcard.cartitem.CartItemUpdateDataRequestDto;
import com.example.demo.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ShoppingCart management", description = "Carts management endpoints")
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@RestController
public class ShoppingCardController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @Operation(summary = "Get a ShoppingCart", description = "find user ShoppingCart")
    public ShoppingCartDto getUserShoppingCart(Authentication authentication, Pageable pageable) {
        return shoppingCartService.findShoppingCart(authentication, pageable);
    }

    @Operation(summary = "Add a book to ShoppingCart",
            description = "Add current book to ShoppingCart")
    @PostMapping
    public CartItemResponseDto addBookToShoppingCart(Authentication authentication,
                                @RequestBody @Valid CartItemRequestDto requestDto) {
        return shoppingCartService.addBookToShoppingCart(authentication, requestDto);
    }

    @PutMapping("/cart-items/{cartItemId}")
    @Operation(summary = "Update a book in ShoppingCart",
            description = "update a book in ShoppingCart")
    public CartItemResponseDto updateBookQuantityInShoppingCart(Authentication authentication,
                                           @PathVariable Long cartItemId,
                                           @RequestBody CartItemUpdateDataRequestDto requestDto) {
        return shoppingCartService.updateBookQuantityInTheShoppingCart(authentication,
                                                               cartItemId, requestDto);
    }

    @DeleteMapping("/cart-items/{cartItemId}")
    @Operation(summary = "Delete a book from ShoppingCart",
            description = "delete a book from ShoppingCart")
    public void deleteBookFromTheShoppingCart(Authentication authentication,
                                              @PathVariable Long cartItemId) {
        shoppingCartService.deleteBookFromTheShoppingCart(authentication, cartItemId);
    }
}
