package com.udemy.mock.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {

    private Long customerId;
    private List<ItemElement> items;

    @Data
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ItemElement {

        private Long itemId;
        private Integer price;
        private Integer quantity;
    }
}
