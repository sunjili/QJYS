package com.rmtech.qjys.callback;

import com.rmtech.qjys.model.gson.MBase;

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
