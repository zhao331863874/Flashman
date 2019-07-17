package com.ddinfo.flashman.network;

import com.ddinfo.flashman.constant.UrlConstant;
import com.ddinfo.flashman.models.AttachUserInfo;
import com.ddinfo.flashman.models.AttachUserInfoEntity;
import com.ddinfo.flashman.models.BalanceAccountListEntity;
import com.ddinfo.flashman.models.BalanceDetailEntity;
import com.ddinfo.flashman.models.BaseResponseEntity;
import com.ddinfo.flashman.models.BatchOrderEntity;
import com.ddinfo.flashman.models.BindWareHouseEntity;
import com.ddinfo.flashman.models.BoardEntity;
import com.ddinfo.flashman.models.CallPhoneEntity;
import com.ddinfo.flashman.models.CheckPayStatusEntity;
import com.ddinfo.flashman.models.ChildManEntity;
import com.ddinfo.flashman.models.DetailOfFrozenEntity;
import com.ddinfo.flashman.models.FrozenDetailsEntity;
import com.ddinfo.flashman.models.OrderDetailEntity;
import com.ddinfo.flashman.models.OrderDetailEntityV1;
import com.ddinfo.flashman.models.PartSendConfirmEntity;
import com.ddinfo.flashman.models.PartSendListEntity;
import com.ddinfo.flashman.models.PayOrderResponseEntity;
import com.ddinfo.flashman.models.PayOrderResult;
import com.ddinfo.flashman.models.PayResultEntity;
import com.ddinfo.flashman.models.PaymentDetailEntity;
import com.ddinfo.flashman.models.PaymentEntity;
import com.ddinfo.flashman.models.PaymentListEntity;
import com.ddinfo.flashman.models.RecordsEntity;
import com.ddinfo.flashman.models.ReturnBackGroupOrderListEntity;
import com.ddinfo.flashman.models.RouteEntity;
import com.ddinfo.flashman.models.RouteOrderEntity;
import com.ddinfo.flashman.models.SeckillOrderEntity;
import com.ddinfo.flashman.models.SeckillOrderList;
import com.ddinfo.flashman.models.TokenEntity;
import com.ddinfo.flashman.models.WalletEntity;
import com.ddinfo.flashman.models.WareHouseInfoEntity;
import com.ddinfo.flashman.models.params.AddAlipayAccountParams;
import com.ddinfo.flashman.models.params.BackGoodsParams;
import com.ddinfo.flashman.models.params.BindWareHouseParams;
import com.ddinfo.flashman.models.params.CheckPayStatusParams;
import com.ddinfo.flashman.models.params.CreateBatchOrderParams;
import com.ddinfo.flashman.models.params.CreditManParams;
import com.ddinfo.flashman.models.params.DeliveryManIdParams;
import com.ddinfo.flashman.models.params.DeliveryOrderParams;
import com.ddinfo.flashman.models.params.DepositInParams;
import com.ddinfo.flashman.models.params.GoodsGetParams;
import com.ddinfo.flashman.models.params.GoodsPayParams;
import com.ddinfo.flashman.models.params.GoodsRefuseParams;
import com.ddinfo.flashman.models.params.GoodsRefuseV2Params;
import com.ddinfo.flashman.models.params.GoodsSendParams;
import com.ddinfo.flashman.models.params.IdParams;
import com.ddinfo.flashman.models.params.LoginParams;
import com.ddinfo.flashman.models.params.OrderCancelParams;
import com.ddinfo.flashman.models.params.PartSendDoneParams;
import com.ddinfo.flashman.models.params.PaymentListParams;
import com.ddinfo.flashman.models.params.RechargeParams;
import com.ddinfo.flashman.models.params.RegisterParams;
import com.ddinfo.flashman.models.params.SeckillParams;
import com.ddinfo.flashman.models.params.SendCodeParams;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by ailen on 2015/10/30.
 */
public interface WebService {
    //登陆获取Token
    @POST(UrlConstant.TOKEN)
    Call<BaseResponseEntity<TokenEntity>> login(@Body LoginParams loginParams);

