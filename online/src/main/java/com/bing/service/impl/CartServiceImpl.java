package com.bing.service.impl;

import com.google.common.collect.Lists;
import com.bing.common.ResponseCode;
import com.bing.common.ServerResponse;
import com.bing.dao.CartMapper;
import com.bing.dao.ProductMapper;
import com.bing.pojo.Cart;
import com.bing.pojo.Product;
import com.bing.service.ICartService;
import com.bing.utils.BigDecimalUtils;
import com.bing.vo.CartProductVO;
import com.bing.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartServiceImpl implements ICartService {
    @Autowired
    CartMapper cartMapper;
    @Autowired
    ProductMapper productMapper;

    @Override//购物车列表
    public ServerResponse listCart(Integer userId) {
        CartVO cartVO= getCartVOLimit(userId);
        return ServerResponse.createServerResponseBySucess("购物车列表信息",cartVO);
    }

    @Override//添加商品到购物车
    public ServerResponse addUpdateProduct(Integer userId, Integer productId, Integer count) {
        if(productId==null||count==null){
            return ServerResponse.createServerResponseByFail(ResponseCode.ERROR,"参数不能为空");
        }

        Product product= productMapper.selectByPrimaryKey(productId);
        if(product==null){
            return ServerResponse.createServerResponseByFail(ResponseCode.ERROR,"要添加的商品不存在");
        }
        //step2:根据producId和userId查询购物信息
        Cart cart= cartMapper.selectCartByUseridAndProductId(userId,productId);
        if(cart==null){
            //添加
            Cart cart1=new Cart();
            cart1.setUserId(userId);
            cart1.setProductId(productId);
            cart1.setQuantity(count);
            cart1.setChecked(1);
            cartMapper.insert(cart1);
        }else{
            //更新
            Cart cart1=new Cart();
            cart1.setId(cart.getId());
            cart1.setProductId(productId);
            cart1.setUserId(userId);
            cart1.setQuantity(cart.getQuantity()+count);
            cart1.setChecked(cart.getChecked());
            cartMapper.updateByPrimaryKey(cart1);
        }
        CartVO cartVO = getCartVOLimit(userId);
        return ServerResponse.createServerResponseBySucess("操作成功",cartVO);
    }

    @Override
    public ServerResponse update(Integer userId, Integer productId, Integer count) {

        //step1:参数判定
        if(productId==null||count==null){
            return ServerResponse.createServerResponseByFail(ResponseCode.ERROR,"参数不能为空");
        }

        //step2:查询购物车中商品
        Cart cart= cartMapper.selectCartByUseridAndProductId(userId,productId);
        if(cart!=null){
            //step3:更新数量
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKey(cart);
        }


        //step4:返回cartvo
        return ServerResponse.createServerResponseBySucess("更新成功",getCartVOLimit(userId));
    }



    @Override//删除购物车信息
    public ServerResponse deleteCartProduct(Integer userId, String productIds) {
        if(productIds==null||productIds.equals("")){
            return ServerResponse.createServerResponseByFail(ResponseCode.ERROR,"参数不能为空");
        }
        //step2:productId-->List<Integer>
        List<Integer> productIdList=Lists.newArrayList();
        String[] productIdsArr=  productIds.split(",");
        if(productIdsArr!=null&&productIdsArr.length>0){
            for(String productIdstr:productIdsArr){
                Integer productId=Integer.parseInt(productIdstr);
                productIdList.add(productId);
            }
        }

        //step3:调用dao
        cartMapper.deleteByUserIdAndProductIds(userId,productIdList);
        //step4:返回结果
        return ServerResponse.createServerResponseBySucess("删除成功",getCartVOLimit(userId));
    }

    @Override//选择、取消选择、全选、取消全选
    public ServerResponse chooseCartProduct(Integer userId, Integer productId,Integer check) {
        cartMapper.selectOrUnselectProduct(userId,productId,check);
        return ServerResponse.createServerResponseBySucess("选择成功",getCartVOLimit(userId));
    }


    @Override//查询购物车中的产品的数量
    public ServerResponse selectCartProductCount(Integer userId) {
        int quantity=cartMapper.get_cart_product_count(userId);
        return ServerResponse.createServerResponseBySucess("查询成功",quantity);
    }

    @Override
    public List<Cart> findCartListByUserIdAndChecked(Integer userId) {
       List<Cart> list = cartMapper.findCartListByUserIdAndChecked(userId);
       return list;
    }

    @Override
    public ServerResponse batchDelete(List<Cart> cartList) {

       Integer result = cartMapper.batchDelete(cartList);
       if (result==null){
           return ServerResponse.createServerResponseByFail(ResponseCode.ERROR,"清除购物车错误");
       }

        return ServerResponse.createServerResponseBySucess("清除成功");
    }


    private CartVO getCartVOLimit(Integer userId){
        CartVO cartVO=new CartVO();
        //step1: 根据userId查询购物信息--》List<Cart>
        List<Cart> cartList=  cartMapper.selectCartByUserid(userId);//查询出购物车中属于“张三”的购物车信息
        //step2:List<Cart> -->List<CartProductVO>
        List<CartProductVO> cartProductVOList= Lists.newArrayList();//返回到前端的商品信息集合

        //购物车总价格
        BigDecimal carttotalprice=new BigDecimal("0");

        if(cartList!=null&&cartList.size()>0){//如果cartList不为空，遍历集合
            for(Cart cart:cartList){
                CartProductVO cartProductVO=new CartProductVO();
                cartProductVO.setId(cart.getId());
                cartProductVO.setQuantity(cart.getQuantity());
                cartProductVO.setUserId(userId);
                cartProductVO.setProductChecked(cart.getChecked());
                //查询商品，查找出来的信息返回到显示到前端的vo类中
                Product product=  productMapper.selectByPrimaryKey(cart.getProductId());
                if(product!=null){
                    cartProductVO.setProductId(cart.getProductId());
                    cartProductVO.setProductMainImage(product.getMainImage());
                    cartProductVO.setProductName(product.getName());
                    cartProductVO.setProductPrice(product.getPrice());
                    cartProductVO.setProductStatus(product.getStatus());
                    cartProductVO.setProductStock(product.getStock());
                    cartProductVO.setProductSubtitle(product.getSubtitle());
                    int  stock=product.getStock();
                    int limitProductCount=0;//如果说，库存大于购物车的数量，limitProductCount=购物车的数量，库存小于购物车的数量，limitProductCount=库存的数量，赋值给quantity返回前端
                    if(stock>=cart.getQuantity()){
                        limitProductCount=cart.getQuantity();
                        cartProductVO.setLimitQuantity("LIMIT_NUM_SUCCESS");
                    }else{//商品库存不足
                        limitProductCount=stock;
                        //更新购物车中商品的数量
                        Cart cart1=new Cart();
                        cart1.setId(cart.getId());
                        cart1.setQuantity(stock);
                        cart1.setProductId(cart.getProductId());
                        cart1.setChecked(cart.getChecked());
                        cart1.setUserId(userId);
                        cartMapper.updateByPrimaryKey(cart1);

                        cartProductVO.setLimitQuantity("LIMIT_NUM_FAIL");
                    }
                    cartProductVO.setQuantity(limitProductCount);
                    cartProductVO.setProductTotalPrice(BigDecimalUtils.mul(product.getPrice().doubleValue(),Double.valueOf(cartProductVO.getQuantity())));
                }
                if(cartProductVO.getProductChecked()==1 ){//被选中，计算总价
                    carttotalprice= BigDecimalUtils.add(carttotalprice.doubleValue(),cartProductVO.getProductTotalPrice().doubleValue());
                }
                cartProductVOList.add(cartProductVO);
            }
        }

        cartVO.setCartProductVOList(cartProductVOList);
        //step3: 计算总架构
        cartVO.setCarttotalprice(carttotalprice);

        //step4:判断购物车是否全选
        int count=cartMapper.isCheckedAll(userId);
        System.out.println(count);
        if(count==0){
            cartVO.setIsallchecked(true);
        }else {
            cartVO.setIsallchecked(false);
        }

        //step5:返回结果
        return cartVO;
    }


}
