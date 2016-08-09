package com.sign.test;

import com.test.httpclient.httpclient.domain.Response;
import com.test.weixin.pay.http.WechatPaymentHttpClient;
import com.test.weixin.pay.util.CommonUtil;
import com.test.weixin.pay.util.WechatPaymentUtil;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by zhangjian on 2016/7/29.
 */
public class TestSign {
    /**
     * @param args
     */
    public static void main(String[] args) throws UnsupportedEncodingException {
        //微信api提供的参数
        String key = "9tBWQxPYIfZEHOSKAZuGY2UBQyWlTq8l";//API KEY认证
        String appid = "wx9ba36613cc2989c5";//微信支付分配的公众号ID
        String mch_id = "1371352602";//微信支付分配的商户号
        // mch_id = "1374006502";//微信支付分配的商户号
        String device_info = "WEB"; //终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
        String nonce_str = "1add1a30ac87aa2db72f57a2375d8fec";

        /**
         * goods_detail []：
         └ goods_id String 必填 32 商品的编号
         └ wxpay_goods_id String 可选 32 微信支付定义的统一商品编号
         └ goods_name String 必填 256 商品名称
         └ goods_num Int 必填 商品数量
         └ price Int 必填 商品单价，单位为分
         └ goods_category String 可选 32 商品类目ID
         └ body String 可选 1000 商品描述信息
         */
        //非必传
        String detail = "{\"goods_detail\":[{\"goods_id\":\"iphone6s_16G\",\"goods_name\":\"iPhone6s 16G\",\"goods_category\":\"123456\"},{\"goods_id\":\"iphone6s_32G\",\"goods_name\":\"iPhone6s 32G\",\"price\":608800,\"goods_category\":\"123789\",\"body\":\"苹果手机\"}]}";//商品详细列表，使用Json格式，传输签名前请务必使用CDATA标签将JSON文本串保护起来。
        String attach = "aaaaa";// 	附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
        String fee_type = "CNY";// 符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
        String time_start = "20091225091010";// 订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
        String time_expire = "20091227091010";// 订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。其他详见时间规则 注意：最短失效时间间隔必须大于5分钟
        String goods_tag = "WXG";// 商品标记，代金券或立减优惠功能的参数，说明详见代金券或立减优惠
        String product_id = "12235413214070356458058";// trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。
        String limit_pay = "no_credit";// no_credit--指定不能使用信用卡支付

        String openid = "os42NwH4kriaJOBeK9PaoUgYngkE";// trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。openid如何获取，可参考【获取openid】。企业号请使用【企业号OAuth2.0接口】获取企业号内成员userid，再调用【企业号userid转openid接口】进行转换

        //必传
        String body = "aaa-aaa";

        String ouno = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String out_trade_no = ouno;// 	商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号"20160808174447"
        int total_fee = 1;// 订单总金额，单位为分，详见支付金额
        String spbill_create_ip = "125.71.90.135";// APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
        String notify_url = "http://zhangjian.iok.la/wechat/test/pay";// 接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
        String trade_type = "JSAPI";// 取值如下：JSAPI，NATIVE，APP，详细说明见参数规定,JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付，统一下单接口trade_type的传参可参考这里 MICROPAY--刷卡支付，刷卡支付有单独的支付接口，不调用统一下单接口


        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
        parameters.put("appid", appid);
        parameters.put("attach", attach);
        parameters.put("body", body);
        parameters.put("detail", detail);
        parameters.put("device_info", device_info);
        parameters.put("mch_id", mch_id);
        parameters.put("nonce_str", nonce_str);
        parameters.put("notify_url", notify_url);
        parameters.put("openid", openid);
        parameters.put("out_trade_no", out_trade_no);
        parameters.put("spbill_create_ip", spbill_create_ip);
        parameters.put("total_fee", total_fee);
        parameters.put("trade_type", trade_type);





        String characterEncoding = "UTF-8";

        String mySign = WechatPaymentUtil.createSign(characterEncoding, parameters, key);

        parameters.put("sign", mySign);


        String xmlParams = CommonUtil.converterMapToXml(parameters);

        WechatPaymentHttpClient client = new WechatPaymentHttpClient();

        System.out.println(xmlParams);
        Response response = client.post(WechatPaymentHttpClient.CREATE_ORDER_URI, xmlParams);

        System.out.println("http-status :" + response.getStatus());

        System.out.println("http-response : " + new String(response.getBytes(),"UTF-8"));

    }



}
