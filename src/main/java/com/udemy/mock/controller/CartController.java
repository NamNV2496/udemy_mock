package com.udemy.mock.controller;

import com.udemy.mock.dto.response.CartResponse;
import com.udemy.mock.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;


    // curl --location 'http://localhost:8080/mock/api/v1/cart?customerId=2'
    @GetMapping
    public CartResponse getCartInfo(@RequestParam Long customerId) {

        return cartService.getAllItem(customerId);
    }
}
