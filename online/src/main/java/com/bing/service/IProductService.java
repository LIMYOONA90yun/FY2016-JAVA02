package com.bing.service;

import com.bing.common.ServerResponse;
import com.bing.pojo.Product;
import com.bing.vo.ProductDetailVo;

public interface IProductService {
    public ServerResponse addOrUpdate(Product product);
    public ServerResponse updateStatus(Product product);
    public ServerResponse search(String productName,
                                 Integer productId,
                                 Integer pageNum,
                                Integer pageSize);
    public ServerResponse<ProductDetailVo> detail(Integer productId);
    public ServerResponse findDetailByFront(Integer id);
    public ServerResponse searchFront(Integer categoryId,
                                     String keyword,
                                     Integer pageNum,
                                     Integer pageSize,
                                     String orderBy);


    public ServerResponse<Product> findProductByProductId(Integer productId);
}
