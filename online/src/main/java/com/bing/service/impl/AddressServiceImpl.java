package com.bing.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.bing.common.ResponseCode;
import com.bing.common.ServerResponse;
import com.bing.dao.ShippingMapper;
import com.bing.pojo.Shipping;
import com.bing.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AddressServiceImpl implements IAddressService {

    @Autowired
    ShippingMapper shippingMapper;

    @Override
    public ServerResponse add(Integer userId, Shipping shipping) {
        if(shipping==null){
            return ServerResponse.createServerResponseByFail(ResponseCode.ERROR,"参数错误");
        }
        //step2:添加
        shipping.setUserId(userId);
        shippingMapper.insert(shipping);
        //setp3:返回结果
        Map<String,Integer> map= Maps.newHashMap();
        map.put("shippingId",shipping.getId());
        return ServerResponse.createServerResponseBySucess("添加成功",map);
    }

    @Override
    public ServerResponse del(Integer userId, Integer shippingId) {
        if(shippingId==null){
            return ServerResponse.createServerResponseByFail(ResponseCode.ERROR,"参数必传");
        }
        //setp2:删除
        int result=shippingMapper.deleteByUserIdAndShippingId(userId,shippingId);
        //step3:返回结果
        if(result>0){
            return ServerResponse.createServerResponseBySucess();
        }

        return ServerResponse.createServerResponseByFail(ResponseCode.ERROR,"删除失败");
}

    @Override
    public ServerResponse update(Shipping shipping) {
        if(shipping==null){
            return ServerResponse.createServerResponseByFail(ResponseCode.ERROR,"参数错误");
        }
        //step2:更新
        int result=  shippingMapper.updateBySelectiveKey(shipping);
        //step3:返回结果

        if(result>0){
            return ServerResponse.createServerResponseBySucess();
        }
        return ServerResponse.createServerResponseByFail(ResponseCode.ERROR,"更新失败");
    }

    @Override
    public ServerResponse select(Integer shippingId) {
        if(shippingId==null){
            return ServerResponse.createServerResponseByFail(ResponseCode.ERROR,"参数错误");
        }
        Shipping shipping= shippingMapper.selectByPrimaryKey(shippingId);


        return ServerResponse.createServerResponseBySucess("查询成功",shipping);
    }

    @Override
    public ServerResponse list(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList=shippingMapper.selectAll();
        PageInfo pageInfo=new PageInfo(shippingList);

        return ServerResponse.createServerResponseBySucess("成功",pageInfo);
    }
}
