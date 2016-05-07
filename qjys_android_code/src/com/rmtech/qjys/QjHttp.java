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
import com.rmtech.qjys.model.UserContext;
import com.rmtech.qjys.model.gson.MBase;
import com.rmtech.qjys.model.gson.MDoctorList;
import com.rmtech.qjys.model.gson.MFlowList;
import com.rmtech.qjys.model.gson.MFlowList.FlowInfo;
import com.rmtech.qjys.model.gson.MFolderInfo;
import com.rmtech.qjys.model.gson.MGroupData;
import com.rmtech.qjys.model.gson.MGroupList;
import com.rmtech.qjys.model.gson.MHosList;
import com.rmtech.qjys.model.gson.MIdData;
import com.rmtech.qjys.model.gson.MImageList;
import com.rmtech.qjys.model.gson.MPatientList;
import com.rmtech.qjys.model.gson.MStateList;
import com.rmtech.qjys.model.gson.MUploadImageInfo;
import com.rmtech.qjys.model.gson.MUrlData;
import com.rmtech.qjys.model.gson.MUser;
import com.sjl.lib.db.DBUtil;
import com.sjl.lib.http.okhttp.HttpSetting;
import com.sjl.lib.http.okhttp.OkHttpUtils;
import com.sjl.lib.utils.L;

public class QjHttp {

	private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

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
	public static final String URL_UPLOAD_HEAD = "/doctor/uploadhead";
	public static final String URL_PATIENT_GROUPINFO = "/patient/groupinfo";
	public static final String URL_PATIENT_IMAGE_LIST = "/patient/imagelist";
	public static final String URL_PATIENT_CREATE_FOLDER = "/patient/createfolder";
	public static final String URL_PATIENT_DELETE_MEMBERS = "/patient/deletemembers";
	public static final String URL_UPDATE_USERINFO = "/doctor/updateuserinfo";
	public static final String URL_GET_HOSBYNAME = "/hospital/gethosbyname";
	public static final String URL_GET_USERSINFOBYIDS = "/doctor/getusersinfobyids";
	public static final String URL_UPDATE_ADMIN = "/patient/updateadmin";
	public static final String URL_DELETE_PATIENT = "/patient/deletepatients";
	public static final String URL_TREATE_PROCEDURELIST = "/doctor/treateprocedurelist";
	public static final String URL_SAVE_TREATEPROCEDURE = "/doctor/savetreateprocedure";
	public static final String URL_TREATE_STATE_LIST = "/doctor/treatestatelist";
	public static final String URL_DELETE_TREATE_STATE = "/doctor/deletetreatestate";
	public static final String URL_ADD_TREATESTATE = "/doctor/addtreatestate";

	/**
	 * 7 添加就诊状态 /doctor/addtreatestate 请求方法： post
	 * 
	 * 请求参数 name: 姓名
	 */
	public static void addTreateState(String name, BaseModelCallback callback) {
		if (name == null) {
			return;
		}
		HashMap<String, String> params = new HashMap<>();
		params.put("name", name);
		OkHttpUtils.post(URL_ADD_TREATESTATE, params, callback);
	}

	/**
	 * /doctor/deletetreatestate 请求方法： post
	 * 
	 * 请求参数 id: id
	 */
	public static void deleteTreateState(String id, BaseModelCallback callback) {
		HashMap<String, String> params = new HashMap<>();
		params.put("id", id);
		OkHttpUtils.post(URL_DELETE_TREATE_STATE, params, callback);
	}

