package com.example.demo.mapper;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.order.orderitem.OrderItemResponseDto;
import com.example.demo.model.CartItem;
import com.example.demo.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    OrderItemResponseDto toDto(OrderItem orderItem);

    @Mapping(source = "cartItem.book.price", target = "price")
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    OrderItem toModelFromCartItem(CartItem cartItem);
}
