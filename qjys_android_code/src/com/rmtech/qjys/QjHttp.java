package com.rmtech.qjys;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rmtech.qjys.callback.BaseModelCallback;
import com.rmtech.qjys.callback.QjHttpCallback;
import com.rmtech.qjys.callback.QjHttpCallbackNoParse;
import com.rmtech.qjys.callback.QjHttpCallbackWitchSaveCache;
import com.rmtech.qjys.model.CaseInfo;
import com.rmtech.qjys.model.MBase;
import com.rmtech.qjys.model.MDoctorList;
import com.rmtech.qjys.model.MGroupList;
import com.rmtech.qjys.model.MIdData;
import com.rmtech.qjys.model.MPatientList;
import com.rmtech.qjys.model.MUrlData;
import com.rmtech.qjys.model.MUser;
import com.rmtech.qjys.model.UserContext;
import com.sjl.lib.db.DBUtil;
import com.sjl.lib.http.okhttp.HttpSetting;
import com.sjl.lib.http.okhttp.OkHttpUtils;

public class QjHttp {

	private static final MediaType MEDIA_TYPE_PNG = MediaType
			.parse("image/png");

	public static final String URL_DOCTOR_APPLYCODE = "/doctor/applycode";
	public static final String URL_DOCTOR_SMSLOGIN = "/doctor/smslogin";
	public static final String URL_DOCTOR_SEARCH = "/doctor/search";
	public static final String URL_CREATE_PATIENT = "/patient/createpatient";
	public static final String URL_PATIENT_LIST = "/patient/patientlist";
	public static final String URL_ADD_FRIEND = "/doctor/addfriend";
	public static final String URL_FRIEND_LIST = "/doctor/friendlist";
	public static final String URL_ADD_MEMBERS = "/patient/addmembers";
	public static final String URL_UPDATE_PATIENT = "/patient/updatepatient";
	public static final String URL_UPLOAD_IMAGE = "/patient/uploadimage";
	public static final String URL_PATIENT_GROUPINFO = "/patient/groupinfo";


	public static void uploadImage(String patient_id, String folder_id, String name,
			String path, QjHttpCallback<MUrlData> callback) {
		HashMap<String, String> newparams = new HashMap<String, String>();
		HttpSetting.addHttpParams(newparams, URL_UPLOAD_IMAGE);
		newparams.put("patient_id", patient_id);
		newparams.put("folder_id", folder_id);

		HashMap<String, String> headers = new HashMap<String, String>();
		HttpSetting.addHttpHeader(headers);
		OkHttpUtils.post()//
				.addFile("image", name, new File(path))//
				.url(HttpSetting.BASE_URL + URL_UPLOAD_IMAGE)//
//				.mediaType(MediaType.parse("application/json; charset=utf-8"))
				.params(newparams)//
				.headers(headers)//
				.build()//
				.execute(callback);

	}
<<<<<<< HEAD



=======



>>>>>>> develop
	public static void getGroupinfo(String group_ids, QjHttpCallback<MGroupList> callback) {
		HashMap<String, String> params = new HashMap<>();
		params.put("group_ids", group_ids);
		OkHttpUtils.post(URL_PATIENT_GROUPINFO, params, callback);
	}
	public static void addMembers(String patient_id, String doctor_ids, BaseModelCallback callback) {

		HashMap<String, String> params = new HashMap<>();
		params.put("patient_id", patient_id);
		params.put("doctor_ids", doctor_ids);
		OkHttpUtils.post(URL_ADD_MEMBERS, params, callback);
	}

	public static void getVcode(String phoneNumer, BaseModelCallback callback) {
		HashMap<String, String> params = new HashMap<>();
		params.put("phone", phoneNumer);
		OkHttpUtils.post(URL_DOCTOR_APPLYCODE, params, callback);
	}

	public static void addFriend(String phoneNumer, BaseModelCallback callback) {
		HashMap<String, String> params = new HashMap<>();
		params.put("phone", phoneNumer);
		OkHttpUtils.post(URL_ADD_FRIEND, params, callback);
	}

	public static void login(String inuptPhoneStr, String codeStr,
			QjHttpCallback<MUser> callback) {
		HashMap<String, String> params = new HashMap<>();
		params.put("phone", inuptPhoneStr);
		params.put("code", codeStr);
		OkHttpUtils.post(URL_DOCTOR_SMSLOGIN, params, callback);
	}

	public static void serchContact(String toAddUsername,
			QjHttpCallback<MUser> callback) {
		HashMap<String, String> params = new HashMap<>();
		params.put("phone", toAddUsername);
		OkHttpUtils.post(URL_DOCTOR_SEARCH, params, callback);
	}

