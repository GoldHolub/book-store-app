package com.example.demo.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;
import org.hibernate.validator.constraints.ISBN;

@Data
public class CreateBookRequestDto {
    @NotBlank
    @NotNull
    private String title;
    @NotBlank
    @NotNull
    private String author;
    @ISBN
    @NotNull
    private String isbn;
    @Min(0)
    private BigDecimal price;
    private String description;
    private String coverImage;
}
