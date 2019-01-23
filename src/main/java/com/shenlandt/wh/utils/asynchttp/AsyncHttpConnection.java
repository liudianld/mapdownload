package com.shenlandt.wh.utils.asynchttp;

import java.net.CookieStore;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.shenlandt.wh.utils.asynchttp.support.ParamsWrapper;
import com.shenlandt.wh.utils.asynchttp.support.RequestInvoker;
import com.shenlandt.wh.utils.asynchttp.support.RequestInvoker.HttpMethod;
import com.shenlandt.wh.utils.asynchttp.support.RequestInvokerFactory;
import com.shenlandt.wh.utils.asynchttp.support.RequestInvokerFilter;

/**
 * An asynchronous multithread http connection framework.
 */
public class AsyncHttpConnection {

	public static final String VERSION = "1.2.0";
	static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors()*2;
	static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
	
	private AsyncHttpConnection(){}
	private static class SingletonProvider {
		private static AsyncHttpConnection instance = new AsyncHttpConnection();
	}
	public static AsyncHttpConnection getInstance(){
		return SingletonProvider.instance;
	}

	private RequestInvokerFilter requestInvokerFilter;

    private CookieStore cookieStore;

    public void setCookieStore(CookieStore cookieStore) {
        this.cookieStore = cookieStore;
    }

    public void setRequestInvokerFilter (RequestInvokerFilter requestInvokerFilter) {
		this.requestInvokerFilter = requestInvokerFilter;
	}

	/**
	 * 发送一个无参数的GET请求到指定URL
	 * @param url 目标URL
	 * @param callback 响应回调
	 */
	public void get(String url,ResponseCallback callback, Object... args){
		get(url, null, callback, args);
	}
	
	/**
	 * 发送一个带参数的GET请求到指定URL
	 * @param url 目标URL
	 * @param params 请求参数
	 * @param callback 响应回调
	 *
	 */
	public void get(String url,ParamsWrapper params,ResponseCallback callback, Object... args){
		verifyUrl(url);
		sendRequest(url, params, HttpMethod.GET, callback, args);
	}
	
	/**
	 * 发送一个无参数的POST请求到指定URL
	 * @param url 目标URL
	 * @param callback 响应回调
	 */
	public void post(String url,ResponseCallback callback, Object... args){
		post(url, null, callback, args);
	}
	
	/**
	 * 发送一个带参数的POST请求到指定URL
	 * @param url 目标URL
	 * @param params 请求参数
	 * @param callback 响应回调
	 *
	 */
	public void post(String url,ParamsWrapper params,ResponseCallback callback, Object... args){
		verifyUrl(url);
		sendRequest(url, params, HttpMethod.POST, callback, args);
	}
	
	private void verifyUrl(String url){
		if(url == null) throw new IllegalArgumentException("Connection url cannot be null");
	}
	
	/**
	 * 发送一个带参数的HTTP请求到指定URL，指定它的HTTP请求方法。
	 * @param url 目标URL
	 * @param params 参数
	 * @param method HTTP方法
	 * @param callback 响应回调
	 */
	public void sendRequest(String url,ParamsWrapper params,HttpMethod method, ResponseCallback callback, Object... args ){
		if(url == null) return;
		RequestInvoker invoker = RequestInvokerFactory.obtain(method, url, params, callback, args);
        invoker.setCustomCookieStore(cookieStore);
		if( requestInvokerFilter != null ) requestInvokerFilter.onRequestInvoke(invoker);
		THREAD_POOL.submit(invoker);
	}

}
