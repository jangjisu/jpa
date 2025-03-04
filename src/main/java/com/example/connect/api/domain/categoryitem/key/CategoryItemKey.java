package com.example.connect.api.domain.categoryitem.key;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
public class CategoryItemKey implements Serializable {
    private Long categoryId;
    private Long itemId;
}
