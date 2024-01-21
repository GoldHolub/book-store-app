package com.example.demo.dto.order.orderitem;

import lombok.Data;

@Data
public class OrderItemResponseDto {
    private Long id;
    private Long bookId;
    private int quantity;
}
