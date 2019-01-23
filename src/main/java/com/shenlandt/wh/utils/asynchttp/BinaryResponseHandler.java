package com.shenlandt.wh.utils.asynchttp;

import java.io.IOException;
import java.io.InputStream;
import java.net.CookieStore;
import java.net.URL;

import com.shenlandt.wh.utils.asynchttp.utility.StreamUtility;

/**
 * desc   : 字节型数据处理类
 */
public abstract class BinaryResponseHandler implements ResponseCallback {

    protected CookieStore cookieStore;

	@Override
	final public void onResponse(CookieStore cookieStore,InputStream response,URL url, Object... args) {
        this.cookieStore = cookieStore;
		byte[] data = null;
		try {
			data = StreamUtility.convertToByteArray(response);
		} catch (IOException exp) {
			exp.printStackTrace();
			onStreamError(exp,url,args);
        }catch (Throwable exp) {
            exp.printStackTrace();
            onUncatchedError(exp,url,args);
        } finally{
			StreamUtility.closeSilently(response);
		}
		onResponse(data,url,args);
	}

    @Override
    public void onUncatchedError(Throwable exp, URL url, Object... args) {
    }

	public abstract void onResponse(byte[] data,URL url, Object... args);
	
}
