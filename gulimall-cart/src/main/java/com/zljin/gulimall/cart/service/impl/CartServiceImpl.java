package com.zljin.gulimall.cart.service.impl;

import com.zljin.gulimall.cart.service.CartService;
import com.zljin.gulimall.cart.vo.Cart;
import com.zljin.gulimall.cart.vo.CartItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class CartServiceImpl implements CartService {
    @Override
    public CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public CartItem getCartItem(Long skuId) {
        return null;
    }

    @Override
    public Cart getCart() throws ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public void clearCart(String cartkey) {

    }

    @Override
    public void checkItem(Long skuId, Integer check) {

    }

    @Override
    public void changeItemCount(Long skuId, Integer num) {

    }

    @Override
    public void deleteItem(Long skuId) {

    }

    @Override
    public List<CartItem> getUserCartItems() {
        return null;
    }
}
