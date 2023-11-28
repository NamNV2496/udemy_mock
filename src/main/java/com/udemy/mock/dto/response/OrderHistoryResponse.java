package com.udemy.mock.dto.response;

import com.udemy.mock.dto.request.OrderRequest.ItemElement;
import java.time.LocalDateTime;
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
public class OrderHistoryResponse {

    List<OrderHistoryElement> historyElements;

    @Data
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OrderHistoryElement {

        private Long orderId;
        private LocalDateTime orderDate;
        private Integer status;
        private List<ItemElement> items;
    }
}
