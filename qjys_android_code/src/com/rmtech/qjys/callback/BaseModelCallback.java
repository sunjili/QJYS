package com.rmtech.qjys.callback;

import com.google.gson.Gson;
import com.rmtech.qjys.model.gson.MBase;

public abstract class BaseModelCallback extends QjHttpCallback<MBase> {

	public MBase parseNetworkResponse(String response) throws Exception {
		MBase user = new Gson().fromJson(response, MBase.class);
		return user;

	}

}