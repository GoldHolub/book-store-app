package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.category.CategoryDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Sql(scripts = {"classpath:database/shoppingCart/delete-books-and-shoppingCart-from-db.sql",
                "classpath:database/books/delete-books-and-category-from-db.sql",
                "classpath:database/books/delete-categories-from-db.sql",},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
    private static MockMvc mockMvc;
    @Autowired
    private BookController bookController;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    public static void applySecurity(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Sql(scripts = {"classpath:database/books/delete-categories-from-db.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void createCategory_validCategory_ok() throws Exception {
        CategoryDto expected = new CategoryDto();
        expected.setName("Horror new");
        expected.setDescription("Horror book");

        String jsonRequest = objectMapper.writeValueAsString(expected);

        MvcResult mvcResult = mockMvc.perform(post("/api/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        CategoryDto actual = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), CategoryDto.class);

        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Sql(scripts = "classpath:database/books/add-categories-to-db.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/delete-categories-from-db.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void findAll_givenCategoriesInCatalog_ShouldReturnAllCategories() throws Exception {
        List<CategoryDto> expected = createThreeExistingCategories();
        MvcResult mvcResult = mockMvc.perform(get("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        CategoryDto[] actual = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), CategoryDto[].class);

        assertEquals(3, actual.length);
        assertEquals(expected, Arrays.stream(actual).toList());
    }

    @Sql(scripts = "classpath:database/books/add-categories-to-db.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/delete-categories-from-db.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(authorities = {"ADMIN"})
    @Test
    void updateCategory_validCategory_ok() throws Exception {
        CategoryDto expected = new CategoryDto();
        expected.setName("new updated Category");

        String jsonRequest = objectMapper.writeValueAsString(expected);

        MvcResult mvcResult = mockMvc.perform(put("/api/categories/1")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), CategoryDto.class);

        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Sql(scripts = "classpath:database/books/add-categories-to-db.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/delete-categories-from-db.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(authorities = {"ADMIN"})
    @Test
    void updateCategory_invalidCategory_notOk() throws Exception {
        CategoryDto expected = new CategoryDto();
        expected.setName("new updated Category");

        String jsonRequest = objectMapper.writeValueAsString(expected);

        mockMvc.perform(post("/api/categories/256")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Sql(scripts = "classpath:database/books/add-books-and-category-to-db.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/delete-books-and-category-from-db.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(authorities = {"ADMIN"})
    @Test
    void getBooksByCategoryId_validCategoryId_ShouldReturnAllBooks() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/categories/1/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        BookDto[] actual = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), BookDto[].class);

        assertEquals(2, actual.length);
    }

    @Sql(scripts = "classpath:database/books/add-books-and-category-to-db.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/delete-books-and-category-from-db.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(authorities = {"ADMIN"})
    @Test
    void getBooksByCategoryId_invalidCategoryId_ShouldReturnEmptyList() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/categories/4/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        BookDto[] actual = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), BookDto[].class);

        assertEquals(0, actual.length);
    }

    @Sql(scripts = "classpath:database/books/add-categories-to-db.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/delete-categories-from-db.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(authorities = {"ADMIN"})
    @Test
    void getCategoryById_validId_ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/categories/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        CategoryDto expected = createThreeExistingCategories().get(1);
        CategoryDto actual = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), CategoryDto.class);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    private List<CategoryDto> createThreeExistingCategories() {
        CategoryDto categoryDto1 = new CategoryDto();
        categoryDto1.setName("Fiction new");
        CategoryDto categoryDto2 = new CategoryDto();
        categoryDto2.setName("Fantasy new");
        CategoryDto categoryDto3 = new CategoryDto();
        categoryDto3.setName("Detective new");

        List<CategoryDto> expected = new ArrayList<>();
        expected.add(categoryDto1);
        expected.add(categoryDto2);
        expected.add(categoryDto3);
        return expected;
    }
}
