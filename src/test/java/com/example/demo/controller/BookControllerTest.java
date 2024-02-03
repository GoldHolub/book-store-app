package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.CreateBookRequestDto;
import com.example.demo.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import lombok.SneakyThrows;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Sql(scripts = {"classpath:database/books/delete-books-and-category-from-db.sql",
        "classpath:database/books/delete-categories-from-db.sql",
        "classpath:database/books/add-books-and-category-to-db.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:database/books/delete-books-and-category-from-db.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    private static MockMvc mockMvc;
    @Autowired
    private BookController bookController;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BookService bookService;

    @BeforeAll
    public static void applySecurity(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Sql(scripts = "classpath:database/books/delete-added-book-from-db.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void createBook_ValidCreateBookRequestDto_ok() throws Exception {
        CreateBookRequestDto bookRequestDto = new CreateBookRequestDto();
        bookRequestDto.setAuthor("Max");
        bookRequestDto.setTitle("new test adventure");
        bookRequestDto.setPrice(BigDecimal.valueOf(57.5));
        bookRequestDto.setCategoryIds(Set.of(1L));
        bookRequestDto.setIsbn("978-0-061-96436-7");

        BookDto expected = new BookDto();
        expected.setAuthor(bookRequestDto.getAuthor());
        expected.setTitle(bookRequestDto.getTitle());
        expected.setPrice(bookRequestDto.getPrice());
        expected.setCategoryIds(bookRequestDto.getCategoryIds());
        expected.setIsbn(bookRequestDto.getIsbn());
        expected.setId(3L);

        String jsonRequest = objectMapper.writeValueAsString(bookRequestDto);

        MvcResult mvcResult = mockMvc.perform(post("/api/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        BookDto actual = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertEquals(expected, actual);
    }

    @SneakyThrows
    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void getAll_givenBooksInCatalog_ShouldReturnAllProducts() {
        List<BookDto> expected = createTwoExistingBooks();

        MvcResult mvcResult = mockMvc.perform(get("/api/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        BookDto[] actual = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), BookDto[].class);

        assertEquals(2, actual.length);
        assertEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void updateBook_validBook_ok() throws Exception {
        CreateBookRequestDto bookRequestDto = new CreateBookRequestDto();
        bookRequestDto.setAuthor("John");
        bookRequestDto.setTitle("new Updated book 2");
        bookRequestDto.setPrice(BigDecimal.valueOf(15));
        bookRequestDto.setCategoryIds(Set.of(1L));
        bookRequestDto.setIsbn("978-0-596-52068-7");

        BookDto expected = new BookDto();
        expected.setAuthor(bookRequestDto.getAuthor());
        expected.setTitle(bookRequestDto.getTitle());
        expected.setPrice(bookRequestDto.getPrice());
        expected.setCategoryIds(bookRequestDto.getCategoryIds());
        expected.setIsbn(bookRequestDto.getIsbn());
        expected.setId(2L);

        String jsonRequest = objectMapper.writeValueAsString(bookRequestDto);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/books/2")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        BookDto actual = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void updateBook_invalidBook_notOk() throws Exception {
        CreateBookRequestDto bookRequestDto = new CreateBookRequestDto();
        bookRequestDto.setAuthor("John");

        String jsonRequest = objectMapper.writeValueAsString(bookRequestDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/books/2")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void getBookById_validId_ok() throws Exception {
        BookDto expected = createTwoExistingBooks().get(1);

        MvcResult mvcResult = mockMvc.perform(get("/api/books/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        BookDto actual = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void getBookById_invalidId_notOk() throws Exception {
        mockMvc.perform(get("/api/books/152")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void search_getBooksByPriceFrom5To12_ListWithOneBook() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/books/search")
                        .param("minPrice", "5")
                        .param("maxPrice", "12")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        BookDto[] actual = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), BookDto[].class);

        assertEquals(1, actual.length);
    }

    private List<BookDto> createTwoExistingBooks() {
        BookDto bookDto1 = new BookDto();
        bookDto1.setAuthor("Mark");
        bookDto1.setTitle("new book 1");
        bookDto1.setPrice(BigDecimal.valueOf(10));
        bookDto1.setCategoryIds(Set.of(1L));
        bookDto1.setIsbn("978-0-545-01022-1");
        bookDto1.setId(1L);

        BookDto bookDto2 = new BookDto();
        bookDto2.setAuthor("John");
        bookDto2.setTitle("new book 2");
        bookDto2.setPrice(BigDecimal.valueOf(15));
        bookDto2.setCategoryIds(Set.of(1L));
        bookDto2.setIsbn("978-0-596-52068-7");
        bookDto2.setId(2L);

        List<BookDto> expected = new ArrayList<>();
        expected.add(bookDto1);
        expected.add(bookDto2);
        return expected;
    }
}
