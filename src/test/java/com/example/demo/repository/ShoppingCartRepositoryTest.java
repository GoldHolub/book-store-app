package com.example.demo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.demo.model.ShoppingCart;
import com.example.demo.repository.shoppingcart.ShoppingCartRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = {"classpath:database/shoppingCart/delete-books-and-shoppingCart-from-db.sql",
                "classpath:database/shoppingCart/add-books-to-shoppingCart-and-db.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:database/shoppingCart/delete-books-and-shoppingCart-from-db.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShoppingCartRepositoryTest {
    private static final Long VALID_USER_ID = 1L;
    private static final Long INVALID_USER_ID = -1L;
    private static final int CART_ITEMS_AMOUNT = 2;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    void findByUserId_validUserId_ok() {
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository
                .findByUserId(VALID_USER_ID);
        assertTrue(optionalShoppingCart.isPresent());
        assertEquals(VALID_USER_ID, optionalShoppingCart.get().getUser().getId());
        assertEquals(CART_ITEMS_AMOUNT, optionalShoppingCart.get().getCartItems().size());
    }

    @Test
    void findByUserId_invalidUserId_EmptyOptional() {
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository
                .findByUserId(INVALID_USER_ID);
        assertFalse(optionalShoppingCart.isPresent());
    }
}
