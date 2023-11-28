package com.udemy.mock.scheduler;

import com.udemy.mock.entity.Customer;
import com.udemy.mock.entity.CustomerTotalAmount;
import com.udemy.mock.entity.ItemHistory;
import com.udemy.mock.entity.OrderHistory;
import com.udemy.mock.repository.CustomerRepository;
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
import org.springframework.transaction.annotation.Transactional;

@EnableScheduling
@Configuration
@RequiredArgsConstructor
@Service
@Slf4j
public class CalculateTotalNAVScheduler {

    private final OrderHistoryRepository orderHistoryRepository;
    private final CustomerRepository customerRepository;
    private final ItemHistoryRepository itemHistoryRepository;
    private final CustomerTotalAmountRepository customerTotalAmountRepository;

    //    @Scheduled(cron = "* 5 5 * * *")
    @Transactional
    public void jobNav() {

        if (customerTotalAmountRepository.existsByLastModifiedDateAfter(LocalDate.now().atStartOfDay())) {
            log.info("Đã tính toán NAV ngày hôm nay rồi. Không thực hiện lại!");
            return;
        }
        List<CustomerTotalAmount> customerTotalAmountList = customerTotalAmountRepository.findAll();
        Map<Long, CustomerTotalAmount> customerIdMap = customerTotalAmountList.stream()
            .collect(Collectors.toMap(CustomerTotalAmount::getCustomerId, Function.identity()));
        Map<Long, Integer> totalNavTodayOfCustId = this.getTotalNav();
        List<CustomerTotalAmount> updateList = this.addTotalToday(totalNavTodayOfCustId, customerIdMap);

        // update customerRank
        Map<Long, Integer> totalAmountOfAccount = updateList.stream()
            .collect(Collectors.toMap(CustomerTotalAmount::getCustomerId, CustomerTotalAmount::getTotal));
        this.updateRank(totalAmountOfAccount);
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

    @Transactional
    public void jobNavWay2() {

        if (customerTotalAmountRepository.existsByLastModifiedDateAfter(LocalDate.now().atStartOfDay())) {
            log.info("Đã tính toán NAV ngày hôm nay rồi. Không thực hiện lại!");
            return;
        }
        List<CustomerTotalAmount> customerTotalAmountList = customerTotalAmountRepository.findAll();
        Map<Long, CustomerTotalAmount> customerIdMap = customerTotalAmountList.stream()
            .collect(Collectors.toMap(CustomerTotalAmount::getCustomerId, Function.identity()));

        List<CustomerTotalAmount> updateList = this.calculateTotalAmountAccountQueryNative(customerIdMap);
        customerTotalAmountRepository.saveAll(updateList);
    }

    public List<CustomerTotalAmount> calculateTotalAmountAccountQueryNative(
        Map<Long, CustomerTotalAmount> customerIdMap) {

        List<CustomerTotalAmount> updateList = new ArrayList<>();
        Map<Long, Integer> totalAmountOfAccount = new HashMap<>();

        List<Map<Object, Object>> returnMap = customerTotalAmountRepository.getTotalAmount();

        returnMap.forEach(map -> {
            log.info("key: {} value: {}", map.get("id"), map.get("total"));
            long customerId = Long.parseLong(map.get("id").toString());
            int total = Integer.parseInt(map.get("total").toString());
            totalAmountOfAccount.put(customerId, total);
            if (customerIdMap.containsKey(customerId)) {
                CustomerTotalAmount update = new CustomerTotalAmount();
                BeanUtils.copyProperties(customerIdMap.get(customerId), update);
                update.setTotal(total);
                updateList.add(update);
            } else {
                updateList.add(CustomerTotalAmount.builder()
                    .customerId(customerId)
                    .total(total)
                    .build());
            }
        });
        // update customerRank
        this.updateRank(totalAmountOfAccount);
        return updateList;
    }

    private void updateRank(Map<Long, Integer> totalAmountOfAccount) {

        List<Customer> customers = customerRepository.findAll();
        List<Customer> customerUpdateList = new ArrayList<>();
        Map<Long, String> listUpgradeRank2 = new HashMap<>();
        Map<Long, String> listUpgradeRank3 = new HashMap<>();

        Map<Long, Customer> customerMap = customers.stream()
            .collect(Collectors.toMap(Customer::getId, Function.identity()));
        for (Map.Entry<Long, Integer> entry : totalAmountOfAccount.entrySet()) {
            if (entry.getValue() > 1000000
                && customerMap.get(entry.getKey()).getCustomerRank() != 2) {
                Customer customerUpdate = new Customer();
                BeanUtils.copyProperties(customerMap.get(entry.getKey()), customerUpdate);
                customerUpdate.setCustomerRank(2);
                customerUpdateList.add(customerUpdate);
                listUpgradeRank2.put(customerUpdate.getId(), customerUpdate.getUserName());
            } else if (entry.getValue() > 5000000
                && customerMap.get(entry.getKey()).getCustomerRank() != 3) {
                Customer customerUpdate = new Customer();
                BeanUtils.copyProperties(customerMap.get(entry.getKey()), customerUpdate);
                customerUpdate.setCustomerRank(3);
                customerUpdateList.add(customerUpdate);
                listUpgradeRank3.put(customerUpdate.getId(), customerUpdate.getUserName());
            }
        }

        log.info("list of customer upgrade to level 2: {}", listUpgradeRank2);
        log.info("list of customer upgrade to level 3: {}", listUpgradeRank3);
        customerRepository.saveAll(customerUpdateList);
    }

    public Map<Long, Integer> calculateTotalAmountAccountEachDayQueryNative() {

        Map<Long, Integer> ret = new HashMap<>();
        List<Map<Object, Object>> returnMap = customerTotalAmountRepository.getTotalAmountEachDay();

        returnMap.forEach(map -> {
            long customerId = Long.parseLong(map.get("id").toString());
            int total = Integer.parseInt(map.get("total").toString());
            ret.put(customerId, total);
        });
        log.info("NAV each day: {}", ret);
        return ret;
    }
}
