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
        params.put("appid","wx9ba36613cc2989c5");// 	公众号的唯一标识
        params.put("redirect_uri","http://zhangjian.iok.la/wechat/test/openid");// 	授权后重定向的回调链接地址，请使用urlencode对链接进行处理
        params.put("response_type","code");// 	返回类型，请填写code
        params.put("scope","snsapi_userinfo");//应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息）
        params.put("state","wx090eef3f75d06436");// 	重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节

        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" + WechatPaymentUtil.getParamsStr(params) + "#wechat_redirect";// 	无论直接打开还是做页面302重定向时候，必须带此参数
        resp.sendRedirect(url);
    }

}
