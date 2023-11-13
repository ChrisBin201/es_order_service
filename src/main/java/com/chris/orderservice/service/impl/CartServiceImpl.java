package com.chris.orderservice.service.impl;

import com.chris.common.caller.ProductServiceCaller;
import com.chris.common.config.UserDetailsInfo;
import com.chris.common.handler.CommonErrorCode;
import com.chris.common.handler.CommonException;
import com.chris.common.service.redis.RedisCartService;
import com.chris.data.dto.order.CartDTO;
import com.chris.data.entity.order.sub.ProductItemDetail;
import com.chris.data.entity.product.ProductItem;
import com.chris.data.redis.Cart;
import com.chris.orderservice.service.CartService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    RedisCartService redisCartService;

    @Autowired
    ProductServiceCaller productServiceCaller;

    @Override
    public List<Cart> getAllByCustomer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsInfo userLogin = (UserDetailsInfo) authentication.getPrincipal();
        return  redisCartService.getCarts(userLogin.getId()).orElse(List.of());

    }

    @Override
    public Cart save(CartDTO cartDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsInfo userLogin = (UserDetailsInfo) authentication.getPrincipal();
        ProductItemDetail productItem = productServiceCaller.findProductItemById(cartDTO.getProductItemId())
                .onErrorResume(e -> {
                    throw new RuntimeException(e);
                })
                .blockOptional()
                .orElseThrow(() -> new CommonException(HttpStatus.BAD_REQUEST.value(),
                        CommonErrorCode.PRODUCT_ITEM_NOT_FOUND.getCode(),
                        CommonErrorCode.PRODUCT_ITEM_NOT_FOUND.getMessage()));
        Cart cart = Cart.builder()
                .customerId(userLogin.getId())
                .productItem(productItem)
                .quantity(cartDTO.getQuantity())
                .build();
        redisCartService.saveCart(cart);
        return cart;
    }

    @Override
    public Cart update(CartDTO cartDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsInfo userLogin = (UserDetailsInfo) authentication.getPrincipal();
        ProductItemDetail productItem = productServiceCaller.findProductItemById(cartDTO.getProductItemId())
                .onErrorResume(e -> {
                    throw new RuntimeException(e);
                })
                .blockOptional()
                .orElseThrow(() -> new CommonException(HttpStatus.BAD_REQUEST.value(),
                        CommonErrorCode.PRODUCT_ITEM_NOT_FOUND.getCode(),
                        CommonErrorCode.PRODUCT_ITEM_NOT_FOUND.getMessage()));
        Cart cart = Cart.builder()
                .customerId(userLogin.getId())
                .productItem(productItem)
                .quantity(cartDTO.getQuantity())
                .build();
        redisCartService.updateCart(cart);
        return cart;
    }

    @Override
    public void delete(long productItemId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsInfo userLogin = (UserDetailsInfo) authentication.getPrincipal();
        redisCartService.deleteCart(userLogin.getId(),productItemId);
    }

    @Override
    public void deleteAllByCustomer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsInfo userLogin = (UserDetailsInfo) authentication.getPrincipal();
        redisCartService.deleteCarts(userLogin.getId());
    }
}
