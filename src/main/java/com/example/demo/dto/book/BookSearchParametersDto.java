package com.example.demo.dto.book;

import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class BookSearchParametersDto {
    private String[] authors;
    private String[] titles;
    @Min(0)
    private BigDecimal minPrice;
    @Min(0)
    private BigDecimal maxPrice;
}
