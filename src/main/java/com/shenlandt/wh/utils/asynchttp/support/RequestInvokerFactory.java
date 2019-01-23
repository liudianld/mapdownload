package com.shenlandt.wh.utils.asynchttp.support;

import com.shenlandt.wh.utils.asynchttp.ResponseCallback;
import com.shenlandt.wh.utils.asynchttp.support.RequestInvoker.HttpMethod;

/**
 * Invoker实现类创建工厂
 */
public class RequestInvokerFactory {
	
	private static Class<? extends RequestInvoker> InvokerType = SimpleHttpInvoker.class;
	
	public static RequestInvoker obtain(HttpMethod method,String url,ParamsWrapper params,ResponseCallback callback, Object... args){
		RequestInvoker invoker;
		if(!InvokerType.equals(SimpleHttpInvoker.class)){
			try {
				invoker = InvokerType.newInstance();
			} catch (Exception exp) {
				System.err.println(String.format("Cannot instance from %s, used default SimpleHttpInvoker.",InvokerType.getName()));
				invoker = new SimpleHttpInvoker();
			}
		}else{
			invoker = new SimpleHttpInvoker();
		}
		invoker.init(method, url, params, callback, args);
		return invoker;
	}
	
	/**
	 * 向工厂注册一个Invoker实现类
	 * @param clazz 实现类的类对象
	 */
	public static void register(Class<? extends RequestInvoker> clazz){
		InvokerType = clazz;
	}
	
}
