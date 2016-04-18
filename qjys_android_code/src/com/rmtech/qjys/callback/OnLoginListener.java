package com.rmtech.qjys.callback;

import com.google.gson.Gson;
import com.rmtech.qjys.model.MUser;

public abstract class OnLoginListener extends QjHttpCallback<MUser> {
	public abstract void onChange();

	@Override
	public MUser parseNetworkResponse(String response) throws Exception {
		MUser user = new Gson().fromJson(response, MUser.class);
		return user;

	}
}