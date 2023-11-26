package com.udemy.mock.service;

import com.udemy.mock.dto.request.ItemRequest;
import com.udemy.mock.dto.response.ItemResponse;
import com.udemy.mock.entity.Item;
import com.udemy.mock.repository.ItemRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemResponse getItemById(Long id) {
        Optional<Item> itemOptional = itemRepository.findById(id);
        if (itemOptional.isPresent()) {
            ItemResponse response = new ItemResponse();
            BeanUtils.copyProperties(itemOptional.get(), response);
            return response;
        }
        return null;
    }

    public Long insertItem(ItemRequest itemRequest) {

        if (itemRepository.existsByItemCode(itemRequest.getItemCode())) {
            throw new RuntimeException("Id is existed");
        }
        if (itemRequest.getPrice() <= 0 || itemRequest.getQuantity() <= 0) {
            throw new RuntimeException("Data is invalid");
        }
        Item item = new Item();
        BeanUtils.copyProperties(itemRequest, item);
        return itemRepository.save(item).getId();
    }

    public Long updateItem(ItemRequest itemRequest) {

        if (itemRequest.getPrice() <= 0 || itemRequest.getQuantity() < 0) {
            throw new RuntimeException("Data is invalid");
        }
        Item item = itemRepository.findById(itemRequest.getId()).orElseThrow(
            () -> new RuntimeException("Id isn't exist!")
        );
        BeanUtils.copyProperties(itemRequest, item, "id", "created_date", "created_by");
        return itemRepository.save(item).getId();
    }

    public void deleteItem(Long id) {

        if (!itemRepository.existsById(id)) {
            throw new RuntimeException("Id isn't exist!");
        }
        itemRepository.deleteById(id);
    }
}
