package com.test.weixin.pay.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.httpclient.httpclient.HttpRequest;
import com.test.httpclient.httpclient.domain.Response;
import com.test.weixin.pay.http.WechatPaymentHttpClient;
import org.apache.commons.httpclient.Header;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by zhangjian on 2016/7/25.
 */
public class WechatOpenIdServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String code = req.getParameter("code");
        String state = req.getParameter("state");

        System.out.println("code:" + code);
        System.out.println("state:" + state);
        Header[] headers = {};

        //获取认证信息
        /**
         *获取token
         * https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
         * 正确：
         {
         "access_token":"ACCESS_TOKEN",
         "expires_in":7200,
         "refresh_token":"REFRESH_TOKEN",
         "openid":"OPENID",
         "scope":"SCOPE",
         "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
         }
         * 错误：{"errcode":40029,"errmsg":"invalid code"}
         *
         */
        HttpRequest httpRequest = new HttpRequest("https", headers, "api.weixin.qq.com", 443);
        Response response = httpRequest.get("/sns/oauth2/access_token?appid=wx9ba36613cc2989c5&secret=8e0402012dc917a72d7805efbc07dfc5&code=" + code + "&grant_type=authorization_code ");

        String tokenData = null;
        if (response.isSuccess()) {
            tokenData = response.getString();
        }

        /**
         *  刷新token
         *  https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN
         *  正确：
         {
         "access_token":"ACCESS_TOKEN",
         "expires_in":7200,
         "refresh_token":"REFRESH_TOKEN",
         "openid":"OPENID",
         "scope":"SCOPE"
         }
         * 错误：
         * {"errcode":40029,"errmsg":"invalid code"}
         */
       /* HttpRequest httpRequest1 = new HttpRequest("https",headers,"api.weixin.qq.com",443);
        httpRequest1.get("/sns/oauth2/refresh_token?appid=wx9ba36613cc2989c5&grant_type=refresh_token&refresh_token=REFRESH_TOKEN");*/

        /**
         * 拉取用户信息
         * http：GET（请使用https协议）
         * https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN
         * 正确：
         {
         "openid":" OPENID",
         " nickname": NICKNAME,
         "sex":"1",
         "province":"PROVINCE"
         "city":"CITY",
         "country":"COUNTRY",
         "headimgurl":    "http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/46",
         "privilege":[
         "PRIVILEGE1"
         "PRIVILEGE2"
         ],
         "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
         }
         * 错误：
         * {"errcode":40003,"errmsg":" invalid openid "}
         */
        //httpRequest.get("/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN");
        resp.getWriter().write(response.getString());


        /**
         * 检验授权凭证（access_token）是否有效
         * http：GET（请使用https协议）
         * https://api.weixin.qq.com/sns/auth?access_token=ACCESS_TOKEN&openid=OPENID
         *
         * 正确：{ "errcode":0,"errmsg":"ok"}
         * 错误：{ "errcode":40003,"errmsg":"invalid openid"}
         *
         */


        /**
         * 调用微信接口下单
         * https://api.mch.weixin.qq.com/pay/unifiedorder
         */

        if(tokenData == null){
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> token = mapper.readValue(tokenData,Map.class);

        WechatPaymentHttpClient client = new WechatPaymentHttpClient();





    }

}
