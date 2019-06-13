package com.bing.service;

import com.bing.common.ServerResponse;

import java.util.Map;

public interface IOrderService {

    public ServerResponse create(Integer userId,Integer shippingId);
    public ServerResponse getProductItem(Integer userId,Long orderNo);
    public ServerResponse cancel(Integer userId, Long orderNo);
    public ServerResponse orderPay(Integer userId,Long orderNo);
    public ServerResponse updateOrder(Integer userId,Long orderNo);
    public String paySuccess(Map<String,String> signParams);
}
