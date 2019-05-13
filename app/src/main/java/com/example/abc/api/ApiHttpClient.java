package com.example.abc.api;

import android.util.Log;

import com.loopj.android.http.*;

/**
 * @author smmh
 *	Apihttp客户端
 */
public class ApiHttpClient {
	
	public static final String API_URL = "http://192.168.27.100:8080/js/html/%s";
	
	public static AsyncHttpClient client;
	
	public ApiHttpClient() {}
	
	/**
	 * 获得静态对象
	 */
	public static AsyncHttpClient getHttpClient() {
		return client;
	}
	
	/**
	 * 设置静态对象
	 */
	public static void setHttpClient(AsyncHttpClient c) {
		client = c;
		client.setTimeout(5000);
	}
	
	
	/**
	 * GET方法，无参
	 */
	public static void get(String partUrl, AsyncHttpResponseHandler handler) {
		client.get(getAbsoluteApiUrl(partUrl), handler);
		log(new StringBuilder("GET ").append(getAbsoluteApiUrl(partUrl)).toString());
	}
	
	/**
	 * GET方法，带参
	 */
	public static void get(String partUrl, RequestParams params, 
			AsyncHttpResponseHandler handler) {
		client.get(getAbsoluteApiUrl(partUrl), params, handler);
		log(new StringBuilder("GET ").append(getAbsoluteApiUrl(partUrl))
				.append(params).toString());
	}
	
	
	/**
	 * POST方法，带参
	 */
	public static void post(String partUrl, RequestParams params,
			AsyncHttpResponseHandler handler) {
		client.post(getAbsoluteApiUrl(partUrl), params, handler);
		log(new StringBuilder("POST ").append(getAbsoluteApiUrl(partUrl))
				.append(params).toString());
	}
	
	
	/**
	 * 获取api路径
	 */
	public static String getAbsoluteApiUrl(String partUrl) {
        String url = String.format(API_URL, partUrl);
        return url;
    }
	
	/**
	 * 输出调试信息
	 */
	public static void log(String log) {
		Log.d("ApiHttpClient", log);
	}
	
	
}
