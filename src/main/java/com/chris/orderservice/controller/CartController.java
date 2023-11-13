package com.chris.orderservice.controller;

import com.chris.common.config.UserDetailsInfo;
import com.chris.common.service.redis.RedisCartService;
import com.chris.data.dto.ResponseData;
import com.chris.data.dto.order.CartDTO;
import com.chris.data.dto.product.CategoryDTO;
import com.chris.data.redis.Cart;
import com.chris.orderservice.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController extends BaseController{

    @Autowired
    CartService cartService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllByCustomer() {
        ResponseData<List<Cart>> response = new ResponseData<>();
        response.initData(cartService.getAllByCustomer());
        response.success();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody CartDTO dto) {
        ResponseData<Cart> response = new ResponseData<>();

        Cart cart = cartService.save(dto);

        response.initData(cart);
        response.success();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PutMapping("")
    public ResponseEntity<?> update(@Valid @RequestBody CartDTO dto) {
        ResponseData<Cart> response = new ResponseData<>();

        Cart cart = cartService.update(dto);

        response.initData(cart);
        response.success();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteByProductItemId(@RequestParam(name = "product_item_id") long productItemId) {
        ResponseData<CategoryDTO> response = new ResponseData<>();
        cartService.delete(productItemId);
        response.success();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/all")
    public ResponseEntity<?> deleteAllByCustomer() {
        ResponseData<CategoryDTO> response = new ResponseData<>();
        cartService.deleteAllByCustomer();
        response.success();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
