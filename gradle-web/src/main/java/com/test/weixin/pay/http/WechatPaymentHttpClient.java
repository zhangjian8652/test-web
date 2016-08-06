package com.test.weixin.pay.http;

import com.test.httpclient.httpclient.HttpRequest;
import org.apache.commons.httpclient.Header;

/**
 * Created by zhangjian on 2016/7/31.
 */
public class WechatPaymentHttpClient extends HttpRequest{

    private static String HTTP_TYPE = "https";
    private static int HTTP_443 = 443;
    private static String ADDRESS = "api.mch.weixin.qq.com";
    public static String CREATE_ORDER_URI = "/pay/unifiedorder";
    private static Header[] XML_REQUEST_HEADERS = {new Header(CONTENT_TYPE_KEY,CONTENT_TYPE_XML),new Header(ACCEPT_KEY,ACCEPT_XML)};
    public WechatPaymentHttpClient() {
        super(HTTP_TYPE, XML_REQUEST_HEADERS, ADDRESS, HTTP_443);
    }
}