	public static void createpatient(CaseInfo info,
			QjHttpCallback<MIdData> baseModelCallback) {

		// public String abstractStr;// ： 摘要
		HashMap<String, String> params = new HashMap<>();
		if (!TextUtils.isEmpty(info.name)) {
			params.put("name", info.name);
		}
		if (!TextUtils.isEmpty(info.hos_name)) {
			params.put("hos_name", info.hos_name);
		}
		params.put("sex", info.sex + "");
		if (!TextUtils.isEmpty(info.age)) {
			params.put("age", info.age);
		}
		if (!TextUtils.isEmpty(info.department)) {
			params.put("department", info.department);
		}
		if (!TextUtils.isEmpty(info.ward_no)) {
			params.put("ward_no", info.ward_no);
		}
		if (!TextUtils.isEmpty(info.bed_no)) {
			params.put("bed_no", info.bed_no);
		}
		if (!TextUtils.isEmpty(info.diagnose)) {
			params.put("diagnose", info.diagnose);
		}
		if (!TextUtils.isEmpty(info.treat_state)) {
			params.put("treat_state", info.treat_state);
		}
		if (!TextUtils.isEmpty(info.procedure_title)) {
			params.put("procedure_title", info.procedure_title);
		}
		if (!TextUtils.isEmpty(info.procedure_text)) {
			params.put("procedure_text", info.procedure_text);
		}
		if (!TextUtils.isEmpty(info.abs)) {
			params.put("abs", info.abs);
		}
		params.put("state", info.state + "");
		OkHttpUtils.post(URL_CREATE_PATIENT, params, baseModelCallback);

	}

	public static void updatepatient(CaseInfo info,
			BaseModelCallback baseModelCallback) {

		// public String abstractStr;// ： 摘要
		if (TextUtils.isEmpty(info.id)) {
			return;
		}
		HashMap<String, String> params = new HashMap<>();
		if (!TextUtils.isEmpty(info.name)) {
			params.put("name", info.name);
		}
		if (!TextUtils.isEmpty(info.hos_name)) {
			params.put("hos_name", info.hos_name);
		}
		params.put("sex", info.sex + "");
		if (!TextUtils.isEmpty(info.age)) {
			params.put("age", info.age);
		}
		if (!TextUtils.isEmpty(info.department)) {
			params.put("department", info.department);
		}
		if (!TextUtils.isEmpty(info.ward_no)) {
			params.put("ward_no", info.ward_no);
		}
		if (!TextUtils.isEmpty(info.bed_no)) {
			params.put("bed_no", info.bed_no);
		}
		if (!TextUtils.isEmpty(info.diagnose)) {
			params.put("diagnose", info.diagnose);
		}
		if (!TextUtils.isEmpty(info.treat_state)) {
			params.put("treat_state", info.treat_state);
		}
		if (!TextUtils.isEmpty(info.procedure_title)) {
			params.put("procedure_title", info.procedure_title);
		}
		if (!TextUtils.isEmpty(info.procedure_text)) {
			params.put("procedure_text", info.procedure_text);
		}
		if (!TextUtils.isEmpty(info.abs)) {
			params.put("abs", info.abs);
		}
		params.put("state", 0 + "");
		OkHttpUtils.post(URL_UPDATE_PATIENT, params, baseModelCallback);

	}

	public static ParameterizedType type(final Class clazz, final Type... args) {
		return new ParameterizedType() {
			public Type getRawType() {
				return clazz;
			}

			public Type[] getActualTypeArguments() {
				return args;
			}

			public Type getOwnerType() {
				return null;
			}
		};
	}

	public static void getDoctorList(boolean needCache,
			final QjHttpCallbackNoParse<MDoctorList> callback) {
		postWitchCache(needCache, URL_FRIEND_LIST, null,
				new QjHttpCallbackNoParse<MDoctorList>() {

					@Override
					public MDoctorList parseNetworkResponse(String str)
							throws Exception {
						Type objectType = type(MDoctorList.class,
								new TypeToken<Map<String, List<CaseInfo>>>() {
								}.getType());

						return new Gson().fromJson(str, objectType);
					}

					@Override
					public void onResponseSucces(boolean isCache,
							MDoctorList response) {
						if (callback != null) {
							callback.onResponseSucces(isCache, response);
						}
					}

					@Override
					public void onError(Call call, Exception e) {
						if (callback != null) {
							callback.onError(call, e);
						}
					}
				});

	}

	public static void getPatientList(boolean needCache,
			final QjHttpCallbackNoParse<MPatientList> callback) {
		postWitchCache(needCache, URL_PATIENT_LIST, null,
				new QjHttpCallbackNoParse<MPatientList>() {

					@Override
					public MPatientList parseNetworkResponse(String str)
							throws Exception {
						Type objectType = type(MPatientList.class,
								new TypeToken<Map<String, List<CaseInfo>>>() {
								}.getType());

						return new Gson().fromJson(str, objectType);
					}

					@Override
					public void onResponseSucces(boolean isCache,
							MPatientList response) {
						if (callback != null) {
							callback.onResponseSucces(isCache, response);
						}
					}

					@Override
					public void onError(Call call, Exception e) {
						if (callback != null) {
							callback.onError(call, e);
						}
					}
				});

	}

	public static void postWitchCache(boolean needCache, final String url,
			final HashMap<String, String> hashMap,
			final QjHttpCallbackNoParse callback) {
		if (needCache) {
			final String cacheKey = url + UserContext.getInstance().getCookie();
			final QjHttpCallback tempCallback = new QjHttpCallbackWitchSaveCache(
					cacheKey, callback);

			new AsyncTask<Void, Void, Object>() {

				@Override
				protected Object doInBackground(Void... params) {
					Object obj = DBUtil.getCache(cacheKey);
					if (obj != null) {
						return obj;
					}
					return null;
				}

				@Override
				protected void onPostExecute(Object result) {
					super.onPostExecute(result);
					if (result != null && result instanceof MBase) {
						callback.onResponseSucces(true, (MBase) result);
					}
					OkHttpUtils.post(url, hashMap, tempCallback);
				}
			}.execute();
		} else {
			OkHttpUtils.post(url, hashMap, callback);
		}
	}

}
