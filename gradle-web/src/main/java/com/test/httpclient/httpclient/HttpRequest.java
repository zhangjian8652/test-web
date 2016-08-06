package com.test.httpclient.httpclient;

import com.test.httpclient.httpclient.domain.Response;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhangjian on 2016/4/12.
 * 用于发送rest api 请求工具类
 */
public class HttpRequest {

    private Logger logger = Logger.getLogger(HttpRequest.class);
    //请求类型 目前有http 或者 https两种 默认为http
    private String type;
    //请求头信息
    private Header[] headers;
    //请求域名或者ip
    private String address;
    //请求端口
    private Integer port;

    private HttpClient httpClient;

    private Response response;

    private String charSet = "UTF-8";

    private String defaultCharSet = "UTF-8";

    public static String TYPE_HTTP = "http";

    public static String TYPE_HTTPS = "https";

    protected static String CONTENT_TYPE_JSON = "application/json";
    protected static String CONTENT_TYPE_XML = "application/xml";
    protected static String CONTENT_TYPE_KEY = "content-type";

    protected static String ACCEPT_XML = "application/xml";
    protected static String ACCEPT_JSON = "application/json";
    protected static String ACCEPT_KEY = "accept";


    /**
     * @param type    目前支持[HTTPS,HTTP]，选择一种，可以使用 HttpRequest.TYPE_HTTP 或者 HttpRequest.TYPE_HTTPS
     * @param headers 请求头信息
     * @param address 请求域名或者IP
     * @param port    请求端口
     */
    public HttpRequest(String type, Header[] headers, String address, Integer port) {
        init(type, headers, address, port);
    }

    public HttpRequest(String type, Map<String, String> mapHeaders, String address, Integer port) {
        Header[] headers = transferMapHeaders(mapHeaders);
        init(type, headers, address, port);
    }

    private void init(String type, Header[] headers, String address, Integer port) {

        this.type = type == null ? TYPE_HTTP : type;
        this.headers = headers;
        this.address = address;
        this.port = port;
        httpClient = new HttpClient();

        if (type != null && TYPE_HTTPS.equalsIgnoreCase(type)) {
            Protocol protocol = new Protocol("https", new LocalSSLSocketFactory(), port);
            Protocol.registerProtocol("https", protocol);
            httpClient.getHostConfiguration().setHost(address, port, protocol);
        } else {
            httpClient.getHostConfiguration().setHost(address, port);
        }

    }


    protected HttpRequest() {
    }

    /**
     * 将map对象头信息转为Header数组
     *
     * @param mapHeaders
     * @return
     */
    private Header[] transferMapHeaders(Map<String, String> mapHeaders) {
        if (mapHeaders == null || mapHeaders.size() <= 0) {
            return null;
        }

        Header[] headers = new Header[mapHeaders.size()];
        Set<String> keys = mapHeaders.keySet();

        int index = 0;
        for (String key : keys) {
            Header header = new Header(key, mapHeaders.get(key));
            headers[index++] = header;
        }
        return headers;
    }

    /**
     * 将map对象的参数转为NameValuePair数组
     *
     * @param mapParams
     * @return
     */
    private NameValuePair[] transferMapParams(Map<String, String> mapParams) {
        if (mapParams == null || mapParams.size() <= 0) {
            return null;
        }

        NameValuePair[] nameValuePairs = new Header[mapParams.size()];
        Set<String> keys = mapParams.keySet();

        int index = 0;
        for (String key : keys) {
            NameValuePair nameValuePair = new Header(key, mapParams.get(key));
            nameValuePairs[index++] = nameValuePair;
        }
        return nameValuePairs;
    }

    /**
     * 发起get请求
     */
    public Response get(String uri) {
        logger.info("发起 get 请求 开始");
        GetMethod getMethod = new GetMethod(uri);
        execute(getMethod);
        logger.info("发起 get 请求 结束");
        return response;
    }


    /**
     * 发起post请求
     */
    public Response post(String uri, NameValuePair[] nameValuePairs) {

        logger.info("发起 POST 请求 开始");
        PostMethod postMethod = new PostMethod(uri);
        postMethod.addParameters(nameValuePairs);
        execute(postMethod);
        logger.info("发起 POST 请求 结束");

        return response;
    }

