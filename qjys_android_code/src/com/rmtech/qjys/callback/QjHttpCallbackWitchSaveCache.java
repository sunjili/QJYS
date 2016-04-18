package com.rmtech.qjys.callback;

import okhttp3.Call;

import com.rmtech.qjys.model.MBase;
import com.sjl.lib.db.DBUtil;

public class QjHttpCallbackWitchSaveCache extends QjHttpCallback {

		private QjHttpCallback callback;
		private String cacheKey;

		public QjHttpCallbackWitchSaveCache(String cacheKey, QjHttpCallback callback) {
			this.callback = callback;
			this.cacheKey = cacheKey;
		}

		@Override
		public void onError(Call call, Exception e) {
			if (callback != null) {

				callback.onError(call, e);
			}
		}

		@Override
		public void onResponseSucces(MBase response) {
			if (callback != null) {
				callback.onResponseSucces(response);
			}
		}

		@Override
		public MBase parseNetworkResponse(String str) throws Exception {
			MBase mBase = null;
			if (callback != null) {
				mBase = callback.parseNetworkResponse(str);
				DBUtil.saveCache(cacheKey, mBase);
			}
			return mBase;
		}

	}