package com.test.weixin.pay.config;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by zhangjian on 2016/8/9.
 */
public class WechatPaymentConfig {
    public static String KEY;
    public static String APPID;
    public static String MCH_ID;
    public static String SECRET;
    public static String WECHAT_USER_AUTH_SESSION_KEY = "wechatUserAuth";

    static {
        Properties properties = new Properties();
        try {
            properties.load(WechatPaymentConfig.class.getClassLoader().getResourceAsStream("wechat_payment.properties"));
            KEY = properties.getProperty("key");
            APPID = properties.getProperty("appid");
            MCH_ID = properties.getProperty("mch_id");
            SECRET = properties.getProperty("secret");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
