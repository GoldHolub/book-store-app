package com.example.demo.mapper;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.category.CategoryDto;
import com.example.demo.dto.category.CategoryResponseDto;
import com.example.demo.model.Category;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    CategoryResponseDto toResponseDto(Category category);

    Category toModel(CategoryDto categoryDto);
}
