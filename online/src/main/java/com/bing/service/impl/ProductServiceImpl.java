package com.bing.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.bing.common.ProductStatusEnum;
import com.bing.common.ResponseCode;
import com.bing.common.ServerResponse;
import com.bing.dao.CategoryMapper;
import com.bing.dao.ProductMapper;
import com.bing.pojo.Category;
import com.bing.pojo.Product;
import com.bing.service.ICategory;
import com.bing.service.IProductService;
import com.bing.utils.DateUtils;
import com.bing.vo.ProductDetailVo;
import com.bing.vo.ProductListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    ProductMapper productMapper;
    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    ICategory iCategory;
    private String imageHost;

    @Override
    /**
     *
     * */
    public ServerResponse<ProductDetailVo> addOrUpdate(Product product) {
        if (product.getCategoryId() == null) {
            return ServerResponse.createServerResponseByFail(ResponseCode.ERROR, "参数必传");
        }
        //寻找主图
        String subImage = product.getSubImages();
        if (subImage != null && !subImage.equals("")) {
            String[] subImages = subImage.split(",");
            if (subImages.length > 0) {
                product.setMainImage(subImages[0]);
            }
        }
        if (product.getId() == null) {//添加
            int result = productMapper.insert(product);
            if (result <= 0) {
                return ServerResponse.createServerResponseByFail(ResponseCode.ERROR, "添加失败");
            }
            return ServerResponse.createServerResponseBySucess();
        } else {//更新
            int result = productMapper.updateByPrimaryKey(product);
            if (result <= 0) {
                return ServerResponse.createServerResponseByFail(ResponseCode.ERROR, "更新失败");
            }
            return ServerResponse.createServerResponseBySucess();
        }
    }

    /**
     * 修改售卖状态1-在售 2-下架 3-删除
     */
    @Override
    public ServerResponse updateStatus(Product product) {
        if (product.getStatus() == null) {
            return ServerResponse.createServerResponseByFail(ResponseCode.ERROR, "参数必传");
        }
        int result = productMapper.updateStatus(product);
        if (result <= 0) {
            return ServerResponse.createServerResponseByFail(ResponseCode.ERROR, "修改失败");
        }
        return ServerResponse.createServerResponseBySucess();
    }

    /**
     * 分页搜索商品
     */
    @Override
    public ServerResponse search(String productName, Integer productId, Integer pageNum, Integer pageSize) {
        if (productName == null && productId == null) {
            return ServerResponse.createServerResponseByFail(ResponseCode.ERROR, "参数必传");
        }
        if (productName != null) {
            productName = "%" + productName + "%";
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.findProductByNameOrId(productId, productName);
        List<ProductListVo> productListVoList = Lists.newArrayList();
        if (productList != null && productList.size() > 0) {
            for (Product product : productList) {
                //将前端需要的值放到vo这个集合中
                ProductListVo productListVo = assembleProductListVo(product);
                productListVoList.add(productListVo);
            }
        }
        PageInfo pageInfo = new PageInfo(productListVoList);

        return ServerResponse.createServerResponseBySucess(null, pageInfo);
    }

    /**
     * 后台查询商品细节
     */
    @Override
    public ServerResponse<ProductDetailVo> detail(Integer productId) {
        if (productId == null) {
            return ServerResponse.createServerResponseByFail(ResponseCode.ERROR, "请输入商品id");
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createServerResponseByFail(ResponseCode.ERROR,"查询的商品不存在");
        }
        ProductDetailVo productDetailVo = assembleProductDetailListVo(product);

        return ServerResponse.createServerResponseBySucess(null, productDetailVo);
    }

    /*
     * 分页的VO
     * */
    private ProductListVo assembleProductListVo(Product product) {
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setName(product.getName());
        productListVo.setPrice(product.getPrice());
        productListVo.setStatus(product.getStatus());
        productListVo.setSubtitle(product.getSubtitle());

        return productListVo;
    }

    /*
     * 细节的VO
     * */
    private ProductDetailVo assembleProductDetailListVo(Product product) {
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setCreateTime(DateUtils.dateToStr(product.getCreateTime()));
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setImageHost(imageHost);
        productDetailVo.setName(product.getName());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setId(product.getId());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setUpdateTime(DateUtils.dateToStr(product.getUpdateTime()));
        //要查父类id，就要先查到类别，然后才能得到
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category != null) {
            productDetailVo.setParentCategoryId(category.getParentId());
        }
        return productDetailVo;
    }

    /**
     * 前端商品详情
     */
    public ServerResponse findDetailByFront(Integer id) {
        //非空校验
        if (id == null) {
            return ServerResponse.createServerResponseByFail(ResponseCode.ERROR, "参数必传");
        }
        //搜索商品
        Product product = productMapper.selectByPrimaryKey(id);
        if (product == null) {
            return ServerResponse.createServerResponseByFail(ResponseCode.ERROR, "商品不存在");
        }
        //状态查询
        if (product.getStatus() == ProductStatusEnum.PRODUCT_OFFLINE.getStatus()) {
            return ServerResponse.createServerResponseBySucess("商品已下架");
        }
        if (product.getStatus() == ProductStatusEnum.PRODUCT_DELECT.getStatus()) {
            return ServerResponse.createServerResponseBySucess("商品已删除");
        }
        ProductDetailVo productDetailVo = assembleProductDetailListVo(product);
        return ServerResponse.createServerResponseBySucess("查看成功", productDetailVo);
    }

    @Override
    public ServerResponse searchFront(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy) {
        Set<Integer> integers = Sets.newHashSet();
        //参数校验

        if (categoryId == null && (keyword == null || keyword.equals(""))) {
            return ServerResponse.createServerResponseByFail(ResponseCode.ERROR, "参数必传");
        }
        if (categoryId != null) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null && (keyword == null || keyword.equals(""))) {
                //没有该类数据
                //开始分页
                PageHelper.startPage(pageNum, pageSize);
                //分页的vo
                List<ProductListVo> productListVoList = Lists.newArrayList();
                //分页信息
                PageInfo pageInfo = new PageInfo(productListVoList);
                return ServerResponse.createServerResponseBySucess(null, pageInfo);
            }
            //只是把类别id查完，还需产品的详细信息
            if (category != null) {
                ServerResponse serverResponse = iCategory.get_deep_category(category);
                if (serverResponse.isSucess()) {
                    integers = (Set<Integer>) serverResponse.getData();
                }
            }
        }
        //categoryname不为空
        if (keyword != null && !keyword.equals("")) {
            keyword = "%" + keyword + "%";
        }
        //排序
        if (orderBy.equals("")) {
            PageHelper.startPage(pageNum, pageSize);
        } else {
            String[] orderByArr = orderBy.split("_");
            if (orderByArr.length > 1) {//orderBy是这个分页插件的排序功能其中A为排序依据的字段名，B为排序规律，desc为降序，asc为升序
                PageHelper.startPage(pageNum, pageSize, orderByArr[0] + " " + orderByArr[1]);
            }else{
                PageHelper.startPage(pageNum,pageSize);
            }}
            List<Product> products = productMapper.searchByName(integers, keyword);
            List<ProductListVo> productListVOList = Lists.newArrayList();
            if (products != null && products.size() > 0) {
                for (Product product : products) {
                    ProductListVo productListVO = assembleProductListVo(product);
                    productListVOList.add(productListVO);
                }
            }
                //step5:分页
                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(productListVOList);
                //step6:返回
           return ServerResponse.createServerResponseBySucess(null, pageInfo);
        }

    @Override
    public ServerResponse<Product> findProductByProductId(Integer productId) {
        if (productId == null) {
            return ServerResponse.createServerResponseByFail(ResponseCode.ERROR, "请输入商品id");
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createServerResponseByFail(ResponseCode.ERROR,"查询的商品不存在");
        }
        return ServerResponse.createServerResponseBySucess(null, product);
    }
}


