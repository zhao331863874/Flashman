package com.ddinfo.flashman.constant;

import android.os.Environment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weitf on 2016/10/10.
 * 应用内 所使用的 常量
 */
public class ExampleConfig {
  //    public static final serviceId = 126838;//測試
  //    public static final serviceId = 128000;//預發佈
  public static final int serviceId = 128004;//生产
  public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().toString();
  public static final String BASE_NAME = "Flashman";
  public static final String BASE_DIR_NAME = "/" + BASE_NAME + "/";
  public static final String PIC_NAME = "PIC_FILE";
  public static final String PIC_DIR_NAME = BASE_DIR_NAME + PIC_NAME + "/";
  public static final String TOKENPRENAME = "TokenPreName";//token值
  public static final String LicenseKey = "a6a612a3435940ada8f9e9a5a502fbaa";//听云 key
  public static final int WAIT_TIME = 500;//
  public static final String SALES_SHARE = "flash_share";
  public static final String LOGIN_EMAIL = "email";
  public static final String LOGIN_PASSWORD = "passwrod";
  public static final String SHOP_RIGHT_SELECT = "shop_list_right_select";
  public static final String DDMALL_FIRST_OPEN = "ddmall_first_open";
  public static final String VERSION_CODE = "version_code";
  public static final String TOKEN_CACHE = "token_cache";
  public static final String PHONE_CACHE = "phone_cache";
  public static final String PWD_CACHE = "pwd_cache";
  public static final String TYPE_UNUSED = "unused";
  public static final String TYPE_USED = "used";
  public static final String TYPE_OVERTIME = "overtime";
  public static final String TYPE_USENOW = "useNow";
  public static final String SEARCH_RECORD = "search_record";
  public static final String intentGoodsId = "goodsId";
  public static final String intentNoticeId = "noticeId";
  public static final String intentKeyword = "keyWord";
  public static final String SHOP_DETAILS = "shopDetails";
  public static final String STORE_SIGN_DETAILS = "storeSigndetail";
  public static final String STORE_MESSAGE = "shoreMessage";
  public static final String PUT_KEY_MID = "Mid";
  public static final String SIGN_IN_PNG = "thumbImgIn.png";
  public static final String SIGN_OUT_PNG = "thumbImgOut.png";
  public static final int intentSignIn = 1;
  public static final int intentCheck = 2;
  public static final int DB_SHOPPING_CART_ADD = 0x0023;
  public static final int DB_SHOPPING_CART_UPDATE = 0x0013;
  public static final int DB_SHOPPING_CART_DELATE = 0x0073;
  public static final int RESPONSE_STATUS_SUCCESS = 1;//网络接口请求返回成功
  public static final int RESPONSE_STATUS_TOKEN_FAIL = -1;//网络接口请求返回 请求token 失败
  public static final String phoneNumber = "18118609656";
  public static final String ISUPDATECANCEL = "isUpdateCancel";
  public static final String PHOTOS_CACHE_SP = "PHOTOS_CACHE_SP";
  public static final String intentType = "intentType";
  public static final int intentTypeCheck = 1001;
  public static final int intentTypeAdd = 1002;
  public static final String intentStockId = "intentStockId";
  public static final String intentStockCreateTime = "intentStockCreateTime";
  public static final String DATA = "data";
  public static String token = "";//token值
  public static List<String> listSearchRecord = new ArrayList<>();//搜索结果缓存
  public static String LAST_ADDRESS = "lastAddress";
  public static String orderId = "";//订单Id
  public static boolean isTicketUsable = false;
  public static int ticketUsebaseline = 0;
  public static String codeString = "";
  public static double ticketValue = 0;
  public static int numCart = 0;
  public static double priceAll = 0;
  public static double enjoyFavourableAll = 0;//可享受优惠价格
  public static int submitCartLimitPrice = 1000000;//提交限制金额
  public static String messageId = "";
  public static boolean isNewReg = false;
  public static int countNewMessage = 0;
  public static double dadouAccountAll = 0;
  public static boolean isDownload = false;
  public static boolean isDebug = false;
  public static int tempOrderCount = 0;
  public static double tempOrderPrice = 0.00;
  public static boolean isShutdown = false;
  public static String LAUNCH_AD_IMG_URL = "";
  public static boolean LAUNCH_AD_IS_DOWNLOADED = false;

