package com.bing.controller.front;

import com.bing.common.ServerResponse;
import com.bing.pojo.User;
import com.bing.service.impl.CartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/cart/")
public class CatController {
    @Autowired
    HttpSession session;
    @Autowired
    CartServiceImpl cartService;

    public Integer getId(){
        User user =(User) session.getAttribute("user");
        return user.getId();
    }

    @RequestMapping("list.do")
    public ServerResponse listCart(){//查询购物车里的所有信息

        return cartService.listCart(getId());
    }
    @RequestMapping("add.do")
    public ServerResponse addProduct(Integer productId,Integer count){//添加商品到购物车
        System.out.println(productId);
        System.out.println(count);
        System.out.println(getId());
        return  cartService.addUpdateProduct(getId(),productId,count);
    }
    @RequestMapping("update.do")
    public ServerResponse update(Integer productId,Integer count){//更新购物车产品的数量
        return cartService.update(getId(),productId,count);
    }

    @RequestMapping("delete_product.do")
    public ServerResponse delectCartProduct(String productId){//移除购物车某商品
        return cartService.deleteCartProduct(getId(),productId);
    }

    @RequestMapping("select.do")
    public ServerResponse selectCartProduct(Integer productId){//购物车选中某商品
        return cartService.chooseCartProduct(getId(),productId,1);
    }

    @RequestMapping("un_select.do")
    public ServerResponse unSelectCartProduct(Integer productId){//购物车取消选中某商品
        return cartService.chooseCartProduct(getId(),productId,0);
    }

    @RequestMapping("get_cart_product_count.do")
    public ServerResponse selectCartProductCount(){//查询购物车中的商品数量
        return cartService.selectCartProductCount(getId());
    }

    @RequestMapping("select_all.do")
    public ServerResponse selectAll(){//全选
        return cartService.chooseCartProduct(getId(),null,1);
    }

    @RequestMapping("un_select_all.do")
    public ServerResponse unSelectAll(){//取消全选
        return cartService.chooseCartProduct(getId(),null,0);
    }
}
