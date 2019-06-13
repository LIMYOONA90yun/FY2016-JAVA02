package com.bing.controller.front;


import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.bing.common.ServerResponse;
import com.bing.pojo.User;
import com.bing.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Iterator;
import java.util.Map;

import static java.lang.Integer.parseInt;

@RequestMapping("/order")
@RestController
public class OrderController {

    @Autowired
    HttpSession session;
    @Autowired
    IOrderService orderService;


    public Integer getId(){
      User user =  (User)session.getAttribute("user");
      return  user.getId();
    }

    @RequestMapping("/create.do")
    public ServerResponse create(Integer shippingId){
        return orderService.create(getId(),shippingId);
    }
    @RequestMapping("/get_order_cart_product")
    public ServerResponse getProductItem(Long orderNo){
        return orderService.getProductItem(getId(),orderNo);
    }
    @RequestMapping("/cancel.do")
    public ServerResponse deleteOrder(Long orderNo){
        return orderService.cancel(getId(),orderNo);
    }
    @RequestMapping("/order_pay.do")
    public ServerResponse orderPay(Long orderNo){
    return orderService.orderPay(getId(),orderNo);
    }
    @RequestMapping("/callback.do")
    public String alipay_callback(HttpServletRequest request){
        Map<String,String[]> callbackParams = request.getParameterMap();

        Map<String,String> signParams= Maps.newHashMap();
        Iterator<String> iterator =callbackParams.keySet().iterator();
        while (iterator.hasNext()){
            String key=iterator.next();
            String[] values=callbackParams.get(key);
           StringBuffer sbuffer = new StringBuffer();
            if (values!=null&&values.length>0){
                for(int i=0;i<values.length;i++){
                    sbuffer.append(values[i]);
                    if(i!=values.length-1){
                        sbuffer.append(",");
                    }
                }
            }
            signParams.put(key,sbuffer.toString());

        }

        //支付宝验签
        try {
            signParams.remove("sign_type");
          boolean result =  AlipaySignature.rsaCheckV2(signParams, Configs.getAlipayPublicKey(),"utf-8", Configs.getSignType());
        if (result){//验证成功
          return orderService.paySuccess(signParams);


        }else{
            return "fail";
        }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;

    }


    @RequestMapping("/update_order.do")
    public ServerResponse updateOrder(Long orderNo){
        return orderService.updateOrder(getId(),orderNo);
    }

}
