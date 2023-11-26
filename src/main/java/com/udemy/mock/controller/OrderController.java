package com.udemy.mock.controller;

import com.udemy.mock.dto.request.OrderRequest;
import com.udemy.mock.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    /*
        curl --location 'http://localhost:8080/mock/api/v1/order' \
        --header 'Content-Type: application/json' \
        --data '{
            "customerId": 1,
            "items": [
                {
                    "itemId": 1,
                    "price": 125000,
                    "quantity": 2
                },
                {
                    "itemId": 2,
                    "price": 75000,
                    "quantity": 1
                }
            ]
        }'
    */
    @PostMapping
    public Long createOrder(@RequestBody OrderRequest orderRequest) {

        return orderService.createOrder(orderRequest);
    }

    /*
        curl --location --request PUT 'http://localhost:8080/mock/api/v1/order/update-order-status?orderId=1&status=2'
     */
    @PutMapping("/update-order-status")
    public Boolean updateStatusOrder(@RequestParam Long orderId, @RequestParam Integer status) {

        return orderService.updateStatus(orderId, status);
    }
}
