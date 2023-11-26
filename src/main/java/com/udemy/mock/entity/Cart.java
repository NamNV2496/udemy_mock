package com.udemy.mock.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "mock_cart")
public class Cart extends Auditing<String> {

    @Id
    private Long id;
    private Long itemId;
    private Long customerId;
    private Integer price;
    private Integer quantity;
}
