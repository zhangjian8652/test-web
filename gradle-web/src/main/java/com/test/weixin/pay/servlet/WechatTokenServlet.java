package com.test.weixin.pay.servlet;

import com.test.weixin.pay.util.WechatPaymentUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by zhangjian on 2016/7/21.
 */
public class WechatTokenServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("request come in!");

        Map<String,String> params = new LinkedHashMap<String, String>();
        params.put("appid","wx9ba36613cc2989c5");
        params.put("redirect_uri","http://zhifu.woqu.sc.cn/wechat/test/openid/");
        params.put("response_type","code");
        params.put("scope","snsapi_userinfo");
        params.put("state","wx090eef3f75d06436");

        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" + WechatPaymentUtil.getParamsStr(params) + "#wechat_redirect";
        resp.sendRedirect(url);
    }

}
