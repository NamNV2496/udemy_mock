package com.udemy.mock.repository;

import com.udemy.mock.entity.CustomerTotalAmount;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerTotalAmountRepository extends JpaRepository<CustomerTotalAmount, Long> {

    boolean existsByCreationDateAfter(LocalDateTime start);
}
