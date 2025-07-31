package com.substring.foodies.repository;

import com.substring.foodies.entity.Cart;
import com.substring.foodies.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByCreator(User creator);
}
