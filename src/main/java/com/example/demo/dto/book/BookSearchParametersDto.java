package com.example.demo.dto.book;

import java.math.BigDecimal;

public record BookSearchParametersDto(String[] authors,
                                      String[] titles, BigDecimal minPrice, BigDecimal maxPrice) {
}
