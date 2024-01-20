package com.example.demo.repository.shoppingcart;

import com.example.demo.model.Book;
import com.example.demo.model.CartItem;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CartItemRepository extends JpaRepository<CartItem, Long>,
                                            JpaSpecificationExecutor<Book> {
    Set<CartItem> findByShoppingCartId(Long shoppingCartId);

    List<CartItem> findByShoppingCartId(Long shoppingCartId, Pageable pageable);
}
