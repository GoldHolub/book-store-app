package com.example.demo.controller;

import com.example.demo.dto.order.OrderResponseDto;
import com.example.demo.dto.order.OrderStatusRequestDto;
import com.example.demo.dto.order.OrderSubmissionRequestDto;
import com.example.demo.dto.order.orderitem.OrderItemResponseDto;
import com.example.demo.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/orders")
@RequiredArgsConstructor
@RestController
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Submit order", description = "submit current order")
    @PostMapping
    public OrderResponseDto submitOrder(Authentication authentication, Pageable pageable,
                                        @RequestBody @Valid OrderSubmissionRequestDto requestDto) {
        return orderService.createOrder(authentication, pageable, requestDto);
    }

    @GetMapping
    public List<OrderResponseDto> getOrders(Authentication authentication, Pageable pageable) {
        return orderService.getOrders(authentication, pageable);
    }

    @PatchMapping("/{orderId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public OrderResponseDto updateOrderStatus(@PathVariable Long orderId,
                                             @RequestBody @Valid OrderStatusRequestDto requestDto) {
        return orderService.updateOrderStatus(orderId, requestDto);
    }

    @GetMapping("/{orderId}/items")
    public List<OrderItemResponseDto> getOrderItemsByOrderId(Authentication authentication,
                                                               @PathVariable Long orderId,
                                                               Pageable pageable) {
        return orderService.getOrderItemsByOrderId(authentication, orderId, pageable);
    }

    @GetMapping("/{orderId}/items/{orderItemId}")
    public OrderItemResponseDto getOrderItemByIdAndOrderId(Authentication authentication,
                                                           @PathVariable Long orderId,
                                                           @PathVariable Long orderItemId) {
        return orderService.getOrderItemByIdAndOrderId(authentication, orderId, orderItemId);
    }
}
