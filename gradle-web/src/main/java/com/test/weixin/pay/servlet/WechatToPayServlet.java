package com.test.weixin.pay.servlet;

import com.test.weixin.pay.util.WechatPaymentUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

/**
 * Created by zhangjian on 2016/7/21.
 */
public class WechatToPayServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String key = "9tBWQxPYIfZEHOSKAZuGY2UBQyWlTq8l";//API KEY认证

        SortedMap<Object,Object> params = new TreeMap<Object, Object>();

        params.put("appId","wx9ba36613cc2989c5");
        params.put("timeStamp",System.currentTimeMillis());
        params.put("nonceStr",UUID.randomUUID().toString().replace("-",""));
        params.put("package","prepay_id=wx20160805153913a49b094ce70941518785");
        params.put("signType","MD5");

        String sgn = WechatPaymentUtil.createSign("UTF-8",params,key);
        req.setAttribute("appId","wx9ba36613cc2989c5");
        req.setAttribute("timeStamp",System.currentTimeMillis());
        req.setAttribute("nonceStr", UUID.randomUUID().toString().replace("-",""));
        req.setAttribute("package","prepay_id=wx20160805153913a49b094ce70941518785");
        req.setAttribute("signType","MD5");
        req.setAttribute("paySign", sgn);

        System.out.println(sgn);

        req.getRequestDispatcher("/index.jsp").forward(req,resp);


    }

}
