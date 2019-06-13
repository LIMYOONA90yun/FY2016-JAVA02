package com.bing.service;

import com.bing.common.ServerResponse;
import com.bing.pojo.Shipping;

public interface IAddressService {

    public ServerResponse add(Integer userId,Shipping shipping);

    /**
     * 删除
     */
    public ServerResponse del(Integer userId,Integer shippingId);

    /**
     * 登录状态更新地址
     */
    public ServerResponse update(Shipping shipping);

    /**
     * 选中查看具体的地址
     */
    public ServerResponse select(Integer shippingId);

    /**
     * 分页查询
     */
    public ServerResponse list(Integer pageNum, Integer pageSize);

}