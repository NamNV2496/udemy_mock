package com.udemy.mock.controller;

import com.udemy.mock.dto.request.ItemRequest;
import com.udemy.mock.dto.response.ItemResponse;
import com.udemy.mock.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    //curl --location 'http://localhost:8080/mock/api/v1/item/1'
    @GetMapping("/{id}")
    public ItemResponse getItem(@PathVariable Long id) {
        return itemService.getItemById(id);
    }

    // curl -X POST "http://localhost:8080/mock/api/v1/item" -H "accept: */*" -H "Content-Type: application/json" -d "{\"id\":1,\"itemCode\":\"Quần\",\"price\":125000,\"quantity\":50}"
    @PostMapping
    public Long insertItem(@RequestBody ItemRequest itemRequest) {
        return itemService.insertItem(itemRequest);
    }

    // curl -X PUT "http://localhost:8080/mock/api/v1/item" -H "accept: */*" -H "Content-Type: application/json" -d "{\"id\":1,\"itemCode\":\"Quần\",\"price\":150000,\"quantity\":50}"
    @PutMapping
    public Long updatItem(@RequestBody ItemRequest itemRequest) {
        return itemService.updateItem(itemRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
    }
}
