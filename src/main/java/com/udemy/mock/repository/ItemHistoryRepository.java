package com.udemy.mock.repository;

import com.udemy.mock.entity.ItemHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemHistoryRepository extends JpaRepository<ItemHistory, Long> {

    List<ItemHistory> findByOrderHistoryIdIn(List<Long> orderHistoryIds);
}
