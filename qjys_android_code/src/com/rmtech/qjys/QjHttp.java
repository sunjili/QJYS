package com.rmtech.qjys;

import java.util.HashMap;

import com.rmtech.qjys.model.MBase.BaseCallback;
import com.rmtech.qjys.model.MUser;
import com.sjl.lib.http.okhttp.OkHttpUtils;
import com.sjl.lib.http.okhttp.callback.Callback;

public class QjHttp {
	public static final String URL_DOCTOR_APPLYCODE = "/doctor/applycode";
	public static final String URL_DOCTOR_SMSLOGIN = "/doctor/smslogin";
	public static final String URL_DOCTOR_SEARCH = "/doctor/search";

	public static void getVcode(String phoneNumer,BaseCallback callback) {
		HashMap<String, String> params = new HashMap<>();
		params.put("phone", phoneNumer);
		OkHttpUtils.post(URL_DOCTOR_APPLYCODE, params, callback);
	}

	public static void login(String inuptPhoneStr, String codeStr, Callback<MUser> callback) {
		HashMap<String, String> params = new HashMap<>();
		params.put("phone", inuptPhoneStr);
		params.put("code", codeStr);
		OkHttpUtils.post(URL_DOCTOR_SMSLOGIN, params, callback);
	}

	public static void serchContact(String toAddUsername, Callback<MUser> callback) {
		HashMap<String, String> params = new HashMap<>();
		params.put("phone", toAddUsername);
		OkHttpUtils.post(URL_DOCTOR_SEARCH, params, callback);
	}

}
