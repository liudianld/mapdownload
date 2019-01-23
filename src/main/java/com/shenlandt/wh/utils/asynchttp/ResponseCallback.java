package com.shenlandt.wh.utils.asynchttp;

import java.io.IOException;
import java.io.InputStream;
import java.net.CookieStore;
import java.net.URL;

import com.shenlandt.wh.utils.asynchttp.support.ParamsWrapper;

/**
 * Http请求响应回调接口。每个Http请求被发出后，只有两种结果：1、服务端响应请求；2、请求异常。
 */
public interface ResponseCallback {

	/**
	 * 在Http请求被提交时回调
	 * @param url 被提交的URL
	 * @param params 提交的参数
	 */
	void onSubmit(URL url, ParamsWrapper params, Object... args);
	
    /**
     * 返回服务端的响应流。响应流的关闭，由Callback的实现类关闭。
     * @param response 响应流
     */
    void onResponse(CookieStore cookieStore,InputStream response,URL url,Object... args);
	
	/**
	 * 从客户端连接服务端，服务端返回响应的过程中发生的IO异常，在此方法中回调。异常会在第一现场被打印输出。
	 * @param exp 异常对象。
	 * @param url 异常URL地址
	 */
	void onConnectError (IOException exp, URL url, Object... args);

	/**
	 * 在读取服务端返回的响应流数据的过程，发生IO异常，在此方法中回调。异常会在第一现场被打印输出。
	 * @param exp 异常对象。
	 * @param url 异常URL地址
	 */
	void onStreamError (IOException exp, URL url, Object... args);

    /**
     * 其它异常
     * @param exp 异常对象。
     * @param url 异常URL地址
     */
    void onUncatchedError(Throwable exp, URL url, Object... args);

}
