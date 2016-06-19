package com.rmtech.qjys.callback;

import okhttp3.Response;
import android.util.Log;

import com.google.gson.Gson;
import com.rmtech.qjys.model.gson.MUser;
import com.sjl.lib.http.okhttp.callback.Callback;

public abstract class OnLoginListener extends Callback<MUser> {
	public abstract void onChange();

	@Override
	public MUser parseNetworkResponse(Response response) throws Exception {
		String str = response.body().string();
		Log.d("ssssssssssss","OnLoginListener response = "+str);
		MUser user = new Gson().fromJson(str, MUser.class);
		return user;

	}
}