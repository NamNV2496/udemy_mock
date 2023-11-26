package com.udemy.mock.service;

import com.udemy.mock.dto.request.CustomerRequest;
import com.udemy.mock.entity.Customer;
import com.udemy.mock.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Transactional
    public Long addNewCustomer(CustomerRequest customerRequest) {

        if (customerRepository.existsByUserName(customerRequest.getUserName())) {
            throw new RuntimeException("Username is existed!");
        }
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerRequest, customer);
        return customerRepository.save(customer).getId();
    }
}
