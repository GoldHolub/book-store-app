package com.example.demo.controller;

import com.example.demo.dto.order.OrderResponseDto;
import com.example.demo.dto.order.OrderSubmissionRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/orders")
@RequiredArgsConstructor
@RestController
public class OrderController {
    @Operation(summary = "Submit order", description = "submit current order")
    @PostMapping
    public OrderResponseDto submitOrder(@RequestBody @Valid OrderSubmissionRequestDto requestDto) {
        return null;
    }
}
