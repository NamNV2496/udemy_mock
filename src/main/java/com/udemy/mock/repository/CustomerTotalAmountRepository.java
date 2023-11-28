package com.udemy.mock.repository;

import com.udemy.mock.entity.CustomerTotalAmount;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerTotalAmountRepository extends JpaRepository<CustomerTotalAmount, Long> {

    boolean existsByLastModifiedDateAfter(LocalDateTime end);

    String QUERY_STR = "WITH totalCustomer AS (SELECT * FROM mock_customer mc) "
        + " "
        + " "
        + "SELECT "
        + "    mc.id as id, "
        + "    COALESCE(mcta.total, 0) + COALESCE(SUM(mih.price * mih.quantity), 0) AS total "
        + "FROM "
        + "    totalCustomer mc "
        + "LEFT JOIN "
        + "    mock_customer_total_amount mcta ON mcta.customer_id = mc.id "
        + "LEFT JOIN "
        + "    mock_order_history moh ON moh.customer_id = mc.id "
        + "LEFT JOIN "
        + "    mock_item_history mih ON moh.id = mih.order_history_id "
        + "WHERE "
        + "    DATE(moh.created_date) = CURDATE() OR moh.id IS NULL "
        + "GROUP BY "
        + "    mc.id, mcta.total;";

    @Query(value = QUERY_STR, nativeQuery = true)
    List<Map<Object, Object>> getTotalAmount();


    String TOTAL_AMOUNT_EACH_DAY = "select "
        + "     mc.id as id, "
        + "     coalesce(SUM(mih.price * mih.quantity)) AS total "
        + "from "
        + "     mock_customer mc  "
        + "left join  "
        + "     mock_customer_total_amount mcta on mcta.customer_id = mc.id  "
        + "left join  "
        + "     mock_order_history moh on moh.customer_id = mc.id  "
        + "left join  "
        + "     mock_item_history mih on moh.id = mih.order_history_id  "
        + "where  "
        + "     DATE(moh.created_date) = CURDATE()  "
        + "GROUP BY  "
        + "     mc.id;";
    @Query(value = TOTAL_AMOUNT_EACH_DAY, nativeQuery = true)
    List<Map<Object, Object>> getTotalAmountEachDay();
}