  public static final String WB_URL_KEY = "urlStr";
  public static final String WB_NAV_TITLE_KEY = "title";

  //配送
  public static final int VIEWHOLDER_TOP = 1001;
  public static final int VIEWHOLDER_HEAD = 1002;
  public static final int VIEWHOLDER_NORMAL = 1003;
  public static final int VIEWHOLDER_RETURN_BACK = 1005;
  public static final int VIEWHOLDER_FOOT = 1004;
  public static final String TOKEN = "token";
  public static final String USER_PHONE = "USER_PHONE";
  public static final String TASK_DETAIL_TYPE = "TASK_DETAIL_TYPE";
  public static final String ACTIVITY_TYPE = "ACTIVITY_TYPE";
  public static final String ACTIVITY_FLAG = "ACTIVITY_FLAG";

  public static final String TELEPHONE = "TELEPHONE";
  public static String telephone = "";
  public static String email = "";
  public static final String EMAIL = "EMAIL";
  public static final String DELIVERY_ID = "DELIVERY_ID";
  public static final String NUMBER_ID = "NUMBER_ID";
  public static final String ORDER_ID = "ORDER_ID";
  public static final String INVOICENUMBER_ID = "INVOICENUMBER_ID";
  public static final String QRCODE = "QRCODE";
  public static final String TYPE_CAMERA = "TYPE_CAMERA";
  public static final String ID = "ID";

  public static final String AUTH_CODE_SECOND = "AUTH_CODE_SECOND";
  public static final String PAY_TIME = "PAY_TIME";

  public static final String INTENT_TOPUPMONEY = "INTENT_TOPUPMONEY";
  public static final String INTENT_DELIVERY_IDS_NEY = "deliveryOrderIdsKEY";

  public static final String INTENT_TYPE = "INTENT_TYPE";
  public static final int INTENT_WITHDRAWS_BALANCE = 2003;
  public static final int INTENT_WITHDRAWS_PLEDGE = 2004;
  public static final String LOGIN_PHONE = "LOGIN_PHONE";

  public static final int CODE_RESET_TIME = 60;
  /********
   * bugly 应用升级
   ***********/
  public static final String BUGLY_APP_ID = "d69cfb5004";
  public static final String BUGLY_APP_KEY = "d3f06f3b-dee8-4011-a674-3584f3448cac";

  /*******
   * 微信 支付
   ********/
  public final static String URL_PAY_CALLBACK = "http://192.168.1.125/WechatPayServer";
  // 微信开放平台审核通过的应用APPID，如：wxd678efh567hg6787
  //    public static final String WX_APP_ID = "wx7ae5d54b23231e65";
  public static final String WX_APP_ID = "wx65cda5aab3553b8b";

  public static final String WX_APP_SECRET = "6d9406af447ef83ad805d8570ce9308c";
  // 微信支付分配的商户号,如：1230000109
  public static final String WX_MCH_ID = "1438983402";

