package com.udemy.mock.repository;

import com.udemy.mock.entity.ItemOrder;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemOrderRepository extends JpaRepository<ItemOrder, Long> {

    List<ItemOrder> findByOrderId(Long orderId);
}
