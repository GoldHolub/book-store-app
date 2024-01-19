package com.example.demo.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import org.hibernate.validator.constraints.ISBN;
import org.hibernate.validator.constraints.UniqueElements;

@Data
public class CreateBookRequestDto {
    @NotBlank
    private String title;
    @NotBlank
    private String author;
    @ISBN
    @NotNull
    private String isbn;
    @Min(0)
    private BigDecimal price;
    private String description;
    private String coverImage;
    @UniqueElements
    private Set<Long> categoryIds = new HashSet<>();
}
