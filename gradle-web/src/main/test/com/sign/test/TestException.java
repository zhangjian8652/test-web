package com.sign.test;

/**
 * Created by zhangjian on 2016/8/8.
 */
public class TestException {
    public static void main(String[] args) throws Exception {

        boolean a =true;
        if (a)
        throw new Exception("aa");
        System.out.println("hello world!");
    }
}
