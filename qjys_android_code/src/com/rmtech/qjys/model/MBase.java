package com.rmtech.qjys.model;

import java.io.Serializable;

import okhttp3.Response;

import com.google.gson.Gson;
import com.sjl.lib.http.okhttp.callback.Callback;

public class MBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int ret;//
	public String msg;

	public static abstract class BaseCallback extends Callback<MBase> {

		public MBase parseNetworkResponse(Response response) throws Exception {
			String string = response.body().string();
			MBase user = new Gson().fromJson(string, MBase.class);
			return user;

		}

	}

}
