package com.udemy.mock.controller;

import com.udemy.mock.dto.request.CustomerRequest;
import com.udemy.mock.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService CustomerRequest;


    /*
        curl --location 'http://localhost:8080/mock/api/v1/customer' \
        --header 'Content-Type: application/json' \
        --data '{
            "userName": "namNV",
            "age": 28,
            "phone": "0987654321",
            "customerRank": 1,
            "address": "Đống đa, Hà Nội"
        }'
    * */
    @PostMapping
    public Long addNewCustomer(@RequestBody CustomerRequest customerRequest) {

        return CustomerRequest.addNewCustomer(customerRequest);
    }
}
