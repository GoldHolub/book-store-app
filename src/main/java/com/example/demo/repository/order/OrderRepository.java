package com.example.demo.repository.order;

import com.example.demo.model.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findOrdersByUserId(Long userId, Pageable pageable);

    Optional<Order> findOrderById(Long orderId);
}
