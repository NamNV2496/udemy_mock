package com.udemy.mock.repository;

import com.udemy.mock.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByUserName(String userName);

    boolean existsById(Long id);
}
