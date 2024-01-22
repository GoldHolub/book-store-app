package com.example.demo.service.impl;

import com.example.demo.dto.shoppingcard.ShoppingCartDto;
import com.example.demo.dto.shoppingcard.cartitem.CartItemRequestDto;
import com.example.demo.dto.shoppingcard.cartitem.CartItemResponseDto;
import com.example.demo.dto.shoppingcard.cartitem.CartItemUpdateDataRequestDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.CartItemMapper;
import com.example.demo.mapper.ShoppingCartMapper;
import com.example.demo.model.Book;
import com.example.demo.model.CartItem;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.User;
import com.example.demo.repository.book.BookRepository;
import com.example.demo.repository.shoppingcart.CartItemRepository;
import com.example.demo.repository.shoppingcart.ShoppingCartRepository;
import com.example.demo.service.ShoppingCartService;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;
    private final BookRepository bookRepository;

    @Override
    public ShoppingCartDto findShoppingCart(Authentication authentication, Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find ShoppingCart by userId: "
                        + user.getId() + ".Put some items to ShoppingCart to create it."));

        ShoppingCartDto shoppingCartDto = shoppingCartMapper.toDto(shoppingCart);
        Set<CartItemResponseDto> cartItemsSet = cartItemRepository
                .findByShoppingCartId(shoppingCart.getId(), pageable)
                .stream()
                .map(cartItemMapper::toDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        shoppingCartDto.setCartItems(cartItemsSet);
        return shoppingCartDto;
    }

    @Override
    public CartItemResponseDto addBookToShoppingCart(Authentication authentication,
                                                     CartItemRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId())
                .orElseGet(() -> createNewShoppingCart(user));

        Book book = bookRepository.findById(requestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Book by current id: "
                        + requestDto.getBookId() + " doesn't exist"));

        CartItem cartItem = cartItemMapper.toModel(requestDto);
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setBook(book);
        CartItem savedCartItem = cartItemRepository.save(cartItem);
        Set<CartItem> cartItems = cartItemRepository.findByShoppingCartId(shoppingCart.getId());
        cartItems.add(savedCartItem);
        shoppingCart.setCartItems(cartItems);
        shoppingCartRepository.save(shoppingCart);
        return cartItemMapper.toDto(cartItem);
    }

    @Override
    public CartItemResponseDto updateBookQuantityInTheShoppingCart(Authentication authentication,
                                                         Long cartItemId,
                                                         CartItemUpdateDataRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        CartItem cartItem = getValidCartItem(cartItemId);
        ShoppingCart shoppingCart = getValidShoppingCart(user.getId());
        if (!cartItemRepository.findByShoppingCartId(shoppingCart.getId()).contains(cartItem)) {
            throw new EntityNotFoundException("Can't find CartItem by id: " + cartItemId
                    + " in the ShoppingCart to update");
        }
        cartItem.setQuantity(requestDto.getQuantity());
        cartItemRepository.save(cartItem);
        return cartItemMapper.toDto(cartItem);
    }

    @Override
    public void deleteBookFromTheShoppingCart(Authentication authentication, Long cartItemId) {
        User user = (User) authentication.getPrincipal();
        CartItem cartItem = getValidCartItem(cartItemId);
        ShoppingCart shoppingCart = getValidShoppingCart(user.getId());
        if (!cartItemRepository.findByShoppingCartId(shoppingCart.getId()).contains(cartItem)) {
            throw new EntityNotFoundException("Can't find CartItem by id: " + cartItemId
                    + " in the ShoppingCart to delete");
        }
        cartItemRepository.delete(cartItem);
    }

    @Override
    public ShoppingCart createNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        return shoppingCartRepository.save(shoppingCart);
    }

    private CartItem getValidCartItem(Long cartItemId) {
        return cartItemRepository.findById(cartItemId).orElseThrow(
                () -> new EntityNotFoundException("Can't find CartItem by id: " + cartItemId));
    }

    private ShoppingCart getValidShoppingCart(Long userId) {
        return shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("Can't find ShoppingCart by userId: "
                        + userId));
    }
}
