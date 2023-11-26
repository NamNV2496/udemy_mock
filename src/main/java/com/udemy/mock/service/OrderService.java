package com.udemy.mock.service;

import com.udemy.mock.constant.enums.OrderStatus;
import com.udemy.mock.dto.request.OrderRequest;
import com.udemy.mock.dto.request.OrderRequest.ItemElement;
import com.udemy.mock.entity.Item;
import com.udemy.mock.entity.ItemHistory;
import com.udemy.mock.entity.ItemOrder;
import com.udemy.mock.entity.Order;
import com.udemy.mock.entity.OrderHistory;
import com.udemy.mock.repository.CustomerRepository;
import com.udemy.mock.repository.ItemHistoryRepository;
import com.udemy.mock.repository.ItemOrderRepository;
import com.udemy.mock.repository.ItemRepository;
import com.udemy.mock.repository.OrderHistoryRepository;
import com.udemy.mock.repository.OrderRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ItemOrderRepository itemOrderRepository;
    private final ItemRepository itemRepository;
    private final OrderHistoryRepository orderHistoryRepository;
    private final ItemHistoryRepository itemHistoryRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public Long createOrder(OrderRequest orderRequest) {

        if (!customerRepository.existsById(orderRequest.getCustomerId())) {
            throw new RuntimeException("CustomerId isn't exist");
        }
        List<Long> itemIds = orderRequest.getItems().stream().map(ItemElement::getItemId).collect(Collectors.toList());
        List<Item> items = itemRepository.findByIdIn(itemIds);
        if (itemIds.size() != items.size()) {
            throw new RuntimeException("ItemId is not available!");
        }

        Order order = orderRepository.save(Order.builder()
            .customerId(orderRequest.getCustomerId())
            .status(OrderStatus.CREATED.getCode())
            .build());
        List<ItemOrder> itemOrderList = new ArrayList<>();
        orderRequest.getItems().forEach(item -> {
            ItemOrder element = new ItemOrder();
            BeanUtils.copyProperties(item, element);
            element.setOrderId(order.getId());
            itemOrderList.add(element);
        });
        itemOrderRepository.saveAll(itemOrderList);

        return order.getId();
    }

    @Transactional
    public Boolean updateStatus(Long orderId, Integer status) {

        if (!OrderStatus.isOrderStatus(status)) throw new RuntimeException("Status is illegal");
        if (OrderStatus.isCreateStatus(status)) throw new RuntimeException("Status is illegal");
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        List<ItemOrder> itemOrderList = itemOrderRepository.findByOrderId(orderId);
        if (orderOptional.isEmpty()) throw new RuntimeException("OrderId isn't exist");

        if (Objects.equals(orderOptional.get().getStatus(), status)) {
            return false;
        }
        Order orderUpdate = new Order();
        BeanUtils.copyProperties(orderOptional.get(), orderUpdate);
        orderUpdate.setStatus(status);
        orderRepository.save(orderUpdate);
        // if status == COMPLETED -> save orderHistory
        if (OrderStatus.isCompleteStatus(status)) {
            this.saveOrderHistory(orderUpdate, itemOrderList);
        }
        return true;
    }

    private void saveOrderHistory(Order order, List<ItemOrder> itemOrderList) {

        OrderHistory orderHistory = new OrderHistory();
        BeanUtils.copyProperties(order, orderHistory, "id");
        orderHistory.setOrderDate(LocalDateTime.now());
        orderHistory.setOrderId(order.getId());
        orderHistoryRepository.save(orderHistory);
        List<ItemHistory> itemHistories = new ArrayList<>();
        itemOrderList.forEach(itemOrder -> {
            ItemHistory element = new ItemHistory();
            BeanUtils.copyProperties(itemOrder, element);
            element.setOrderHistoryId(orderHistory.getId());
            itemHistories.add(element);
        });
        itemHistoryRepository.saveAll(itemHistories);
    }
}
