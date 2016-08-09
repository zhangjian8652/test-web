package com.test.weixin.pay.servlet;

import com.test.weixin.pay.config.WechatPaymentConfig;
import com.test.weixin.pay.domain.WechatUserAuth;
import com.test.weixin.pay.exception.WechatServiceException;
import com.test.weixin.pay.service.WechatPaymentService;
import com.test.weixin.pay.service.WechatPaymentServiceImpl;
import com.test.weixin.pay.util.CommonUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhangjian on 2016/7/25.
 */
public class WechatPayOrder extends HttpServlet {

    private WechatPaymentService wechatPaymentService = new WechatPaymentServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String notifyUrl = req.getRequestURL() + "wechat/payment/notify";
        String clietnIp = CommonUtil.getIpAddr(req);
        String outTradeNO = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        WechatUserAuth wechatUserAuth = (WechatUserAuth) req.getSession().getAttribute(WechatPaymentConfig.WECHAT_USER_AUTH_SESSION_KEY);

        try {
            wechatPaymentService.generateOpenPrePayOrder(0.01,"测试",outTradeNO,wechatUserAuth.getOpenid(),notifyUrl,clietnIp);
        } catch (WechatServiceException e) {
            e.printStackTrace();
        }
    }

}
