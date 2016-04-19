package com.rmtech.qjys.callback;

import okhttp3.Call;
import okhttp3.Response;

import com.rmtech.qjys.model.gson.MBase;
import com.sjl.lib.http.okhttp.callback.Callback;
import com.sjl.lib.utils.L;

/**
 * Created by jilisun on 15/12/14.
 */
public abstract class QjHttpCallbackNoParse<T extends MBase> extends QjHttpCallback<T> {

	public T parseNetworkResponse(String str) throws Exception {
		return null;
	}
	public void onResponseSucces(T response){
		onResponseSucces(false, response);
	}

	public abstract void onResponseSucces(boolean iscache, T response);

}
