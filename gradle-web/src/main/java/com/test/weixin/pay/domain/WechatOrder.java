package com.test.weixin.pay.domain;

/**
 * Created by zhangjian on 2016/7/31.
 */
public class WechatOrder {


    public static String TRADE_TYPE_JSAPI = "JSAPI";
    public static String TRADE_TYPE_NATIVE = "NATIVE";

    private String key;////API KEY认证
    private String appid;// 微信支付分配的公众号ID
    private String deviceInfo;// 终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
    private String mchId;// 微信支付分配的商户号
    private String nonceStr;// 随机字符串
    private String body;// 商品简单描述，该字段须严格按照规范传递，具体请见参数规定 腾讯充值中心-QQ会员充值
    private String notifyUrl;// 接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
    private String outTradeNo;// 商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号"20160808174447"
    private String spbillCreateIp;// APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
    private String totalFee;// 订单总金额，单位为分，详见支付金额
    private String tradeType;// 取值如下：JSAPI，NATIVE，APP，详细说明见参数规定,JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付，统一下单接口trade_type的传参可参考这里 MICROPAY--刷卡支付，刷卡支付有单独的支付接口，不调用统一下单接口

    /**
     * 不同类型支付必传
     */
    private String openid;// trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。openid如何获取，可参考【获取openid】。企业号请使用【企业号OAuth2.0接口】获取企业号内成员userid，再调用【企业号userid转openid接口】进行转换
    private String productId;//trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。


    /**
     * 非必传参数
     */
    private String attach;// 附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
    private String detail;// 商品详细列表，使用Json格式，传输签名前请务必使用CDATA标签将JSON文本串保护起来。
    private String feeType;// 符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
    private String timeStart;// 订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
    private String timeExpire;// 订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。其他详见时间规则 注意：最短失效时间间隔必须大于5分钟
    private String goodsTag;// 商品标记，代金券或立减优惠功能的参数，说明详见代金券或立减优惠
    private String limitPay;// no_credit--指定不能使用信用卡支付


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getSpbillCreateIp() {
        return spbillCreateIp;
    }

    public void setSpbillCreateIp(String spbillCreateIp) {
        this.spbillCreateIp = spbillCreateIp;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeExpire() {
        return timeExpire;
    }

    public void setTimeExpire(String timeExpire) {
        this.timeExpire = timeExpire;
    }

    public String getGoodsTag() {
        return goodsTag;
    }

    public void setGoodsTag(String goodsTag) {
        this.goodsTag = goodsTag;
    }

    public String getLimitPay() {
        return limitPay;
    }

    public void setLimitPay(String limitPay) {
        this.limitPay = limitPay;
    }


}
