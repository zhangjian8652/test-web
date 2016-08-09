package com.test.weixin.pay.service;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.test.httpclient.httpclient.domain.Response;
import com.test.weixin.pay.config.WechatPaymentConfig;
import com.test.weixin.pay.domain.WechatOrder;
import com.test.weixin.pay.domain.WechatPrePayOrder;
import com.test.weixin.pay.domain.WechatUserAuth;
import com.test.weixin.pay.exception.WechatOrderException;
import com.test.weixin.pay.exception.WechatServiceException;
import com.test.weixin.pay.http.WechatPaymentHttpClient;
import com.test.weixin.pay.util.CommonUtil;
import com.test.weixin.pay.util.WechatPaymentUtil;

import javax.xml.ws.WebServiceException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by zhangjian on 2016/8/9.
 */
public class WechatPaymentServiceImpl implements WechatPaymentService {

    @Override
    public String generateGetCodeURL(String returnUrl) throws WechatServiceException {

        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("appid", WechatPaymentConfig.APPID);// 	公众号的唯一标识
        params.put("redirect_uri", returnUrl);// 	授权后重定向的回调链接地址，请使用urlencode对链接进行处理
        params.put("response_type", "code");// 	返回类型，请填写code
        params.put("scope", "snsapi_userinfo");//应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息）
        params.put("state", "wx090eef3f75d06436");// 	重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" + WechatPaymentUtil.getParamsStr(params) + "#wechat_redirect";// 	无论直接打开还是做页面302重定向时候，必须带此参数
        System.out.println("用户登录授权重定向returnUrl = [" + url + "]");
        return url;
    }

    @Override
    public WechatUserAuth getWechatUserAuth(String code) throws WechatServiceException {

        if(CommonUtil.isEmpty(code)){
            throw new WechatServiceException("微信重定向返回的code参数为空请检查网络是否正常。");
        }

        WechatPaymentHttpClient client = new WechatPaymentHttpClient();

        StringBuilder reqURI = new StringBuilder("/sns/oauth2/access_token?");
        reqURI.append("appid=");
        reqURI.append(WechatPaymentConfig.APPID);
        reqURI.append("&secret=");
        reqURI.append(WechatPaymentConfig.SECRET);
        reqURI.append("&code=");
        reqURI.append(code);
        reqURI.append("&grant_type=authorization_code");

        Response response = client.get(reqURI.toString());

        if (!response.isSuccess()) {
            throw new WechatServiceException("调用微信接口异常，请检查网络是否正常。");
        }

        WechatUserAuth wechatUserAuth = null;

        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        try {
            wechatUserAuth = xmlMapper.readValue(response.getString(), WechatUserAuth.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new WebServiceException("转换微信认证openid相关数据失败，请检查数据格式");
        }

        return wechatUserAuth;
    }

    @Override
    public WechatPrePayOrder generateOpenPrePayOrder(double mount, String tittle, String outTradeNo, String openid, String notifyUrl, String ip) throws WechatServiceException {

        WechatOrder wechatOrder = new WechatOrder();
        wechatOrder.setAppid(WechatPaymentConfig.APPID);
        wechatOrder.setBody(tittle);
        wechatOrder.setDeviceInfo(WechatOrder.DEVICE_INFO_WEB);
        wechatOrder.setMchId(WechatPaymentConfig.MCH_ID);
        wechatOrder.setNonceStr(UUID.randomUUID().toString().replaceAll("-", ""));
        wechatOrder.setNotifyUrl(notifyUrl);
        wechatOrder.setOpenid(openid);
        wechatOrder.setOutTradeNo(outTradeNo);
        wechatOrder.setSpbillCreateIp(ip);
        wechatOrder.setTotalFee(mount * 100 + "");
        wechatOrder.setTradeType(WechatOrder.TRADE_TYPE_JSAPI);


        String xmlParams = null;
        try {
            xmlParams = WechatPaymentUtil.generateSortedXMLFromWechatOrder(wechatOrder, WechatPaymentUtil.CHARACTER_ENCODING_UTF8);
        } catch (WechatOrderException e) {
            System.out.println("WechatPaymentServiceImpl.generateOpenPrePayOrder:" + xmlParams);
            e.printStackTrace();
            throw new WechatServiceException("创建生成预支付订单失败");
        }

        WechatPaymentHttpClient client = new WechatPaymentHttpClient();
        Response response = client.post(WechatPaymentHttpClient.CREATE_ORDER_URI, xmlParams);

        if (!response.isSuccess()) {
            System.out.println("WechatPaymentServiceImpl.generateOpenPrePayOrder :" + response.getString());
            throw new WechatServiceException("创建生成预支付订单失败");
        }

        WechatPrePayOrder wechatPrePayOrder = null;
        try {
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
            wechatPrePayOrder = xmlMapper.readValue(response.getString(), WechatPrePayOrder.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new WebServiceException("微信返回数据转换失败，数据格式问题");
        }


        return wechatPrePayOrder;
    }
}
