package com.example.demo.dto.shoppingcard.cartitem;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CartItemUpdateDataRequestDto {
    @Min(0)
    private int quantity;
}
