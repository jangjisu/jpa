package com.example.connect.api.domain.orderitem.key;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
public class OrderItemKey implements Serializable {
    private Long orderId;
    private Long itemId;
}
