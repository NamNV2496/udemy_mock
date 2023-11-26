package com.udemy.mock.service;

import com.udemy.mock.dto.response.CartResponse;
import com.udemy.mock.dto.response.ItemResponse;
import com.udemy.mock.entity.Cart;
import com.udemy.mock.entity.Item;
import com.udemy.mock.repository.CartRepository;
import com.udemy.mock.repository.CustomerRepository;
import com.udemy.mock.repository.ItemRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public CartResponse getAllItem(Long customerId) {

        if (!customerRepository.existsById(customerId)) {
            throw new RuntimeException("CustomerId isn't exist");
        }
        List<Cart> cart = cartRepository.findByCustomerId(customerId);
        Map<Long, Cart> cartMap = cart.stream().collect(Collectors.toMap(Cart::getItemId, Function.identity()));
        List<Long> cartIds = cart.stream().map(Cart::getItemId).collect(Collectors.toList());
        List<Item> itemList = itemRepository.findByIdIn(cartIds);

        CartResponse response = new CartResponse();
        List<ItemResponse> items = new ArrayList<>();
        itemList.forEach(item -> {
            ItemResponse element = new ItemResponse();
            BeanUtils.copyProperties(item, element);
            element.setPrice(cartMap.get(item.getId()).getPrice());
            element.setQuantity(cartMap.get(item.getId()).getQuantity());
            items.add(element);
        });
        response.setItems(items);
        return response;
    }
}
