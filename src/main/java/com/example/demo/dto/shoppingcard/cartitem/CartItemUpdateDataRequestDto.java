package com.example.demo.dto.shoppingcard.cartitem;

import jakarta.validation.constraints.Min;

public record CartItemUpdateDataRequestDto(
        @Min(0)
        int quantity
) {
}
