package com.test.httpclient.httpclient.domain;

import org.apache.commons.httpclient.Header;

import java.io.InputStream;
import java.util.List;

/**
 * Created by zhangjian on 2016/3/11.
 */
public class Response<T> {
    private Integer status;
    private String string;
    private InputStream is;
    private byte[] bytes;
    private Header[] headers;

    private List<T> datas;
    private T t;

    public Response() {

    }


    public Response(Integer status, String string, InputStream is, byte[] bytes, Header[] headers) {
        this.status = status;
        this.string = string;
        this.is = is;
        this.bytes = bytes;
        this.headers = headers;
    }

    public boolean isSuccess(){
        return this.status == 200 || this.status == 201 || this.status == 202;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public InputStream getIs() {
        return is;
    }

    public void setIs(InputStream is) {
        this.is = is;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public Header[] getHeaders() {
        return headers;
    }

    public void setHeaders(Header[] headers) {
        this.headers = headers;
    }
}
