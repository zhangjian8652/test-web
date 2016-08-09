package com.test.weixin.pay.util;

import com.test.weixin.pay.domain.WechatOrder;
import com.test.weixin.pay.exception.WechatOrderException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.*;

/**
 * Created by zhangjian on 2016/7/31.
 */
public class WechatPaymentUtil {

    public static String CHARACTER_ENCODING_UTF8 = "UTF-8";

    /**
     * 将WechatOrder转换成xml参数并且根据ASCII排序，加密生成SIGN
     * @param wechatOrder
     * @param characterEncoding
     * @return
     * @throws WechatOrderException
     */
    public static String generateSortedXMLFromWechatOrder(WechatOrder wechatOrder, String characterEncoding) throws WechatOrderException {
        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
        checkOrderRequiredParams(wechatOrder);
        addOrderParamsToSortedMap(wechatOrder, parameters);
        String mySign = createSign(characterEncoding, parameters, wechatOrder.getKey());
        parameters.put("sign", mySign);

        return CommonUtil.converterMapToXml(parameters);
    }

    /**
     * 将wechatOrder 中的参数放入排序map
     * @param wechatOrder
     * @param parameters
     */
    private static void addOrderParamsToSortedMap(WechatOrder wechatOrder, SortedMap<Object, Object> parameters) {
        //将必传参数添加至排序map
        parameters.put("appid", wechatOrder.getAppid());

        parameters.put("body", wechatOrder.getBody());

        parameters.put("device_info", wechatOrder.getDeviceInfo());
        parameters.put("mch_id", wechatOrder.getMchId());
        parameters.put("nonce_str", wechatOrder.getNonceStr());
        parameters.put("notify_url", wechatOrder.getNotifyUrl());

        parameters.put("out_trade_no", wechatOrder.getOutTradeNo());
        parameters.put("spbill_create_ip", wechatOrder.getSpbillCreateIp());
        parameters.put("total_fee", wechatOrder.getTotalFee());
        parameters.put("trade_type", wechatOrder.getTradeType());


        //不同条件下必传参数加入排序map
        if (WechatOrder.TRADE_TYPE_JSAPI.equals(wechatOrder.getTradeType()))
            parameters.put("openid", wechatOrder.getProductId());
        if (WechatOrder.TRADE_TYPE_NATIVE.equals(wechatOrder.getTradeType()))
            parameters.put("product_id", wechatOrder.getProductId());


        //可选参数非空加入排序map
        if (!isEmpty(wechatOrder.getAttach()))
            parameters.put("attach", wechatOrder.getAttach());

        if (!isEmpty(wechatOrder.getDetail()))
            parameters.put("detail", wechatOrder.getDetail());

        if (!isEmpty(wechatOrder.getFeeType()))
            parameters.put("fee_type", wechatOrder.getFeeType());

        if (!isEmpty(wechatOrder.getTimeStart()))
            parameters.put("time_start", wechatOrder.getTimeStart());

        if (!isEmpty(wechatOrder.getTimeExpire()))
            parameters.put("time_expire", wechatOrder.getTimeExpire());

        if (!isEmpty(wechatOrder.getGoodsTag()))
            parameters.put("goods_tag", wechatOrder.getGoodsTag());

        if (!isEmpty(wechatOrder.getLimitPay()))
            parameters.put("limit_pay", wechatOrder.getLimitPay());
    }

    /**
     * 检测统一下单必填参数
     * @param wechatOrder
     * @throws WechatOrderException
     */
    private static void checkOrderRequiredParams(WechatOrder wechatOrder) throws WechatOrderException {

        if (isEmpty(wechatOrder.getKey())) {
            throw new WechatOrderException("API key is empty.");
        }

        if (isEmpty(wechatOrder.getAppid())) {
            throw new WechatOrderException("appid is empty.");
        }

        if (isEmpty(wechatOrder.getDeviceInfo())) {
            throw new WechatOrderException("deviceInfo is empty.");
        }

        if (isEmpty(wechatOrder.getMchId())) {
            throw new WechatOrderException("mchId is empty.");
        }

        if (isEmpty(wechatOrder.getNonceStr())) {
            throw new WechatOrderException("nonceStr is empty.");
        }

        if (isEmpty(wechatOrder.getBody())) {
            throw new WechatOrderException("body is empty.");
        }

        if (isEmpty(wechatOrder.getNotifyUrl())) {
            throw new WechatOrderException("notifyUrl is empty.");
        }

        if (isEmpty(wechatOrder.getOutTradeNo())) {
            throw new WechatOrderException("outTradeNo is empty.");
        }

        if (isEmpty(wechatOrder.getSpbillCreateIp())) {
            throw new WechatOrderException("spbillCreateIp is empty.");
        }

        if (isEmpty(wechatOrder.getTotalFee())) {
            throw new WechatOrderException("totalFee is empty.");
        }

        if (isEmpty(wechatOrder.getTradeType())) {
            throw new WechatOrderException("tradeType is empty.");
        }

        if (WechatOrder.TRADE_TYPE_JSAPI.equals(wechatOrder.getTradeType()) && isEmpty(wechatOrder.getOpenid())) {
            throw new WechatOrderException("tradeType is " + WechatOrder.TRADE_TYPE_JSAPI + ", but openid is empty.");
        }

        if (WechatOrder.TRADE_TYPE_NATIVE.equals(wechatOrder.getTradeType()) && isEmpty(wechatOrder.getOpenid())) {
            throw new WechatOrderException("tradeType is " + WechatOrder.TRADE_TYPE_NATIVE + ", but productId is empty.");
        }
    }

    /**
     * 微信支付签名算法sign
     *
     * @param characterEncoding
     * @param parameters
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String createSign(String characterEncoding, SortedMap<Object, Object> parameters, String key) {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v)
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + key);
        System.out.println(sb.toString());
        String sign = MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        return sign;
    }

    public static String getParamsStr(Map<String, String> map) {

        String params = "";
        if (map == null || map.size() == 0) {
            return params;
        }

        Iterator<String> iterator = map.keySet().iterator();

        try {
            int i = 0;
            while (iterator.hasNext()) {
                String key = iterator.next();
                params += key + "=" + URLEncoder.encode(map.get(key), "UTF-8");
                if (i + 1 < map.size()) {
                    params += "&";
                }
                i++;
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return params;
    }

    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
            resultSb.append(byteToHexString(b[i]));

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static String MD5Encode(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname))
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes()));
            else
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes(charsetname)));
        } catch (Exception exception) {
        }
        return resultString;
    }

    private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    private static boolean isEmpty(String string) {

        return null == string || "".equals(string);

    }
}
