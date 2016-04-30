package com.rmtech.qjys.utils;

import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.callback.BaseModelCallback;
import com.rmtech.qjys.callback.QjHttpCallbackNoParse;
import com.rmtech.qjys.model.gson.MBase;
import com.rmtech.qjys.model.gson.MFlowList;
import com.rmtech.qjys.model.gson.MFlowList.FlowInfo;

public class FlowListManager {
	static private FlowListManager mInstance = null;

	private HashMap<String, FlowInfo> mIdFlowMap;

	public static synchronized FlowListManager getInstance() {
		if (mInstance == null) {
			mInstance = new FlowListManager();
		}
		return mInstance;
	}

	private boolean isChanged;

	public void setIsChanged(boolean isChanged) {
		this.isChanged = isChanged;
	}

	public void getFlowList(boolean needCache, final QjHttpCallbackNoParse<MFlowList> callback) {
		QjHttp.treateProcedurelist(new QjHttpCallbackNoParse<MFlowList>() {

			@Override
			public void onError(Call call, Exception e) {
				if (callback != null) {
					callback.onError(call, e);
				}
			}

			@Override
			public void onResponseSucces(boolean iscache, MFlowList response) {
				if (callback != null) {

					callback.onResponseSucces(iscache, response);
				}
				if (response.hasData()) {
					updateIdDoctorMap(response.data);

				}
			}
		});

	}

	protected void updateIdDoctorMap(List<FlowInfo> lists) {
		if (mIdFlowMap == null) {
			mIdFlowMap = new HashMap<String, FlowInfo>();
		}
		for (FlowInfo info : lists) {
			mIdFlowMap.put(info.id, info);
		}
	}

	public void updataFlowInfo(FlowInfo flowInfo) {
		if (mIdFlowMap == null) {
			mIdFlowMap = new HashMap<String, FlowInfo>();
		}
		mIdFlowMap.put(flowInfo.id, flowInfo);

	}

	public void addFlow(FlowInfo flowInfo,BaseModelCallback callback) {
		QjHttp.savetreateprocedure(flowInfo, callback);
	}

}