    @POST(UrlConstant.REGISTER)
    Call<BaseResponseEntity<TokenEntity>> register(@Body RegisterParams registerParams);
    //获取验证码
    @POST(UrlConstant.SENDCODE)
    Call<BaseResponseEntity> sendCode(@Body SendCodeParams sendCodeParams);

    @GET(UrlConstant.SECKILLLIST)
    Call<BaseResponseEntity<ArrayList<SeckillOrderList>>> getSeckillList(@Header("Authorization") String authorization,
                                                                         @Query("offset") int offset, @Query("limit") int limit);

    @POST(UrlConstant.SECKILL)
    Call<BaseResponseEntity> seckill(@Header("Authorization") String authorization, @Body SeckillParams params);

    //    @GET(UrlConstant.LISTORDERS)
    //    Call<BaseResponseEntity<ArrayList<SeckillOrderList>>> getOrderList(
    //            @Header("Authorization") String authorization,
    //            @Query("state") int state
    ////            ,
    ////            @Query("offset") int offset,
    ////            @Query("limit") int limit
    //    );

    @GET(UrlConstant.LISTORDERS2)
    Call<BaseResponseEntity<ArrayList<SeckillOrderList>>> getOrderList(@Header("Authorization") String authorization,
                                                                       @Query("flag") int flag, @Query("lat") double lat, @Query("lon") double lon, @Query("state") int state
                                                                       //,
                                                                       //@Query("offset") int offset,
                                                                       //@Query("limit") int limit
    );

    @GET(UrlConstant.HISTORYORDERS)
    Call<BaseResponseEntity<SeckillOrderEntity>> getHistoryOrderList(@Header("Authorization") String authorization, @Query("flag") int flag,
                                                                     @Query("state") int state, @Query("offset") int offset, @Query("limit") int limit);

    @GET(UrlConstant.GOODSLIST)
    Call<BaseResponseEntity<ArrayList<PaymentEntity>>> getPayments(@Header("Authorization") String authorization,
                                                                   @Query("offset") int offset, @Query("limit") int limit);
    //接单仓信息
    @GET(UrlConstant.WAREHOUSEINFO)
    Call<BaseResponseEntity<WareHouseInfoEntity>> getWareHouseInfo(@Header("Authorization") String authorization);
    //获取个人信息
    @GET(UrlConstant.ATTACH_USER_INFO)
    Call<BaseResponseEntity<AttachUserInfoEntity>> getAttachUserInfo(@Header("Authorization") String authorization);
    //提交个人信息
    @POST(UrlConstant.ATTACH_USER_INFO)
    Call<BaseResponseEntity> uploadAttachUserInfo(@Header("Authorization") String authorization, @Body AttachUserInfo entity);

    //订单详情
    @POST(UrlConstant.DELIVERY_ORDER)
    Call<BaseResponseEntity<OrderDetailEntity>> getDeliveryOrder(@Header("Authorization") String authorization,
                                                                 @Body DeliveryOrderParams params);
    //取货请求
    @POST(UrlConstant.GOODSGET)
    Call<BaseResponseEntity> goodsGet(@Header("Authorization") String authorization, @Body GoodsGetParams params);
    //拒收请求
    @POST(UrlConstant.GOODSREFUSE2)
    Call<BaseResponseEntity<PayResultEntity>> goodsRefuse(@Header("Authorization") String authorization,
                                                          @Body GoodsRefuseV2Params params);

    @POST(UrlConstant.GOODSREFUSE)
    Call<BaseResponseEntity<PayResultEntity>> goodsRefuse(@Header("Authorization") String authorization,
                                                          @Body GoodsRefuseParams params);
    //送达请求
    @POST(UrlConstant.GOODSSEND)
    Call<BaseResponseEntity<PayResultEntity>> goodsSend(@Header("Authorization") String authorization,
                                                        @Body GoodsSendParams params);

    //我的钱包
    @GET(UrlConstant.MY_WALLET)
    Call<BaseResponseEntity<WalletEntity>> myWalletInfo(@Header("Authorization") String authorization);

