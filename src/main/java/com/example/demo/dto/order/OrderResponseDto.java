package com.example.demo.dto.order;

import com.example.demo.dto.order.orderitem.OrderItemResponseDto;
import com.example.demo.model.Order;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class OrderResponseDto {
    private Long id;
    private Long userId;
    private List<OrderItemResponseDto> orderItems;
    private LocalDateTime orderDate;
    private BigDecimal total;
    private Order.Status status;
}
