package com.rmtech.qjys.ui.callback;

import okhttp3.Response;

import com.google.gson.Gson;
import com.rmtech.qjys.model.MUser;
import com.sjl.lib.http.okhttp.callback.Callback;

public abstract class OnLoginListener extends Callback<MUser> {
	public abstract void onChange();

	@Override
	public MUser parseNetworkResponse(Response response) throws Exception {
		String string = response.body().string();
		MUser user = new Gson().fromJson(string, MUser.class);
		return user;

	}
}