  /*******支付宝支付********/
  /** 商户私钥，pkcs8格式 */
  /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
  /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
  /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
  /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
  /**
   * 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
   */
  // APPID
  public static final String ALPAY_APP_ID = "2017020805567270";//2017020605541977
  // 商户PID
  public static final String PARTNER = "2088521575010075";
  // 商户收款账号
  public static final String SELLER = "2088102169126433";
  // 商户私钥，pkcs8格式
  public static final String RSA2_PRIVATE = "";
  public static final String RSA_PRIVATE =
      "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC5Hi6DTAJQ5eUC+QeIyPDlZZztgMYKJ0LqVKu2kTNSb1V216r/KgQoBMPch0IuUZjEJeO7TNPpCqyzc2oR52mgWisD/v1tcz1WIHLr3AwOsDJEHHr+HRK70GAY0LoZMghrOOF5rfN3FHBPbeASOfM0w+ITBOG+2Mmbn/GVza2O/JLjPGqfFXUSbF6tGkeinRTSZcWu9BA8YBP/8no/bZNteh0f0y2yeWJqClzI4It3jiucgNy80Zl1uXwIgUx3e5nQLeWN3EpIctVKSTGA1ZSAkK9dqOs3zsVQbO2wHQ+Kul+i8E1boRenWZnFfp3F6CW6zzr5rG4MwrA8uDgRwAnLAgMBAAECggEAHH33vL+rL5ZizDVWIoiDA1vDbo6+bHukgfqsPzpUAuEd71+8cu+dmic5miy7U/O9JpuKVJ67kwfQkwI1ljHfAQ4/xqqtiPYOv73Y0lKZDH20DuWvTINfCcdFsywVKKGZyB8GHTtX2MEtlBweO8/tjfaESlcbA8vMJAbXIS56AyK72bcqt1gIjB+zDZI3P7lAqvueab6R544tMF60aWga4MeKF9VdXaOROfmHlR3VL1iufZ/Wp/FPIb+AgdpEdLQOLVbDCX+T0SpGDExyoDedxTdEAmc7+hp7SIswwXG5xBHwywQ5T84PnICe/r0DW1O799qeK+VSjisWtiH2gyY0MQKBgQDaOEjRXczGikAqHCQ2Do4NNPh2eCgRYbFZDpk1fJbXvsi0Yf3zrTD3cobtlenukbXDj/o2ORD5c5W0sUNEfdCTOFg8b3OsMSwv9Y0agS6JJffWeDhxt3u0/j6nRprQmaCeQqDUAEUSzC1igrS4lvXiONtW4SlLJnoBBdmMDjvc6QKBgQDZKslBVBikRlUwMbTMRPBjGdqbi6PUQ8aX2tcNr3Ybd2gRBYR9h3rZh7/mHA+NkHKdVmV/gytrN9n4L88KQphMhzZ5BMKULJeZnd1wpX/b8yaImtf8mgWfOwxcX9o67+PNe7G1Z0C5y6j+Jgin1U4AhFlOPnEODP8Je4dgYYCwkwKBgElIp6OwJYnzedLqenqw7qGezWYzufkEXuOylF8zBRFVH9/8HKXN3ujqzEdTlikpQ+p2GqhFB99OItN4yEdjHT2jqXY6U5VbOiStCQMCqRcj6hRbcVE3NlM1IuNYQbuWLCuZ4prvsEK80O0JShnHkLG1OGv3179GH6jiRwnS6eWpAoGBANXcL8NY6Mdlx+ClIpIC1eQSAtkpA9Y2RG0pAp0kjJ/x0BbrIxVnhNCRwaX/PkW/2U8eVOfkqa51mv6xPiexez/p6/fzexbmbwjFkNus76QP6IOgUVEd8KllQf/6GPPvZ5vk0xV8sB4H9M0LcXyuL5mMvFG0ZitbGxEgiCme+1dnAoGARoHt5ELMdi/RT57LjqNX+M7JTMeADgNswoMzrNlkxXzjsrYbW1UWaDygJ5kFiH5D9nbErfnznHOXeittibzL6PLH/VYaur509VYZDXN2ZURAwthxoWf/z7CoBiIEBc+JSdaK+n45/DTwaXAYvm49uNjZZRSnN2SxW9rdWqIFQhE=";
  // 支付宝公钥
  public static final String RSA_PUBLIC = "";

  /**
   * The RecyclerView is not currently scrolling.
   * 当前的recycleView不滑动(滑动已经停止时)
   */
  public static final int SCROLL_STATE_IDLE = 0;

  /**
   * The RecyclerView is currently being dragged by outside input such as user touch input.
   * 当前的recycleView被拖动滑动
   */
  public static final int SCROLL_STATE_DRAGGING = 1;

  //地图
  public static final int RC_LOCATION = 1010;
  public static final int RC_INITLOCATION = 1011;
  public static final int PERCALLPHONE = 1100;
  public static final int PERCAMERA = 1101;

  public static final int LOCATIONPRO = 10000;
  public static final int RELOCATIONPRO = 10001;

  public static final int ZXING_ALLOCATION = 276;
  public static final int ZXING_GETGOODS = 57;
  public static final int ZXING_BACKPAY = 582;

  public static final String PAYTYPE_CASH = "cash";     //现金支付
  public static final String PAYTYPE_POS = "pos";       //POS机支付
  public static final String PAYTYPE_ALIPAY = "alipay"; //支付宝支付
  public static final String PAYTYPE_WXPAY = "wepay";   //微信支付
  public static final String FROM = "from";
}
