package com.example.connect.api.domain.item;

import com.example.connect.api.domain.categoryitem.CategoryItem;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @OneToMany(mappedBy = "item")
    private List<CategoryItem> categoryItems;
}
