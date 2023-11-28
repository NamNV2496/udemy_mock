package com.udemy.mock.controller;

import com.udemy.mock.dto.response.OrderHistoryResponse;
import com.udemy.mock.service.OrderHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order-history")
@RequiredArgsConstructor
public class OrderHistoryController {

    private final OrderHistoryService orderHistoryService;


    /*
    curl --location 'http://localhost:8080/mock/api/v1/order-history?date=2023-11-26&customerId=1'
    */
    @GetMapping
    public OrderHistoryResponse getOrderHistory(@RequestParam String date, @RequestParam Long customerId) {

        return orderHistoryService.getOrderHistory(date, customerId);
    }
}
