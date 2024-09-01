package com.zljin.gulimall.cart.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.zljin.gulimall.cart.config.CartInterceptor;
import com.zljin.gulimall.cart.feign.ProductFeignService;
import com.zljin.gulimall.cart.service.CartService;
import com.zljin.gulimall.cart.vo.Cart;
import com.zljin.gulimall.cart.vo.CartItem;
import com.zljin.gulimall.cart.vo.SkuInfoVo;
import com.zljin.gulimall.cart.vo.UserInfoTo;
import com.zljin.gulimall.common.utils.R;
import com.zljin.gulimall.common.utils.ThreadPoolManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CartServiceImpl implements CartService {

    private final StringRedisTemplate redisTemplate;

    private final ProductFeignService productFeignService;

    private final String CART_PREFIX = "gulimall:cart:";


    public CartServiceImpl(StringRedisTemplate redisTemplate, ProductFeignService productFeignService) {
        this.redisTemplate = redisTemplate;
        this.productFeignService = productFeignService;
    }

    @Override
    public CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        String res = (String) cartOps.get(skuId.toString());
        if (StrUtil.isEmpty(res)) {
            //购物车无此商品
            //2、添加新商品到购物车
            //1、远程查询当前要添加的商品的信息
            CartItem cartItem = new CartItem();
            CompletableFuture<Void> getSkuInfoTask = CompletableFuture.runAsync(() -> {
                R skuInfo = productFeignService.getSkuInfo(skuId);
                SkuInfoVo data = skuInfo.getData("skuInfo", new TypeReference<SkuInfoVo>() {
                });
                cartItem.setCheck(true);
                cartItem.setCount(num);
                cartItem.setImage(data.getSkuDefaultImg());
                cartItem.setTitle(data.getSkuTitle());
                cartItem.setSkuId(skuId);
                cartItem.setPrice(data.getPrice());
            }, ThreadPoolManager.THREAD_POOL_EXECUTOR);

            //2、远程查询sku的组合信息
            CompletableFuture<Void> getSkuSaleAttrValues = CompletableFuture.runAsync(() -> {
                List<String> values = productFeignService.getSkuSaleAttrValues(skuId);
                cartItem.setSkuAttr(values);
            }, ThreadPoolManager.THREAD_POOL_EXECUTOR);

            CompletableFuture.allOf(getSkuInfoTask, getSkuSaleAttrValues).get();
            String s = JSONUtil.toJsonStr(cartItem);
            cartOps.put(skuId.toString(), s);
            return cartItem;
        } else {
            //购物车有此商品，修改数量
            CartItem cartItem = JSONUtil.toBean(res, CartItem.class);
            cartItem.setCount(cartItem.getCount() + num);
            cartOps.put(skuId.toString(), JSONUtil.toJsonStr(cartItem));
            return cartItem;
        }
    }

    @Override
    public CartItem getCartItem(Long skuId) {
        String str = (String) getCartOps().get(skuId.toString());
        return JSONUtil.toBean(str, CartItem.class);
    }

    @Override
    public Cart getCart() throws ExecutionException, InterruptedException {
        Cart cart = new Cart();
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        if (userInfoTo.getUserId() != null) {
            //1、登录
            String cartKey = CART_PREFIX + userInfoTo.getUserId();
            //2、如果临时购物车的数据还没有进行合并【合并购物车】
            String tempCartKey = CART_PREFIX + userInfoTo.getUserKey();
            List<CartItem> tempCartItems = getCartItems(tempCartKey);
            if (tempCartItems != null) {
                //临时购物车有数据，需要合并
                for (CartItem item : tempCartItems) {
                    addToCart(item.getSkuId(), item.getCount());
                }
                //清除临时购物车的数据
                clearCart(tempCartKey);
            }

            //3、获取登录后的购物车的数据【包含合并过来的临时购物车的数据，和登录后的购物车的数据】
            List<CartItem> cartItems = getCartItems(cartKey);
            cart.setItems(cartItems);
        } else {
            //2、没登录
            String cartKey = CART_PREFIX + userInfoTo.getUserKey();
            //获取临时购物车的所有购物项
            List<CartItem> cartItems = getCartItems(cartKey);
            cart.setItems(cartItems);
        }
        return cart;
    }

    @Override
    public void clearCart(String cartkey) {
        redisTemplate.delete(cartkey);
    }

    /**
     * 点击确认勾选按钮
     * @param skuId
     * @param check
     */
    @Override
    public void checkItem(Long skuId, Integer check) {
        CartItem cartItem = getCartItem(skuId);
        cartItem.setCheck(check == 1);
        getCartOps().put(skuId.toString(), JSONUtil.toJsonStr(cartItem));
    }

    @Override
    public void changeItemCount(Long skuId, Integer num) {
        CartItem cartItem = getCartItem(skuId);
        cartItem.setCount(num);
        getCartOps().put(skuId.toString(),JSONUtil.toJsonStr(cartItem));
    }

    @Override
    public void deleteItem(Long skuId) {
        getCartOps().delete(skuId.toString());
    }

    @Override
    public List<CartItem> getUserCartItems() {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        if (userInfoTo.getUserId() == null) {
            return null;
        }
        String cartKey = CART_PREFIX + userInfoTo.getUserId();
        List<CartItem> cartItems = getCartItems(cartKey);

        //获取所有被选中的购物项
        return cartItems.stream()
                .filter(item -> item.getCheck())
                .map(item -> {
                    R price = productFeignService.getPrice(item.getSkuId());
                    String data = (String) price.get("data");
                    item.setPrice(new BigDecimal(data));
                    return item;
                }).collect(Collectors.toList());
    }


    /**
     * 获取到我们要操作的购物车
     * <p> redis hash dataType
     * k->Map(k,v)
     * <p>
     * k:userId
     *          Map(k: skuId  v: CartItem)
     *
     * @return
     */
    private BoundHashOperations<String, Object, Object> getCartOps() {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        String cartKey = "";
        if (userInfoTo.getUserId() != null) {
            cartKey = CART_PREFIX + userInfoTo.getUserId();
        } else {
            cartKey = CART_PREFIX + userInfoTo.getUserKey();
        }
        return redisTemplate.boundHashOps(cartKey);
    }

    private List<CartItem> getCartItems(String cartKey) {
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(cartKey);
        List<Object> values = hashOps.values();
        if (CollectionUtil.isEmpty(values)) {
            return null;
        }
        return values.stream().map((obj) -> {
            String str = (String) obj;
            return JSONUtil.toBean(str, CartItem.class);
        }).collect(Collectors.toList());
    }
}
