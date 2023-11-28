package com.udemy.mock.service;

import com.udemy.mock.dto.request.OrderRequest.ItemElement;
import com.udemy.mock.dto.response.OrderHistoryResponse;
import com.udemy.mock.dto.response.OrderHistoryResponse.OrderHistoryElement;
import com.udemy.mock.entity.ItemHistory;
import com.udemy.mock.entity.OrderHistory;
import com.udemy.mock.repository.ItemHistoryRepository;
import com.udemy.mock.repository.OrderHistoryRepository;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderHistoryService {

    private DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final OrderHistoryRepository orderHistoryRepository;
    private final ItemHistoryRepository itemHistoryRepository;

    public OrderHistoryResponse getOrderHistory(String date, Long customerId) {

        LocalDate localDate = LocalDate.parse(date, DATE_FORMATTER);
        List<OrderHistory> orderHistoryList = orderHistoryRepository.findByOrderDateBetweenAndCustomerId(
            localDate.atStartOfDay(),
            LocalTime.MAX.atDate(localDate),
            customerId);
        List<Long> orderHistoryIds = orderHistoryList.stream().map(OrderHistory::getId).collect(Collectors.toList());
        List<ItemHistory> itemHistories = itemHistoryRepository.findByOrderHistoryIdIn(orderHistoryIds);
        Map<Long, List<ItemHistory>> itemHistMap =
            itemHistories.stream().collect(Collectors.groupingBy(ItemHistory::getOrderHistoryId));
        OrderHistoryResponse response = new OrderHistoryResponse();
        List<OrderHistoryElement> historyElements = new ArrayList<>();
        orderHistoryList.forEach(orderHistory -> {
            OrderHistoryElement element = new OrderHistoryElement();
            BeanUtils.copyProperties(orderHistory, element);

            List<ItemElement> items = new ArrayList<>();
            itemHistMap.get(orderHistory.getId()).forEach(itemHistory -> {
                ItemElement itemElement = new ItemElement();
                BeanUtils.copyProperties(itemHistory, itemElement);
                items.add(itemElement);
            });
            element.setItems(items);
            historyElements.add(element);
        });
        response.setHistoryElements(historyElements);
        return response;
    }
}
