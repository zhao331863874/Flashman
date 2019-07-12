package com.ddinfo.flashman.constant;

import com.ddinfo.flashman.BuildConfig;

/**
 * Created by ailen on 2015/10/30.
 */
public class UrlConstant {
    //    public static final String BASE_URL = "http://192.168.1.251:50000";
    //public static final String BASE_URL = "http://211.152.32.59:50000";
    public static final String BASE_URL = "http://192.168.1.54:50000";//程飞
    //    public static final String BASE_URL = "http://192.168.1.159:50000";//张思逸
//        public static final String BASE_URL = "http://psappapi.dd528.com";
    //    public static final String TOKEN = "/v1/salesman/login";
    /*****************************
     * H5 url
     * 余额更变明细：http://192.168.1.74:50000/H5/balance
     * 提现历史：http://192.168.1.74:50000/H5/extract
     * 冻结明细：
     **************************/
    public static final String H5_BALANCE_DETAIL = BuildConfig.API_SERVER_URL + "/H5/balance";
    public static final String H5_EXTRACE_HISTORY = BuildConfig.API_SERVER_URL + "/H5/extract";
    public static final String H5_FREEZE_DETAIL = BuildConfig.API_SERVER_URL + "/H5/freeze";
    //配送
    //    登录
    public static final String TOKEN = "/v1/user/login";
    //    注册
    public static final String REGISTER = "/v1/user/register";
    public static final String ATTACH_USER_INFO = "/v2/user/userInfo";
    //    获取验证码
    public static final String SENDCODE = "/v1/user/sendCode";
    //    获取提现验证码
    public static final String SEND_ENCASH_CODE = "/v1/validateCode/encash";
    //    抢单列表
    public static final String SECKILLLIST = "/v1/order/seckill/list";
    //    接单
    public static final String SECKILL = "/v1/order/seckill";
    //    任务列表
    public static final String LISTORDERS = "/v1/order/delivery/listOrders";
    //任务列表V2
    public static final String LISTORDERS2 = "/v2/order/delivery/listOrders";
    //    历史任务列表
    public static final String HISTORYORDERS = "/v1/order/delivery/historyOrder";
    // 退货单列表
    public static final String RETURN_BACK_ORDERS = "/v2/order/backGoods/list";
    //取退货接口
    public static final String RETURN_BACK_GOODS = "/v2/order/backGoods/backGoods";
    //退货取货 退款金额计算
    public static final String CALCULATE_BACK_GOODS = "/v2/order/backGoods/calculateBackSum";

    // 订单详情（V2）
    public static final String DELIVERY_ORDER = "/v2/order/delivery/detail";
    //    订单详情(V1)
    public static final String ORDERS_DETAIL = "/v1/order/seckill/detail";
    //    货款列表
    public static final String GOODSLIST = "/v1/pay/goods/list";
    //    接单仓信息
    public static final String WAREHOUSEINFO = "/v1/user/warehouseInfo";
    //    取货
    public static final String GOODSGET = "/v1/order/delivery/getGoods";
    //   退货单明细
    public static final String RETURN_BACK_ORDER_DETAIL = "/v2/order/backGoods/listDetail";
    //    拒收
    public static final String GOODSREFUSE = "/v1/order/delivery/refuse";
    //拒收V2
    public static final String GOODSREFUSE2 = "/v2/order/delivery/refuse";
    //    送达
    public static final String GOODSSEND = "/v1/order/delivery/sendComplete";

    public static final String BIND_WAREHOUSE = " /v2/user/bindWarehouse"; ///v1/user/bindWarehouse

    //我的钱包
    public static final String MY_WALLET = "/v1/wallet";
    //提现
    public static final String ENCASH = "/v1/wallet/balance/encash";
    //押金转入
    public static final String BALANCE_DEPOSITIN = "/v1/wallet/balance/depositIn";
    //获取变更列表
    public static final String BALANCE_RECORDS = "/v1/wallet/balance/records";
    //获取提现账户列表
    public static final String BALANCE_ACCOUNT_LIST = "/v1/wallet/balance/tradeAccountList";
    //设置交易账号-绑定支付宝
    public static final String BALANCE_ADDPLIPAY_ACCOUNT = "/v1/wallet/balance/addAlipayAccount";
    //设置交易账号-绑定支付宝
    public static final String BALANCE_DELLPLIPAY_ACCOUNT = "/v1/wallet/balance/deleteAlipayAccount";
    //余额详情
    public static final String BALANCE_DETAIL = "/v1/wallet/balance/detail";

    //提交付款
    public static final String GOODS_PAY = "/v1/pay/batchOrder/submit";
    public static final String GOODS_PAY_WEIJIN = "/v1/pay/batchOrder/submitWithWeiJin";

    //充值
    public static final String RECHARGE = "/v1/wallet/deposit/recharge";
    //冻结款明细
    public static final String DETAILS_OF_FROZEN = "/v1/wallet/deposit/frozenDetails";
    //客服电话
    public static final String CALLPHONE = "/v1/setting/telephone";

    //查询支付二维码状态
    public static final String CHECKPAYORDERSTATUS = "/v1/order/delivery/checkPayOrderStatue";
    //查看交货款订单状态
    public static final String CHECK_BATCH_PAY_ORDER_STATUS = "v1/pay/batchOrder/checkbatchPayOrderState";

    public static final String GETBOARDLIST = "/v1/order/dashboard/orderStatistic";
    //交货款单列表
    public static final String GETBATCHORDERLIST = "/v1/order/batchOrder/list";
    //交货款单详情
    public static final String GETBATCHORDERDETAIL = "/v1/order/batchOrder/detail";
    //生成交货款单
    public static final String CREATEBATCHORDER = "/v1/order/batchOrder/create";

    //部分送达
    public static final String ORDER_PART_SEND = "/v2/order/delivery/goodDetail";
    //部分送达数量确认
    public static final String ORDER_PART_SEND_CONFIRM = "/v2/order/delivery/rejectPreview";
    //拒收原因支付确认
    public static final String ORDER_PART_SEND_DONE = "/v2/order/delivery/partArrive";

}
