package com.bing.service.impl;

import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayMonitorService;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayMonitorServiceImpl;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.service.impl.AlipayTradeWithHBServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.google.common.collect.Lists;
import com.bing.alipay.Main;
import com.bing.common.ResponseCode;
import com.bing.common.ServerResponse;
import com.bing.dao.OrderItemMapper;
import com.bing.dao.OrderMapper;
import com.bing.dao.PayInfoMapper;
import com.bing.pojo.*;
import com.bing.service.IAddressService;
import com.bing.service.ICartService;
import com.bing.service.IOrderService;
import com.bing.service.IProductService;
import com.bing.utils.BigDecimalUtils;
import com.bing.utils.Const;
import com.bing.utils.DateUtils;
import com.bing.vo.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    OrderMapper orderMapper;
    @Autowired
    OrderItemMapper orderItemMapper;
    @Autowired
    ICartService cartService;
    @Autowired
    IProductService productService;
    @Autowired
    IAddressService addressService;
    @Autowired
    PayInfoMapper payInfoMapper;
    @Value("${online.imageHost}")
    private String imageHost;

    @Transactional//事务注解
    @Override
    public ServerResponse create(Integer userId,Integer shippingId) {
        //step1:参数非空校验

        if(shippingId==null){
            return ServerResponse.createServerResponseByFail(ResponseCode.ERROR,"地址参数不能为空");
        }

        //step2:根据userId查询购物车中已选中的商品 -》List<Cart>
        List<Cart> cartList= cartService.findCartListByUserIdAndChecked(userId);
        //step3:List<Cart>-->List<OrderItem>
        ServerResponse serverResponse= getCartOrderItem(userId,cartList);
        if(!serverResponse.isSucess()){
            return  serverResponse;
        }
        //step4:创建订单order并将其保存到数据库
        //计算订单的价格
        BigDecimal orderTotalPrice=new BigDecimal("0");
        List<OrderItem> orderItemList=(List<OrderItem>)serverResponse.getData();
        if(orderItemList==null||orderItemList.size()==0){
            return  ServerResponse.createServerResponseByFail(ResponseCode.ERROR,"购物车为空");
        }
        orderTotalPrice=getOrderPrice(orderItemList);
        Order order=createOrder(userId,shippingId,orderTotalPrice);

       // int a=3/0;

        if(order==null){
            return ServerResponse.createServerResponseByFail(ResponseCode.ERROR,"订单创建失败");
        }
        //step5:将List<OrderItem>保存到数据库
        for(OrderItem orderItem:orderItemList){
            orderItem.setOrderNo(order.getOrderNo());
        }
        //批量插入
        orderItemMapper.insertBatch(orderItemList);
        //step6:扣库存
        reduceProductStock(orderItemList);
        //step7:购物车中清空已下单的商品
        cleanCart(cartList);

        //step8:返回，OrderVO
        OrderVO orderVO= assembleOrderVO(order,orderItemList,shippingId);
        return ServerResponse.createServerResponseBySucess("订单创建成功",orderVO);
    }

    @Override
    public ServerResponse getProductItem(Integer userId,Long orderNo){//查询订单的商品详情
      List<OrderItem> orderItemList = orderItemMapper.selectByPrimaryKey(userId,orderNo);

        CartOrderItemVO cartOrderItemVO=new CartOrderItemVO();
        cartOrderItemVO.setImageHost(imageHost);
        List<OrderItemVO> orderItemVOList=Lists.newArrayList();
        if(orderItemList==null||orderItemList.size()==0){
            return ServerResponse.createServerResponseByFail(ResponseCode.ERROR,"没有此订单订单商品信息");
        }
        for(OrderItem orderItem:orderItemList){
            orderItemVOList.add(assembleOrderItemVO(orderItem));
        }
        cartOrderItemVO.setOrderItemVOList(orderItemVOList);
        cartOrderItemVO.setTotalPrice( getOrderPrice(orderItemList));
        // cartOrderItemVO.setTotalPrice();
        //step4:返回结果
        return ServerResponse.createServerResponseBySucess("查询成功",cartOrderItemVO);
    }


    @Override
    public ServerResponse cancel(Integer userId, Long orderNo) {

        //step1:参数非空校验
        if(orderNo==null){
            return  ServerResponse.createServerResponseByFail(ResponseCode.ERROR,"参数不能为空");
        }
        //step2:根据userid和orderNO查询订单
       Order order = orderMapper.selectOrderStatus(userId,orderNo);
        if(order==null){
            return  ServerResponse.createServerResponseByFail(ResponseCode.ERROR,"订单不存在");
        }
        //step3:判断订单状态并取消
        if(order.getStatus()!=Const.OrderStatusEnum.ORDER_UN_PAY.getCode()){
            return  ServerResponse.createServerResponseByFail(ResponseCode.ERROR,"订单不可取消");
        }
        //step4:返回结果
        order.setStatus(Const.OrderStatusEnum.ORDER_CANCELED.getCode());
        int result= orderMapper.updateByPrimaryKey(order);
        if(result>0){
            return ServerResponse.createServerResponseBySucess();
        }
        return ServerResponse.createServerResponseByFail(ResponseCode.ERROR,"取消订单失败");
    }

    @Override
    public ServerResponse orderPay(Integer userId, Long orderNo) {
        if(orderNo == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.ERROR,"订单编号不能为空");
        }
      Order order =  orderMapper.selectOrderStatus(userId,orderNo);
        if(order==null){
            return ServerResponse.createServerResponseByFail(ResponseCode.ERROR,"支付订单不存在");
        }
        return pay(order);
    }

    @Override
    public ServerResponse updateOrder(Integer userId, Long orderNo) {//支付完成后修改订单状态
        return null;
    }

    @Override
    public String paySuccess(Map<String, String> signParams) {
        //step1:获取各个参数信息
        //订单号
        String orderNo = signParams.get("out_trade_no");
        //流水号
        String trade_no = signParams.get("trade_no");
        //支付状态
        String trade_status = signParams.get("trade_status");
        //付款时间
        String payment_time = signParams.get("gmt_payment");

        //step2:根据订单号查询订单
        Order order = orderMapper.findOrderByOrderNo(Long.parseLong(orderNo));
        if (order == null) {
            return "fail";
        }

        if (trade_status.equals("TRADE_SUCCESS")) {
            //支付成功
            //修改订单状态
            Order order1 = new Order();
            order1.setUserId(order.getUserId());
            order1.setShippingId(order.getShippingId());
            order1.setPayment(order.getPayment());
            order1.setPaymentType(order.getPaymentType());
            order1.setPostage(order.getPostage());
            order1.setOrderNo(Long.parseLong(orderNo));
            order1.setStatus(Const.OrderStatusEnum.ORDER_PAYED.getCode());
            order1.setPaymentTime(DateUtils.strToDate(payment_time));
            int result = orderMapper.updateOrderStatusAndPaymentTimeByOrderNo(order1);
            if (result <= 0) {
                return "fail";
            }
        }
            /**添加支付记录
             * 用户进行订单支付时就会存入pay_info表中，此时订单状态是 等待用户支付
             * 当用户支付成功时应该将之前的支付信息表中该订单的状态由
             * 等待用户支付 修改为 已经交易成功
             */
            PayInfo payInfo=new PayInfo();
            payInfo.setOrderNo(Long.parseLong(orderNo));
            payInfo.setUserId(order.getUserId());
            payInfo.setPayPlatform(Const.PaymentEnum.ONLINE.getCode());
            payInfo.setPlatformNumber(trade_no);
            payInfo.setPlatformStatus(trade_status);

            int pay_result=payInfoMapper.insert(payInfo);
            if(pay_result<=0){
                return "fail";
            }
            //System.out.println("订单支付流程走通");
            return "success";
    }


    private OrderVO assembleOrderVO(Order order, List<OrderItem> orderItemList, Integer shippingId){
        OrderVO orderVO=new OrderVO();

        List<OrderItemVO> orderItemVOList=Lists.newArrayList();
        for(OrderItem orderItem:orderItemList){
            OrderItemVO orderItemVO= assembleOrderItemVO(orderItem);
            orderItemVOList.add(orderItemVO);
        }
        orderVO.setOrderItemVoList(orderItemVOList);
        orderVO.setImageHost(imageHost);
        Shipping shipping=(Shipping)addressService.select(shippingId).getData();
        if(shipping!=null){
            orderVO.setShippingId(shippingId);
            ShippingVO shippingVO= assmbleShippingVO(shipping);
            orderVO.setShippingVo(shippingVO);
            orderVO.setReceiverName(shipping.getReceiverName());
        }

        orderVO.setStatus(order.getStatus());
        Const.OrderStatusEnum orderStatusEnum= Const.OrderStatusEnum.codeOf(order.getStatus());
        if(orderStatusEnum!=null){
            orderVO.setStatusDesc(orderStatusEnum.getDesc());
        }

        orderVO.setPostage(0);
        orderVO.setPayment(order.getPayment());
        orderVO.setPaymentType(order.getPaymentType());
        Const.PaymentEnum paymentEnum=Const.PaymentEnum.codeOf(order.getPaymentType());
        if(paymentEnum!=null){
            orderVO.setPaymentTypeDesc(paymentEnum.getDesc());
        }
        orderVO.setOrderNo(order.getOrderNo());



        return orderVO;
    }

    private ShippingVO assmbleShippingVO(Shipping shipping){
        ShippingVO shippingVO=new ShippingVO();

        if(shipping!=null){
            shippingVO.setReceiverAddress(shipping.getReceiverAddress());
            shippingVO.setReceiverCity(shipping.getReceiverCity());
            shippingVO.setReceiverDistrict(shipping.getReceiverDistrict());
            shippingVO.setReceiverMobile(shipping.getReceiverMobile());
            shippingVO.setReceiverName(shipping.getReceiverName());
            shippingVO.setReceiverPhone(shipping.getReceiverPhone());
            shippingVO.setReceiverProvince(shipping.getReceiverProvince());
            shippingVO.setReceiverZip(shipping.getReceiverZip());
        }
        return shippingVO;
    }


    private OrderItemVO assembleOrderItemVO(OrderItem orderItem){
        OrderItemVO orderItemVO=new OrderItemVO();

        if(orderItem!=null){

            orderItemVO.setQuantity(orderItem.getQuantity());
            orderItemVO.setCreateTime(DateUtils.dateToStr(orderItem.getCreateTime()));
            orderItemVO.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
            orderItemVO.setOrderNo(orderItem.getOrderNo());
            orderItemVO.setProductId(orderItem.getProductId());
            orderItemVO.setProductImage(orderItem.getProductImage());
            orderItemVO.setProductName(orderItem.getProductName());
            orderItemVO.setTotalPrice(orderItem.getTotalPrice());

        }

        return orderItemVO;
    }

    /**
     * 清空购物车中已选中的商品
     * */

    private  ServerResponse  cleanCart(List<Cart> cartList){

        if(cartList!=null&&cartList.size()>0){
            return cartService.batchDelete(cartList);
        }
        return ServerResponse.createServerResponseByFail(ResponseCode.ERROR,"购物车为空");
    }



    /**
     * 扣库存
     * */
    private  void  reduceProductStock(List<OrderItem> orderItemList){

        if(orderItemList!=null&&orderItemList.size()>0){

            for(OrderItem orderItem:orderItemList){
                Integer productId= orderItem.getProductId();
                Integer quantity=orderItem.getQuantity();
                Product product=(Product)productService.findProductByProductId(productId).getData();
                product.setStock(product.getStock()-quantity);
                productService.addOrUpdate(product);
            }

        }

    }


    /**
     * 计算订单的总价格
     * */
    private  BigDecimal getOrderPrice(List<OrderItem> orderItemList){

        BigDecimal bigDecimal=new BigDecimal("0");

        for(OrderItem orderItem:orderItemList){
            bigDecimal= BigDecimalUtils.add(bigDecimal.doubleValue(),orderItem.getTotalPrice().doubleValue());
        }

        return bigDecimal;
    }


    /**
     * 创建订单
     * */

    private Order createOrder(Integer userId, Integer shippingId, BigDecimal orderTotalPrice){
        Order order=new Order();
        order.setOrderNo(generateOrderNO());
        order.setUserId(userId);
        order.setShippingId(shippingId);
        order.setStatus(10);//未付款
        //订单金额
        order.setPayment(orderTotalPrice);
        order.setPostage(0);
        order.setPaymentType(1);//线上支付，应该用枚举

        //保存订单
        int result=orderMapper.insert(order);
        if(result>0){
            return order;
        }
        return  null;
    }

    /**
     *
     * 生成订单编号
     * */
    private  Long generateOrderNO(){

        return System.currentTimeMillis()+new Random().nextInt(100);
    }

    private  ServerResponse getCartOrderItem(Integer userId,List<Cart> cartList){

        if(cartList==null||cartList.size()==0){
            return ServerResponse.createServerResponseByFail(ResponseCode.ERROR,"购物车空");
        }
        List<OrderItem> orderItemList= Lists.newArrayList();

        for(Cart cart:cartList){

            OrderItem orderItem=new OrderItem();
            orderItem.setUserId(userId);
            Product product=(Product)productService.findProductByProductId(cart.getProductId()).getData();
            if(product==null){
                return  ServerResponse.createServerResponseByFail(ResponseCode.ERROR,"此商品不存在");
            }
            if(product.getStatus()!= 1){//商品下架
                return ServerResponse.createServerResponseByFail(ResponseCode.ERROR,"商品已经下架");
            }
            if(product.getStock()<cart.getQuantity()){//库存不足
                return ServerResponse.createServerResponseByFail(ResponseCode.ERROR,"商品库存不足");
            }
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setCurrentUnitPrice(product.getPrice());
            orderItem.setProductId(product.getId());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setProductName(product.getName());
            orderItem.setTotalPrice(BigDecimalUtils.mul(product.getPrice().doubleValue(),cart.getQuantity().doubleValue()));

            orderItemList.add(orderItem);
        }

        return  ServerResponse.createServerResponseBySucess("订单编号生成成功",orderItemList);
    }

    private static Log log = LogFactory.getLog(Main.class);

    // 支付宝当面付2.0服务
    private static AlipayTradeService tradeService;

    // 支付宝当面付2.0服务（集成了交易保障接口逻辑）
    private static AlipayTradeService   tradeWithHBService;

    // 支付宝交易保障接口服务，供测试接口api使用，请先阅读readme.txt
    private static AlipayMonitorService monitorService;

    static {
        /** 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
         *  Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
         */
        Configs.init("zfbinfo.properties");

        /** 使用Configs提供的默认参数
         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
         */
        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();

        // 支付宝当面付2.0服务（集成了交易保障接口逻辑）
        tradeWithHBService = new AlipayTradeWithHBServiceImpl.ClientBuilder().build();

        /** 如果需要在程序中覆盖Configs提供的默认参数, 可以使用ClientBuilder类的setXXX方法修改默认参数 否则使用代码中的默认设置 */
        monitorService = new AlipayMonitorServiceImpl.ClientBuilder()
                .setGatewayUrl("http://mcloudmonitor.com/gateway.do").setCharset("GBK")
                .setFormat("json").build();
    }

    // 简单打印应答
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
           log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            log.info("body:" + response.getBody());
        }
    }


    // 测试当面付2.0生成支付二维码
    public ServerResponse pay(Order order) {
        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = String.valueOf(order.getOrderNo());

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = "【瑞乐购】网上支付平台";

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = order.getPayment().toString();

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = "购买商品共"+order.getPayment()+"元";

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";


        List<OrderItem> orderItemList = orderItemMapper.selectByPrimaryKey(order.getUserId(),order.getOrderNo());
        if(orderItemList==null||orderItemList.size()==0){
            return ServerResponse.createServerResponseByFail(ResponseCode.ERROR,"没有可以购买的商品");
        }

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();

        for(OrderItem orderItem:orderItemList){
            // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail

            GoodsDetail goods=GoodsDetail.newInstance(String.valueOf(orderItem.getProductId()),orderItem.getProductName(),orderItem.getCurrentUnitPrice().intValue(),
                    orderItem.getQuantity());
            goodsDetailList.add(goods);
        }


        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
                .setTimeoutExpress(timeoutExpress)
                .setNotifyUrl("http://gqyku5.natappfree.cc/order/callback.do")//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                .setGoodsDetailList(goodsDetailList);

        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);

                // 需要修改为运行机器上的路径
                String filePath = String.format("/C:/upload/qr-%s.png",
                        response.getOutTradeNo());
                log.info("filePath:" + filePath);
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, filePath);
                PayVO payVO=new PayVO(order.getOrderNo(),imageHost+"qr-"+ response.getOutTradeNo()+".png");
                return ServerResponse.createServerResponseBySucess(null,payVO);

            case FAILED:
                log.error("支付宝预下单失败!!!");
                break;

            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");
                break;

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                break;

        }
        return ServerResponse.createServerResponseByFail(ResponseCode.ERROR,"下单失败");
    }

}
