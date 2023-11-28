package com.udemy.mock.repository;

import com.udemy.mock.entity.Item;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByIdIn(List<Long> ids);

    boolean existsByItemCode(String itemCode);
}
