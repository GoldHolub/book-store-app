package com.example.demo.service.impl;

import com.example.demo.dto.order.OrderResponseDto;
import com.example.demo.dto.order.OrderStatusRequestDto;
import com.example.demo.dto.order.OrderSubmissionRequestDto;
import com.example.demo.dto.order.orderitem.OrderItemResponseDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.exception.OrderException;
import com.example.demo.mapper.OrderItemMapper;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.model.CartItem;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.User;
import com.example.demo.repository.order.OrderItemRepository;
import com.example.demo.repository.order.OrderRepository;
import com.example.demo.repository.shoppingcart.ShoppingCartRepository;
import com.example.demo.service.OrderService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public OrderResponseDto createOrder(Authentication authentication, Pageable pageable,
                                        OrderSubmissionRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId()).orElseThrow(
                () -> new EntityNotFoundException("Can't find ShoppingCart by userId: "
                                                                    + user.getId()));

        Set<CartItem> cartItems = shoppingCart.getCartItems();
        if (cartItems.isEmpty()) {
            throw new OrderException("ShoppingCart is empty. "
                    + "Please fulfill it before order confirmation");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(Order.Status.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(requestDto.getShippingAddress());
        order.setTotal(countTotalOrderPrice(cartItems));
        Order savedOrder = orderRepository.save(order);
        savedOrder.setOrderItems(fullFillSetOfOrderItems(savedOrder, cartItems));
        OrderResponseDto orderResponseDto = orderMapper.toDto(savedOrder);

        cleanShoppingCart(shoppingCart);
        return orderResponseDto;
    }

    @Override
    @Transactional
    public List<OrderResponseDto> getOrders(Authentication authentication, Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return orderRepository.findOrdersByUserId(user.getId(), pageable).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public OrderResponseDto updateOrderStatus(Long orderId, OrderStatusRequestDto requestDto) {
        Order order = orderRepository.findOrderById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Can't find order by id: " + orderId));
        order.setStatus(requestDto.getStatus());
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public List<OrderItemResponseDto> getOrderItemsByOrderId(Authentication authentication,
                                                             Long orderId, Pageable pageable) {
        Order order = getValidOrder(authentication, orderId);
        List<OrderItem> orderItems = orderItemRepository.findOrderItemsByOrderId(orderId, pageable);
        return orderItems.stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemResponseDto getOrderItemByIdAndOrderId(Authentication authentication,
                                                           Long orderId, Long orderItemId) {
        Order order = getValidOrder(authentication, orderId);
        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(
                () -> new EntityNotFoundException("Can't find OrderItem by id: " + orderId));
        if (!orderItem.getOrder().getId().equals(order.getId())) {
            throw new OrderException("You don't have such OrderItem with id: "
                                            + orderItemId + " in your Order");
        }
        return orderItemMapper.toDto(orderItem);
    }

    private BigDecimal countTotalOrderPrice(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(c -> c.getBook().getPrice().multiply(BigDecimal.valueOf(c.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private final Set<OrderItem> fullFillSetOfOrderItems(Order order, Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(c -> fullFillOrderItem(order, c))
                .collect(Collectors.toSet());
    }

    private OrderItem fullFillOrderItem(Order order, CartItem cartItem) {
        OrderItem orderItem = orderItemMapper.toModelFromCartItem(cartItem);
        orderItem.setOrder(order);
        return orderItemRepository.save(orderItem);
    }

    private Order getValidOrder(Authentication authentication, Long orderId) {
        User user = (User) authentication.getPrincipal();
        Order order = orderRepository.findOrderById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Can't find order by id: " + orderId));
        if (!order.getUser().getId().equals(user.getId())) {
            throw new OrderException("You don't have access to Order with id: " + orderId);
        }
        return order;
    }

    private ShoppingCart cleanShoppingCart(ShoppingCart shoppingCart) {
        shoppingCart.getCartItems().clear();
        return shoppingCartRepository.save(shoppingCart);
    }
}
