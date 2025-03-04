package com.example.connect.api.domain.categoryitem;

import com.example.connect.api.domain.categoryitem.key.CategoryItemKey;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

@Entity
public class CategoryItem {
    @EmbeddedId
    private CategoryItemKey id;
}
