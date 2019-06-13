package com.bing.service;

import com.bing.common.ServerResponse;
import com.bing.pojo.Cart;

import java.util.List;

public interface ICartService {

    public ServerResponse listCart(Integer userId);//查询购物车里的所有信息


    public ServerResponse addUpdateProduct(Integer userId,Integer productId,Integer count);//添加商品到购物车


    public ServerResponse deleteCartProduct(Integer userId,String productIds);//移除购物车某商品

    public ServerResponse update(Integer userId, Integer productId, Integer count);//在购物车中只更新商品的数量


    public ServerResponse chooseCartProduct(Integer userId,Integer productId,Integer check);//购物车选中某商品


    public ServerResponse selectCartProductCount(Integer userId);//查询购物车中的商品数量

    public List<Cart> findCartListByUserIdAndChecked(Integer userId);

    public ServerResponse batchDelete(List<Cart> cartList);//创建订单，批量删除购物车中已经选择的商品


}
