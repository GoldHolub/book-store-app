package com.example.demo.mapper;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.order.OrderResponseDto;
import com.example.demo.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(source = "order.user.id", target = "userId")
    @Mapping(source = "order.orderItems", target = "orderItems")
    OrderResponseDto toDto(Order order);
}
