package com.bing.controller.backend;

import com.bing.common.ServerResponse;
import com.bing.pojo.Product;
import com.bing.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manage/product/")
public class ProductContrioller {
    @Autowired
    IProductService productService;
    /**
     * 添加/更新
     * */
    @RequestMapping("list.do")
    public ServerResponse addOrUpdateProduct(Product product){
        return productService.addOrUpdate(product);
    }
    /**
     *
     * */
    @RequestMapping("set_sale_status.do")
    public ServerResponse updateStatus(Product product){
        return productService.updateStatus(product);
    }
    //搜索商品
    @RequestMapping("search.do")
    public ServerResponse search(@RequestParam(name="productName",required = false)String productName,
                                 @RequestParam(name="productId",required = false)Integer productId,
                                 @RequestParam(name="productNum",required = false,defaultValue = "1")Integer pageNum,
                                 @RequestParam(name="productSize",required = false,defaultValue = "10")Integer pageSize){
        return productService.search(productName,productId,pageNum,pageSize);
    }
    @RequestMapping("detail.do")
    public ServerResponse detail(Integer productId){

        return productService.detail(productId);
    }
}
