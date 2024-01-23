package com.example.demo.service;

import com.example.demo.dto.order.OrderResponseDto;
import com.example.demo.dto.order.OrderStatusRequestDto;
import com.example.demo.dto.order.OrderSubmissionRequestDto;
import com.example.demo.dto.order.orderitem.OrderItemResponseDto;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface OrderService {
    OrderResponseDto createOrder(Authentication authentication, Pageable pageable,
                                 OrderSubmissionRequestDto requestDto);

    List<OrderResponseDto> getOrders(Authentication authentication, Pageable pageable);

    OrderResponseDto updateOrderStatus(Long orderId, OrderStatusRequestDto requestDto);

    List<OrderItemResponseDto> getOrderItemsByOrderId(Authentication authentication,
                                                      Long orderId, Pageable pageable);

    OrderItemResponseDto getOrderItemByIdAndOrderId(Authentication authentication,
                                                    Long orderId, Long orderItemId);
}
