package com.example.connect.api.service.item;

import com.example.connect.api.domain.item.Item;
import com.example.connect.api.domain.item.ItemRepository;
import com.example.connect.api.domain.item.QItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    public Long saveItem(Item item) {
        Item save = itemRepository.save(item);
        return save.getId();
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이템이 존재하지 않습니다."));
    }

    public Iterable<Item> findItems2(String name, int priceStart, int priceEnd) {
        QItem item = QItem.item;
        return itemRepository.findAll(
          item.name.contains(name).and(item.price.between(priceStart, priceEnd))
        );
    }
}
