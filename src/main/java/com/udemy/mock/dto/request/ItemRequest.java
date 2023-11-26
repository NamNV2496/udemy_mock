package com.udemy.mock.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemRequest {

    private Long id;
    private String itemCode;
    private Integer price;
    private Integer quantity;
}
