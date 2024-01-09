package com.example.demo.dto;

import java.math.BigDecimal;

public record BookSearchParametersDto(String[] authors,
                                      String[] titles, BigDecimal minPrice, BigDecimal maxPrice) {
}
