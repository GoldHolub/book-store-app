package com.example.demo.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.example.demo.dto.shoppingcard.cartitem.CartItemRequestDto;
import com.example.demo.dto.shoppingcard.cartitem.CartItemUpdateDataRequestDto;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Sql(scripts = "classpath:database/shoppingCart/delete-books-and-shoppingCart-from-db.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
public class ShoppingCartControllerTest {
    private static final int AMOUNT_OF_BOOKS = 2;
    private static final Long VALID_USER_ID = 1L;
    private static final Long INVALID_USER_ID = 2L;
    private static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    public static void applySecurity(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Sql(scripts = "classpath:database/shoppingCart/add-books-to-shoppingCart-and-db.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/shoppingCart/delete-books-and-shoppingCart-from-db.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void getUserShoppingCart_validUser_ok() throws Exception {
        Authentication authentication = generateCustomAuthentication(VALID_USER_ID);

        mockMvc.perform(get("/api/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(authentication(authentication)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.cartItems.length()").value(AMOUNT_OF_BOOKS));
    }

    @Sql(scripts = "classpath:database/shoppingCart/add-books-to-shoppingCart-and-db.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/shoppingCart/delete-books-and-shoppingCart-from-db.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void getUserShoppingCart_invalidUser_notOk() throws Exception {
        Authentication authentication = generateCustomAuthentication(INVALID_USER_ID);

        mockMvc.perform(get("/api/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(authentication(authentication)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Sql(scripts = "classpath:database/shoppingCart/add-books-to-shoppingCart-and-db.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/shoppingCart/delete-books-and-shoppingCart-from-db.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void addBookToShoppingCart_validBook_ok() throws Exception {
        Authentication authentication = generateCustomAuthentication(VALID_USER_ID);
        CartItemRequestDto expected = new CartItemRequestDto();
        expected.setBookId(1L);
        expected.setQuantity(AMOUNT_OF_BOOKS);

        String jsonRequest = objectMapper.writeValueAsString(expected);

        mockMvc.perform(post("/api/cart")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(authentication(authentication)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.bookId").value(expected.getBookId()))
                .andExpect(jsonPath("$.quantity").value(expected.getQuantity()));
    }

    @Sql(scripts = "classpath:database/shoppingCart/add-books-to-shoppingCart-and-db.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/shoppingCart/delete-books-and-shoppingCart-from-db.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void addBookToShoppingCart_invalidBook_notOk() throws Exception {
        Authentication authentication = generateCustomAuthentication(VALID_USER_ID);
        CartItemRequestDto expected = new CartItemRequestDto();
        expected.setBookId(1000L);
        expected.setQuantity(AMOUNT_OF_BOOKS);

        String jsonRequest = objectMapper.writeValueAsString(expected);

        mockMvc.perform(post("/api/cart")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(authentication(authentication)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Sql(scripts = "classpath:database/shoppingCart/add-books-to-shoppingCart-and-db.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/shoppingCart/delete-books-and-shoppingCart-from-db.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void updateBookQuantityInShoppingCart_validRequest_ok() throws Exception {
        Authentication authentication = generateCustomAuthentication(VALID_USER_ID);
        CartItemUpdateDataRequestDto expected = new CartItemUpdateDataRequestDto();
        expected.setQuantity(5);

        String jsonRequest = objectMapper.writeValueAsString(expected);

        mockMvc.perform(put("/api/cart/cart-items/1")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(authentication(authentication)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.quantity").value(expected.getQuantity()));
    }

    @Sql(scripts = "classpath:database/shoppingCart/add-books-to-shoppingCart-and-db.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/shoppingCart/delete-books-and-shoppingCart-from-db.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void updateBookQuantityInShoppingCart_invalidRequest_notOk() throws Exception {
        Authentication authentication = generateCustomAuthentication(VALID_USER_ID);
        CartItemUpdateDataRequestDto expected = new CartItemUpdateDataRequestDto();
        expected.setQuantity(-50);

        String jsonRequest = objectMapper.writeValueAsString(expected);

        mockMvc.perform(put("/api/cart/cart-items/1")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(authentication(authentication)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Sql(scripts = "classpath:database/shoppingCart/add-books-to-shoppingCart-and-db.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/shoppingCart/delete-books-and-shoppingCart-from-db.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void deleteBookFromTheShoppingCart_validCartItemId_ok() throws Exception {
        Authentication authentication = generateCustomAuthentication(VALID_USER_ID);

        mockMvc.perform(delete("/api/cart/cart-items/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(authentication(authentication)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Sql(scripts = "classpath:database/shoppingCart/add-books-to-shoppingCart-and-db.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/shoppingCart/delete-books-and-shoppingCart-from-db.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void deleteBookFromTheShoppingCart_invalidCartItemId_notOk() throws Exception {
        Authentication authentication = generateCustomAuthentication(VALID_USER_ID);

        mockMvc.perform(delete("/api/cart/cart-items/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(authentication(authentication)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    private Authentication generateCustomAuthentication(Long userId) {
        Role adminRole = new Role(Role.RoleName.ADMIN);
        User user = new User();
        user.setId(userId);
        user.setRoles(Set.of(adminRole));
        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }
}
