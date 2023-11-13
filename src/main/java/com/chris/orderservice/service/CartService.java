package com.chris.orderservice.service;

import com.chris.data.dto.order.CartDTO;
import com.chris.data.redis.Cart;

import java.util.List;

public interface CartService {

    List<Cart> getAllByCustomer();

    Cart save(CartDTO cartDTO);
    Cart update(CartDTO cartDTO);

    void delete(long productItemId);

    void deleteAllByCustomer();

}
