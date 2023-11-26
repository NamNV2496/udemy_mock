package com.udemy.mock.repository;

import com.udemy.mock.entity.OrderHistory;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {

    List<OrderHistory> findByOrderDateBetweenAndCustomerId(LocalDateTime start, LocalDateTime end, Long customerId);

    List<OrderHistory> findByOrderDateBetween(LocalDateTime start, LocalDateTime end);
}