	/**
	 * 6 就诊状态列表 /doctor/treatestatelist
	 * 
	 */
	public static void treateStateList(final QjHttpCallbackNoParse<MStateList> callback) {
		// OkHttpUtils.post(URL_TREATE_PROCEDURELIST, null, callback);
		String cacheKey = URL_TREATE_STATE_LIST + UserContext.getInstance().getCookie();
		postWitchCache(cacheKey, URL_TREATE_STATE_LIST, null, new QjHttpCallbackNoParse<MStateList>() {

			@Override
			public MStateList parseNetworkResponse(String str) throws Exception {
				return new Gson().fromJson(str, MStateList.class);
			}

			@Override
			public void onResponseSucces(boolean isCache, MStateList response) {
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

	/**
	 * 9 诊疗规范列表 /doctor/treateprocedurelist
	 * 
	 */
	public static void treateProcedurelist(final QjHttpCallbackNoParse<MFlowList> callback) {
		// OkHttpUtils.post(URL_TREATE_PROCEDURELIST, null, callback);
		String cacheKey = URL_TREATE_PROCEDURELIST + UserContext.getInstance().getCookie();
		postWitchCache(cacheKey, URL_TREATE_PROCEDURELIST, null, new QjHttpCallbackNoParse<MFlowList>() {

			@Override
			public MFlowList parseNetworkResponse(String str) throws Exception {
				return new Gson().fromJson(str, MFlowList.class);
			}

			@Override
			public void onResponseSucces(boolean isCache, MFlowList response) {
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

	/**
	 * 38 删除病例 /patient/deletepatient 参数 patient_id : 病例id
	 * 
	 * @param patient_id
	 * @param callback
	 */
	public static void deletePatient(String patient_id, BaseModelCallback callback) {
		HashMap<String, String> params = new HashMap<>();
		params.put("patient_ids", patient_id);
		params.put("type", 2 + "");
		// patient_ids : 病例id,以逗号分隔
		// type : 2 回收站 3彻底删除
		OkHttpUtils.post(URL_DELETE_PATIENT, params, callback);
	}

	/**
	 * 10 添加诊疗规范
	 * 
	 * @param flowInfo
	 * @param callback
	 */
	public static void savetreateprocedure(FlowInfo flowInfo, BaseModelCallback callback) {
		if (flowInfo == null) {
			return;
		}
		HashMap<String, String> params = new HashMap<>();
		params.put("title", flowInfo.title);
		params.put("procedure", flowInfo.procedure);
		if (!TextUtils.isEmpty(flowInfo.id)) {
			params.put("id", flowInfo.id);
		}
		OkHttpUtils.post(URL_SAVE_TREATEPROCEDURE, params, callback);
	}

	public static void updateAdmin(String patient_id, String admin_id, BaseModelCallback callback) {
		// patient_id : 病例id
		// admin_id: 新的管理员id
		HashMap<String, String> params = new HashMap<>();
		params.put("patient_id", patient_id);
		params.put("admin_id", admin_id);
		OkHttpUtils.post(URL_UPDATE_ADMIN, params, callback);
	}

	public static void getDoctorByIds(boolean needCache, String doctor_ids,
			final QjHttpCallbackNoParse<MDoctorList> callback) {
		HashMap<String, String> params = new HashMap<>();
		params.put("doctor_ids", doctor_ids);
		String cacheKey = null;
		if (needCache) {
			cacheKey = URL_GET_USERSINFOBYIDS + UserContext.getInstance().getCookie();
		}
		postWitchCache(cacheKey, URL_GET_USERSINFOBYIDS, params, new QjHttpCallbackNoParse<MDoctorList>() {

			@Override
			public MDoctorList parseNetworkResponse(String str) throws Exception {
				Type objectType = type(MDoctorList.class, new TypeToken<Map<String, List<CaseInfo>>>() {
				}.getType());

				return new Gson().fromJson(str, objectType);
			}

			@Override
			public void onResponseSucces(boolean isCache, MDoctorList response) {
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

	public static void getHosByName(String name, QjHttpCallback<MHosList> callback) {

		HashMap<String, String> params = new HashMap<>();
		params.put("name", name);
		OkHttpUtils.post(URL_GET_HOSBYNAME, params, callback);
	}

	public static void deleteMembers(String patient_id, String doctor_ids, BaseModelCallback callback) {

		HashMap<String, String> params = new HashMap<>();
		params.put("patient_id", patient_id);
		params.put("doctor_ids", doctor_ids);
		OkHttpUtils.post(URL_PATIENT_DELETE_MEMBERS, params, callback);
	}

	public static void createFolder(String patient_id, String name, String parent_id,
			QjHttpCallback<MFolderInfo> callback) {
		// patient_id : 病例id
		// name: 文件夹名
		// parent_id：父文件夹id，按照目前情况，只有一级文件夹，该参数可以忽略
		HashMap<String, String> params = new HashMap<>();
		params.put("patient_id", patient_id);
		params.put("name", name);
		if (!TextUtils.isEmpty(parent_id)) {
			params.put("parent_id", parent_id);
		}
		OkHttpUtils.post(URL_PATIENT_CREATE_FOLDER, params, callback);
	}

	public static void uploadHead(String path, QjHttpCallback<MUrlData> callback) {
		HashMap<String, String> newparams = new HashMap<String, String>();
		HttpSetting.addHttpParams(newparams, URL_UPLOAD_IMAGE);
		HashMap<String, String> headers = new HashMap<String, String>();
		HttpSetting.addHttpHeader(headers);
		OkHttpUtils
				.post()
				//

				.addFile("image", "head", new File(path))
				//
				.url(HttpSetting.BASE_URL + URL_UPLOAD_HEAD)
				//
				// .mediaType(MediaType.parse("application/json; charset=utf-8"))
				.params(newparams)
				//
				.headers(headers)
				//
				.build()
				//
				.connTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS).readTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS)
				.writeTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS).execute(callback);
		// OkHttpUtils.getInstance().cancelTag(tag);

	}

	public static void uploadImage(int tag, String patient_id, String folder_id, String name, String path,
			QjHttpCallback<MUploadImageInfo> callback) {
		HashMap<String, String> newparams = new HashMap<String, String>();
		HttpSetting.addHttpParams(newparams, URL_UPLOAD_IMAGE);
		newparams.put("patient_id", patient_id);
		if (!TextUtils.isEmpty(folder_id)) {
			newparams.put("folder_id", folder_id);
		}
		L.e("path=" + path);
		HashMap<String, String> headers = new HashMap<String, String>();
		HttpSetting.addHttpHeader(headers);
		OkHttpUtils
				.post()
				//

				.addFile("image", name, new File(path))
				//
				.url(HttpSetting.BASE_URL + URL_UPLOAD_IMAGE)
				//
				// .mediaType(MediaType.parse("application/json; charset=utf-8"))
				.params(newparams)
				//
				.headers(headers)
				//
				.tag(tag).build()
				//
				.connTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS).readTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS)
				.writeTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS).execute(callback);
		// OkHttpUtils.getInstance().cancelTag(tag);

	}

	public static void getGroupinfo(boolean needCache, String group_ids,
			final QjHttpCallbackNoParse<MGroupList> callback) {
		HashMap<String, String> params = new HashMap<>();
		params.put("group_ids", group_ids);
		// OkHttpUtils.post(URL_PATIENT_GROUPINFO, params, callback);
		String cacheKey = null;
		if (needCache) {
			cacheKey = URL_PATIENT_GROUPINFO + UserContext.getInstance().getCookie();
		}
		postWitchCache(cacheKey, URL_PATIENT_GROUPINFO, params, new QjHttpCallbackNoParse<MGroupList>() {

			@Override
			public MGroupList parseNetworkResponse(String str) throws Exception {
				// Type objectType = type(MDoctorList.class, new
				// TypeToken<Map<String, List<CaseInfo>>>() {
				// }.getType());

				return new Gson().fromJson(str, MGroupList.class);
			}

			@Override
			public void onResponseSucces(boolean isCache, MGroupList response) {
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

	public static void getImageList(boolean needCache, String patient_id, String folder_id,
			final QjHttpCallbackNoParse<MImageList> callback) {
		HashMap<String, String> params = new HashMap<>();
		params.put("patient_id", patient_id);
		params.put("folder_id", folder_id);
		// OkHttpUtils.post(URL_PATIENT_IMAGE_LIST, params, callback);
		String cacheKey = null;
		if (needCache) {
			cacheKey = URL_PATIENT_IMAGE_LIST + UserContext.getInstance().getCookie() + patient_id + folder_id;
		}
		postWitchCache(cacheKey, URL_PATIENT_IMAGE_LIST, params, new QjHttpCallbackNoParse<MImageList>() {

			@Override
			public MImageList parseNetworkResponse(String str) throws Exception {
				// Type objectType = type(MDoctorList.class, new
				// TypeToken<Map<String, List<CaseInfo>>>() {
				// }.getType());

				return new Gson().fromJson(str, MImageList.class);
			}

			@Override
			public void onResponseSucces(boolean isCache, MImageList response) {
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

	public static void addMembers(CaseInfo caseInfo, String doctor_ids, QjHttpCallback<MGroupData> callback) {

		HashMap<String, String> params = new HashMap<>();
		params.put("patient_id", caseInfo.id);
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

	public static void login(String inuptPhoneStr, String codeStr, QjHttpCallback<MUser> callback) {
		HashMap<String, String> params = new HashMap<>();
		params.put("phone", inuptPhoneStr);
		params.put("code", codeStr);
		OkHttpUtils.post(URL_DOCTOR_SMSLOGIN, params, callback);
	}

	public static void serchContact(String toAddUsername, QjHttpCallback<MUser> callback) {
		HashMap<String, String> params = new HashMap<>();
		params.put("phone", toAddUsername);
		OkHttpUtils.post(URL_DOCTOR_SEARCH, params, callback);
	}

	public static void updateUserinfo(HashMap<String, String> params, BaseModelCallback baseModelCallback) {

		OkHttpUtils.post(URL_UPDATE_USERINFO, params, baseModelCallback);

	}

	public static void createpatient(CaseInfo info, QjHttpCallback<MIdData> baseModelCallback) {

		// public String abstractStr;// ： 摘要
		HashMap<String, String> params = new HashMap<>();
		if (!TextUtils.isEmpty(info.name)) {
			params.put("name", info.name);
		}
		if (!TextUtils.isEmpty(info.hos_fullname)) {
			params.put("hos_name", info.hos_fullname);
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
		String doc_ids = info.getParticipateDoctorIds();
		// if (!TextUtils.isEmpty(info.doc_ids)) {
		params.put("doc_ids", doc_ids);
		// }
		params.put("state", info.state + "");
		OkHttpUtils.post(URL_CREATE_PATIENT, params, baseModelCallback);

	}

	public static void updatepatient(CaseInfo info, BaseModelCallback baseModelCallback) {

		// public String abstractStr;// ： 摘要
		if (TextUtils.isEmpty(info.id)) {
			baseModelCallback.onError(null, new IllegalArgumentException("没有ID"));
			L.e("没有ID");
			return;
		}
		HashMap<String, String> params = new HashMap<>();
		params.put("patient_id", info.id);
		if (!TextUtils.isEmpty(info.name)) {
			params.put("name", info.name);
		}
		if (!TextUtils.isEmpty(info.hos_fullname)) {
			params.put("hos_name", info.hos_fullname);
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
		String doc_ids = info.getParticipateDoctorIds();
		// if (!TextUtils.isEmpty(info.doc_ids)) {
		params.put("doc_ids", doc_ids);
		// }
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

	public static void getDoctorList(boolean needCache, final QjHttpCallbackNoParse<MDoctorList> callback) {
		String cacheKey = null;
		if (needCache) {
			cacheKey = URL_FRIEND_LIST + UserContext.getInstance().getCookie();
		}
		postWitchCache(cacheKey, URL_FRIEND_LIST, null, new QjHttpCallbackNoParse<MDoctorList>() {

			@Override
			public MDoctorList parseNetworkResponse(String str) throws Exception {
				Type objectType = type(MDoctorList.class, new TypeToken<Map<String, List<CaseInfo>>>() {
				}.getType());

				return new Gson().fromJson(str, objectType);
			}

			@Override
			public void onResponseSucces(boolean isCache, MDoctorList response) {
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

	public static void getPatientList(boolean needCache, final QjHttpCallbackNoParse<MPatientList> callback) {
		String cacheKey = null;
		if (needCache) {
			cacheKey = URL_PATIENT_LIST + UserContext.getInstance().getCookie();
		}
		postWitchCache(cacheKey, URL_PATIENT_LIST, null, new QjHttpCallbackNoParse<MPatientList>() {

			@Override
			public MPatientList parseNetworkResponse(String str) throws Exception {
				Type objectType = type(MPatientList.class, new TypeToken<Map<String, List<CaseInfo>>>() {
				}.getType());

				return new Gson().fromJson(str, objectType);
			}

			@Override
			public void onResponseSucces(boolean isCache, MPatientList response) {
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

	public static void postWitchCache(final String cacheKey, final String url, final HashMap<String, String> params,
			final QjHttpCallbackNoParse callback) {
		if (!TextUtils.isEmpty(cacheKey)) {
			final QjHttpCallback tempCallback = new QjHttpCallbackWitchSaveCache(cacheKey, callback);

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
					OkHttpUtils.post(url, params, tempCallback);
				}
			}.execute();
		} else {
			OkHttpUtils.post(url, params, callback);
		}
	}

}