    //押金转入
    @FormUrlEncoded
    @POST(UrlConstant.BALANCE_DEPOSITIN)
    Call<BaseResponseEntity> balanceDepositin(@Header("Authorization") String authorization, @Field("sum") double sum);
    //获取提现验证码
    @POST(UrlConstant.SEND_ENCASH_CODE)
    Call<BaseResponseEntity> sendEncashCode(@Header("Authorization") String authorization);

    //提现
    @POST(UrlConstant.ENCASH)
    Call<BaseResponseEntity> encash(@Header("Authorization") String authorization, @Body DepositInParams params);

    //充值
    //balance余额 wechat微信 alipay支付宝
    @POST(UrlConstant.GOODS_PAY)
    Call<BaseResponseEntity<PayOrderResponseEntity>> goodsPay(@Header("Authorization") String authorization,
                                                              @Body GoodsPayParams params);
    //维金二维码地址
    @POST(UrlConstant.GOODS_PAY_WEIJIN)
    Call<BaseResponseEntity<PayOrderResponseEntity>> goodsPayWeiJin(@Header("Authorization") String authorization,
                                                              @Body GoodsPayParams params);
    //充值
    //balance余额 wechat微信 alipay支付宝
    @POST(UrlConstant.RECHARGE)
    Call<BaseResponseEntity<PayOrderResponseEntity>> recharge(@Header("Authorization") String authorization,
                                                              @Body RechargeParams params);

    ///设置交易账号-绑定支付宝
    @POST(UrlConstant.BALANCE_ADDPLIPAY_ACCOUNT)
    Call<BaseResponseEntity> addAlipayAccount(@Header("Authorization") String authorization,
                                              @Body AddAlipayAccountParams params);

    ///设置交易账号-绑定支付宝
    @FormUrlEncoded
    @POST(UrlConstant.BALANCE_DELLPLIPAY_ACCOUNT)
    Call<BaseResponseEntity> delAlipayAccount(@Header("Authorization") String authorization, @Field("id") String id);

    //获取提现账户列表
    @GET(UrlConstant.BALANCE_ACCOUNT_LIST)
    Call<BaseResponseEntity<ArrayList<BalanceAccountListEntity>>> balanceAccountList(
            @Header("Authorization") String authorization);

    //冻结款明细
    @GET(UrlConstant.DETAILS_OF_FROZEN)
    Call<BaseResponseEntity<ArrayList<DetailOfFrozenEntity>>> detailOfFrozen(
            @Header("Authorization") String authorization);

    //获取变更列表
    @GET(UrlConstant.BALANCE_RECORDS)
    Call<BaseResponseEntity<ArrayList<RecordsEntity>>> balanceRecords(@Header("Authorization") String authorization
            , @Query("offset") int offset, @Query("limit") int limit, @Query("type") int type, @Query("target") int target);

    //余额详情
    @POST(UrlConstant.BALANCE_DETAIL)
    Call<BaseResponseEntity<BalanceDetailEntity>> balanceDetail(@Header("Authorization") String authorization);

    //绑定仓库
    @POST(UrlConstant.BIND_WAREHOUSE)
    Call<BaseResponseEntity<BindWareHouseEntity>> bindWareHouse(@Header("Authorization") String authorization,
                                                                @Body BindWareHouseParams params);

    //冻结款明细
    @GET(UrlConstant.CALLPHONE)
    Call<BaseResponseEntity<CallPhoneEntity>> callPhone(@Header("Authorization") String authorization);

    //订单详情V1
    @GET(UrlConstant.ORDERS_DETAIL)
    Call<BaseResponseEntity<OrderDetailEntityV1>> callOrderDetail(@Header("Authorization") String authorization,
                                                                  @Query("deliveryOrderId") int deliveryOrderId);
    //查询支付二维码状态
    @POST(UrlConstant.CHECKPAYORDERSTATUS)
    Call<BaseResponseEntity<CheckPayStatusEntity>> CheckPayOrderStatus(@Body CheckPayStatusParams params);