    /**
     * 发起post请求
     */
    public Response post(String uri, String params) {

        logger.info("发起 POST 请求 开始");
        PostMethod postMethod = new PostMethod(uri);
        StringRequestEntity sre = getJsonStringRequestEntity(params);
        postMethod.setRequestEntity(sre);
/*        postMethod.addRequestHeader(CONTENT_TYPE_KEY, CONTENT_TYPE_JSON);
        logger.info("header [" + CONTENT_TYPE_KEY + ":" + CONTENT_TYPE_JSON + "]");
        logger.info("header [" + ACCEPT_KEY + ":" + ACCEPT_JSON + "]");
        postMethod.addRequestHeader(ACCEPT_KEY, ACCEPT_JSON);*/
        logger.info("json params :" + sre.getContent());
        execute(postMethod);
        logger.info("发起 POST 请求 结束");

        return response;
    }

    /**
     * 添加头信息
     *
     * @param httpMethod
     * @return 返回int类型，根据请求头判断请求数据类型
     */
    private void addRequestHeaders(HttpMethod httpMethod) {

        if(this.headers == null || this.headers.length <= 0){
            return;
        }

        for (int i = 0; i < headers.length; i++) {
            Header header = headers[i];
            httpMethod.addRequestHeader(header);
            if (CONTENT_TYPE_KEY.equalsIgnoreCase(header.getName()) && CONTENT_TYPE_JSON.equalsIgnoreCase(header.getValue())) {
            }
            logger.info("header [" + header.getName() + ":" + header.getValue() + "]");
        }
    }

    /**
     * 发送PUT请求
     */
    public Response put(String uri, NameValuePair[] params) {

        logger.info("发起 PUT 请求 开始");
        PutMethod putMethod = new PutMethod(uri);
        if (params != null) {
            putMethod.setQueryString(params);
        }
        execute(putMethod);
        logger.info("发起 PUT 请求 结束");
        return response;
    }

    /**
     * 发送PUT请求
     */
    public Response put(String uri, String jsonParams) {

        logger.info("发起 PUT 请求 开始");
        PutMethod putMethod = new PutMethod(uri);
        StringRequestEntity sre = getJsonStringRequestEntity(jsonParams);
        putMethod.setRequestEntity(sre);
        execute(putMethod);
        logger.info("发起 PUT 请求 结束");
        return response;
    }

    /**
     * 发送delete请求
     */
    public Response delete(String uri) {
        logger.info("发起 DELETE 请求 开始");
        DeleteMethod deleteMethod = new DeleteMethod(uri);
        execute(deleteMethod);
        logger.info("发起 DELETE 请求 结束");
        return response;
    }

    private void execute(HttpMethod httpMethod) {

        try {
            addRequestHeaders(httpMethod);

            logger.info("request url");
            logger.info(httpClient.getHostConfiguration().getHostURL() + httpMethod.getURI());

            this.httpClient.executeMethod(httpMethod);
            response = new Response(httpMethod.getStatusCode(), httpMethod.getResponseBodyAsString(), httpMethod.getResponseBodyAsStream(), httpMethod.getResponseBody(), httpMethod.getResponseHeaders());
            String resultString = httpMethod.getResponseBodyAsString();
            Integer resultStatus = httpMethod.getStatusCode();

            //给response赋值
            response.setString(resultString);
            response.setStatus(resultStatus);
            response.setBytes(httpMethod.getResponseBody());
            response.setIs(httpMethod.getResponseBodyAsStream());
            response.setHeaders(httpMethod.getResponseHeaders());


            logger.info("http status code");
            logger.info(resultStatus);
            logger.info("http request");
            logger.info(resultString);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private StringRequestEntity getJsonStringRequestEntity(String jsonParams) {
        try {
            return new StringRequestEntity(jsonParams, CONTENT_TYPE_JSON, charSet == null ? defaultCharSet : charSet);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCharSet() {
        return charSet;
    }

    public void setCharSet(String charSet) {
        this.charSet = charSet;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }


    public Response getResponse() {
        return response;
    }

}
