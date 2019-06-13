package com.bing.controller.front;

import com.bing.common.ServerResponse;
import com.bing.pojo.Shipping;
import com.bing.pojo.User;
import com.bing.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RequestMapping("shipping")
@RestController
public class shippingController {

    @Autowired
    IAddressService addressService;


    @Autowired
    HttpSession session;

    /**
     * 添加地址
     *
     * */
    public Integer getId(){
       User user = (User)session.getAttribute("user");
       return user.getId();
    }

    @RequestMapping("/add.do")
    public ServerResponse add(Shipping shipping){

        return  addressService.add(getId(),shipping);

    }

    /**
     * 删除
     * */
    @RequestMapping("/del.do")
    public ServerResponse del(Integer shippingId){
        return  addressService.del(getId(),shippingId);
    }

    /**
     * 登录状态更新地址
     * */
    @RequestMapping("/update.do")
    public ServerResponse update(Shipping shipping){

        shipping.setUserId(getId());
        return  addressService.update(shipping);

    }

    /**
     * 选中查看具体的地址
     * */

    @RequestMapping("/select.do")
    public ServerResponse select(Integer shippingId){

        return  addressService.select(shippingId);

    }

    /**
     * 分页查询
     * */

    @RequestMapping("/list.do")
    public ServerResponse list(@RequestParam(required = false,defaultValue = "1") Integer pageNum,
                               @RequestParam(required = false,defaultValue = "10")Integer pageSize){


        return  addressService.list(pageNum,pageSize);

    }



}
