package com.udemy.mock.scheduler;

import com.udemy.mock.entity.CustomerTotalAmount;
import com.udemy.mock.entity.ItemHistory;
import com.udemy.mock.entity.OrderHistory;
import com.udemy.mock.repository.CustomerTotalAmountRepository;
import com.udemy.mock.repository.ItemHistoryRepository;
import com.udemy.mock.repository.OrderHistoryRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

@EnableScheduling
@Configuration
@RequiredArgsConstructor
@Service
@Slf4j
public class CalculateTotalNAVScheduler {

    private final OrderHistoryRepository orderHistoryRepository;
    private final ItemHistoryRepository itemHistoryRepository;
    private final CustomerTotalAmountRepository customerTotalAmountRepository;

    //    @Scheduled(cron = "* 5 5 * * *")
    public void jobNav() {

        if (customerTotalAmountRepository.existsByCreationDateAfter(LocalDate.now().atStartOfDay())) {
            return;
        }
        List<CustomerTotalAmount> customerTotalAmountList = customerTotalAmountRepository.findAll();
        Map<Long, CustomerTotalAmount> customerIdMap = customerTotalAmountList.stream()
            .collect(Collectors.toMap(CustomerTotalAmount::getCustomerId, Function.identity()));
        Map<Long, Integer> totalNavTodayOfCustId = this.getTotalNav();

        List<CustomerTotalAmount> updateList = this.addTotalToday(totalNavTodayOfCustId, customerIdMap);
        log.info("Test");
        customerTotalAmountRepository.saveAll(updateList);
    }

    private Map<Long, Integer> getTotalNav() {

        Map<Long, Integer> totalNavTodayOfCustId = new HashMap<>();
        List<OrderHistory> orderHistoryList = orderHistoryRepository.findByOrderDateBetween(
            LocalDate.now().atStartOfDay(),
            LocalTime.MAX.atDate(LocalDate.now()));
        List<Long> orderHistoryIds = orderHistoryList.stream().map(OrderHistory::getId).collect(Collectors.toList());
        List<ItemHistory> itemHistories = itemHistoryRepository.findByOrderHistoryIdIn(orderHistoryIds);
        Map<Long, List<ItemHistory>> itemHistMap = itemHistories.stream()
            .collect(Collectors.groupingBy(ItemHistory::getOrderHistoryId));
        orderHistoryList.forEach(orderHistory -> {
            int total = itemHistMap.get(orderHistory.getId()).stream()
                .mapToInt(itemHistory -> itemHistory.getPrice() * itemHistory.getQuantity()).sum();
            Long custId = orderHistory.getCustomerId();
            if (totalNavTodayOfCustId.containsKey(custId)) {
                totalNavTodayOfCustId.put(custId,
                    totalNavTodayOfCustId.get(custId) + total);
            } else {
                totalNavTodayOfCustId.put(custId, total);
            }
        });
        return totalNavTodayOfCustId;
    }

    private List<CustomerTotalAmount> addTotalToday(Map<Long, Integer> totalNavTodayOfCustId,
        Map<Long, CustomerTotalAmount> customerIdMap) {

        List<CustomerTotalAmount> updateList = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : totalNavTodayOfCustId.entrySet()) {
            if (customerIdMap.containsKey(entry.getKey())) {
                CustomerTotalAmount update = new CustomerTotalAmount();
                BeanUtils.copyProperties(customerIdMap.get(entry.getKey()), update);
                update.setTotal(update.getTotal() + entry.getValue());
                updateList.add(update);
            } else {
                updateList.add(CustomerTotalAmount.builder()
                    .customerId(entry.getKey())
                    .total(entry.getValue())
                    .build());
            }
        }
        return updateList;
    }
}
