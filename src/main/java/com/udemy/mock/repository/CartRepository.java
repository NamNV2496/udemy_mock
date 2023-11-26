package com.udemy.mock.repository;

import com.udemy.mock.entity.Cart;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findByCustomerId(Long customerId);
}