    @GET(UrlConstant.CHECK_BATCH_PAY_ORDER_STATUS)
    Call<BaseResponseEntity<PayOrderResult>> checkBatchPayOrderStatus(@Query("tradeRecordNo") String tradeRecordNo);

    @GET(UrlConstant.GETBOARDLIST)
    Call<BaseResponseEntity<ArrayList<BoardEntity>>> getBoardList();

    @GET(UrlConstant.GETBOARDLIST)
    Call<BaseResponseEntity<ArrayList<BoardEntity>>> getDeliveryBoardList(@Query("deliveryManId") int delivery);

    @POST(UrlConstant.GETBATCHORDERLIST)
    Call<BaseResponseEntity<ArrayList<PaymentListEntity>>> getPaymentList(@Body PaymentListParams params);

    @POST(UrlConstant.GETBATCHORDERDETAIL)
    Call<BaseResponseEntity<PaymentDetailEntity>> getPaymentDetail(@Body IdParams idParams);

    @POST(UrlConstant.CREATEBATCHORDER)
    Call<BaseResponseEntity<BatchOrderEntity>> createBatchOrder(@Body CreateBatchOrderParams params);

    //部分送达
    @POST(UrlConstant.ORDER_PART_SEND)
    Call<BaseResponseEntity<PartSendListEntity>> getPartSendList(@Body PartSendDoneParams map);

    //部分送达商品数量提交
    @POST(UrlConstant.ORDER_PART_SEND_CONFIRM)
    Call<BaseResponseEntity<PartSendConfirmEntity>> getPartSendConfirmList(@Body PartSendDoneParams map);

    //部分送达商品数量提交
    @POST(UrlConstant.ORDER_PART_SEND_DONE)
    Call<BaseResponseEntity<PayResultEntity>> getPartSendDone(@Body PartSendDoneParams map);

    //取货退货接口
    @POST(UrlConstant.RETURN_BACK_GOODS)
    Call<BaseResponseEntity> returnBackOrder(@Body BackGoodsParams params);

    //退货取货 金额计算
    @POST(UrlConstant.CALCULATE_BACK_GOODS)
    Call<BaseResponseEntity<Map>> calculateBackGoods(@Body BackGoodsParams params);

    //退货单明细
    @GET(UrlConstant.RETURN_BACK_ORDER_DETAIL)
    Call<BaseResponseEntity<ReturnBackGroupOrderListEntity>> getBackOrderListDetail(@Query("backOrderId") int backOrderId);

    //查询核销支付方式
    @GET("/v1/setting/payMethod")
    Call<BaseResponseEntity<ArrayList<String>>> callPayMethod(@Query("type") String type);

    //取消收货
    @POST("/v1/order/seckill/cancel")
    Call<BaseResponseEntity> orderCancel(@Body OrderCancelParams map);

    @GET("/v2/order/delivery/unHandleList")
    Call<BaseResponseEntity<ArrayList<RouteEntity>>> getRouteList();

    @GET("/v2/order/delivery/unHandleDetail")
    Call<BaseResponseEntity<ArrayList<RouteOrderEntity>>> getRouteOrderList(@Query("routeId") int routeId);

    @POST("/v1/user/bindParentMan")
    Call<BaseResponseEntity> bindParentMan(@Body DeliveryManIdParams deliveryManId);

    @GET("/v1/user/childManList")
    Call<BaseResponseEntity<ArrayList<ChildManEntity>>> getChildManList();

    @GET("/v1/wallet/deposit/frozenDetails")
    Call<BaseResponseEntity<ArrayList<FrozenDetailsEntity>>> getFrozenDetail(@Query("deliveryManId") int deliveryManId);

    @GET("/v1/wallet/deposit/frozenDetails")
    Call<BaseResponseEntity<ArrayList<FrozenDetailsEntity>>> getFrozenDetail();

    @POST("/v1/wallet/balance/creditMan")
    Call<BaseResponseEntity> creditMan(@Body CreditManParams params);


}
