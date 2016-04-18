package com.rmtech.qjys.callback;

import okhttp3.Call;
import okhttp3.Response;

import com.rmtech.qjys.model.MBase;
import com.sjl.lib.http.okhttp.callback.Callback;
import com.sjl.lib.utils.L;

/**
 * Created by jilisun on 15/12/14.
 */
public abstract class QjHttpCallback<T extends MBase> extends Callback<T> {

	@Override
	public T parseNetworkResponse(Response response) throws Exception {
		String str = response.body().string();
		L.e("request url= " + response.request().url());
		L.e("response = " + str);
		return parseNetworkResponse(str);
	}

	public abstract void onError(Call call, Exception e);

	public void onResponse(T response) {
		if(response.ret == 0) {
			onResponseSucces(response);
		} else {
			onError(null, new Exception("response.ret = "+response.ret + " response.msg="+response.msg));
		}
	}

	public abstract void onResponseSucces(T response);

	public abstract T parseNetworkResponse(String str) throws Exception;

}
