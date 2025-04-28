package com.bubble.bubbleforprofessor.cart.repository;

import com.bubble.bubbleforprofessor.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
