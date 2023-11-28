package com.udemy.mock.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerRequest {

    private String userName;
    private Integer age;
    private Integer customerRank;
    private String address;
    private String phone;
}
