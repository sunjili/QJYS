package com.sjl.lib.http.okhttp;

import java.io.Serializable;

public class OkHttpCookieManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7553833830048463505L;
	
	private String cookie;

	private volatile static OkHttpCookieManager mInstance = null;

	private OkHttpCookieManager() {
	}

	public static OkHttpCookieManager getInstance() {
		if (mInstance == null) {
			synchronized (OkHttpCookieManager.class) {
				if (mInstance == null) {
					mInstance = new OkHttpCookieManager();
				}
			}
		}
		return mInstance;
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

}