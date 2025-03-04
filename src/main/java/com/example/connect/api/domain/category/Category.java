package com.example.connect.api.domain.category;

import com.example.connect.api.domain.categoryitem.CategoryItem;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    //TODO parentId 자기자신 FK?
    private String parentId;
    private String name;

    @OneToMany(mappedBy = "category")
    private List<CategoryItem> categoryItems;
}
