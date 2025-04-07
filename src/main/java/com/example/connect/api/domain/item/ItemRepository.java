package com.example.connect.api.domain.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item> {
    @Modifying(clearAutomatically = true) //clearAutomatically 영속성 컨텍스트 초기화
    @Query("update Item i set i.price = i.price * 1.1 where i.stockQuantity < :stockAmount")
    int bulkPriceUp(int stockAmount);
}
