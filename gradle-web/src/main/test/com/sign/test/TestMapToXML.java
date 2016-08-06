package com.sign.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.weixin.pay.util.CommonUtil;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by zhangjian on 2016/7/29.
 */
public class TestMapToXML {
    public static void main(String[] args) throws JsonProcessingException {
        SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
        parameters.put("appid", "a");
        parameters.put("body", "c");
        parameters.put("device_info", "adfs");
        parameters.put("mch_id", "你好");
        parameters.put("nonce_str","aa");

        SortedMap<Object,Object> jsonParams = new TreeMap<Object,Object>();
        jsonParams.put("j1","adsd");
        jsonParams.put("j2","adsd");
        jsonParams.put("j13","adsd");
        jsonParams.put("jswde","adsd");

        ObjectMapper mapper = new ObjectMapper();
        String params = mapper.writeValueAsString(jsonParams);

        parameters.put("json","<!CDATA[" +params + "]>");
        System.out.println(CommonUtil.converterMapToXml(parameters));
    }
}
