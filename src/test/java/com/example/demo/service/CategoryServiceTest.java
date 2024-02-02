package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.demo.dto.category.CategoryDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.model.Category;
import com.example.demo.repository.category.CategoryRepository;
import com.example.demo.service.impl.CategoryServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryMapper categoryMapper;
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void save_validCategory_ok() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("fiction");

        when(categoryMapper.toModel(categoryDto)).thenReturn(new Category());
        when(categoryRepository.save(any())).thenReturn(new Category());

        CategoryDto actual = categoryService.save(categoryDto);
        CategoryDto expected = new CategoryDto();
        expected.setName(categoryDto.getName());

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void update_validId_ok() {
        Long categoryId = 1L;
        CategoryDto expected = new CategoryDto();
        expected.setName("Horror");

        Category category = new Category();
        expected.setName("Horror");
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(new Category()));
        when(categoryMapper.toModel(any())).thenReturn(category);
        when(categoryRepository.save(any())).thenReturn(new Category());

        CategoryDto actual = categoryService.update(categoryId, expected);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void update_invalidId_notOk() {
        Long invalidCategoryId = 99L;
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("new category");
        when(categoryRepository.findById(invalidCategoryId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> categoryService.update(invalidCategoryId, categoryDto));
    }

    @Test
    void getById_validId_ok() {
        Long categoryId = 1L;
        CategoryDto expectedCategoryDto = new CategoryDto();
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(new Category()));
        when(categoryMapper.toDto(any())).thenReturn(expectedCategoryDto);

        CategoryDto result = categoryService.getById(categoryId);

        assertNotNull(result);
        assertEquals(expectedCategoryDto, result);
    }

    @Test
    void getById_invalidId_notOk() {
        Long invalidCategoryId = 99L;
        when(categoryRepository.findById(invalidCategoryId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> categoryService.getById(invalidCategoryId));
    }
}
