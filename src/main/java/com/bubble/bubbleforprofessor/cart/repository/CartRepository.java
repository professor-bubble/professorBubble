package com.bubble.bubbleforprofessor.cart.repository;

import com.bubble.bubbleforprofessor.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
