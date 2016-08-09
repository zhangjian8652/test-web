package com.test.weixin.pay.filter;

import com.test.weixin.pay.config.WechatPaymentConfig;
import com.test.weixin.pay.domain.WechatUserAuth;
import com.test.weixin.pay.exception.WechatServiceException;
import com.test.weixin.pay.service.WechatPaymentService;
import com.test.weixin.pay.service.WechatPaymentServiceImpl;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by zhangjian on 2016/8/9.
 */
public class WechatPaymentFilter implements Filter {

    private String openAccountAuthURI = "/wechat/payment/open/auth";
    private String openAccountAuthResult = "/wechat/payment/open/auth/result";
    private String afterAuthURI;

    private static final WechatPaymentService wechatPaymentService = new WechatPaymentServiceImpl();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.afterAuthURI = filterConfig.getInitParameter("afterAuthURI");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String requestURI = req.getRequestURI();

        if (requestURI.equals(this.openAccountAuthURI)) {

            String redirectUrl = null;
            try {
                redirectUrl = wechatPaymentService.generateGetCodeURL(this.openAccountAuthResult);
            } catch (WechatServiceException e) {
                e.printStackTrace();
            }

            resp.sendRedirect(redirectUrl);
        }


        if (requestURI.equals(this.openAccountAuthResult)) {
            String code = request.getParameter("code");
            String state = request.getParameter("state");

            WechatUserAuth wechatUserAuth = null;
            try {
                wechatUserAuth = wechatPaymentService.getWechatUserAuth(code);
            } catch (WechatServiceException e) {
                e.printStackTrace();
            }

            req.getSession().setAttribute(WechatPaymentConfig.WECHAT_USER_AUTH_SESSION_KEY,wechatUserAuth);

            resp.sendRedirect(afterAuthURI);

        }

    }

    @Override
    public void destroy() {

    }


}
