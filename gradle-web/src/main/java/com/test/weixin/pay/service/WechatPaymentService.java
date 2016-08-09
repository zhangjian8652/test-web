package com.test.weixin.pay.service;

import com.test.weixin.pay.domain.WechatPrePayOrder;
import com.test.weixin.pay.domain.WechatUserAuth;
import com.test.weixin.pay.exception.WechatServiceException;

/**
 * Created by zhangjian on 2016/8/9.
 */
public interface WechatPaymentService {

    /**
     * 获取code的url，
     * @param returnUrl 用户处理后会被微信重定向的返回链接 "http://zhangjian.iok.la/wechat/test/openid" 链接需要配置到微信公众号
     * @return 这里需要将客户端重定向到返回的url上
     */
    String generateGetCodeURL(String returnUrl)  throws WechatServiceException;

    /**
     * 用code获取用户的认证相关信息，
     * @param code
     * @return 这里的返回的WechatUserAuth 中的token先关暂时先可以保存。这个阶段只用到openid
     */
    WechatUserAuth getWechatUserAuth(String code) throws WechatServiceException;

    /**
     *
     * 生成预支付订单
     * @param mount 总金额，列入10.00元
     * @param tittle 订单的描述信息，比如商品购买或者商品充值
     * @param outTradeNo 订单号：20160808174447（32个字符内，可包含字母）
     * @param openid 获取的用户openid
     * @param notifyUrl 交易成功异步通知接口
     * @param ip 微信客户端ip
     * @return 返回创建预支付订单结果
     */
    WechatPrePayOrder generateOpenPrePayOrder(double mount, String tittle, String outTradeNo, String openid, String notifyUrl, String ip)  throws WechatServiceException;

}
