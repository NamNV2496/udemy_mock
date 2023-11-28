package com.udemy.mock.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemResponse {

    private Long id;
    private String itemCode;
    private Integer price;
    private Integer quantity;
}
