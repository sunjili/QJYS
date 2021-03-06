package com.rmtech.qjys.model;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.rmtech.qjys.utils.PreferenceManager;

public class UserContext {

	private static UserContext mUserContext;
	private UserInfo mUser = null;
	private boolean isLogined = false;

	public UserInfo getUser() {
		return mUser;
	}

	public void clear() {
		mUser = null;
		isLogined = false;
		clearCookie();
	}

	public void clearCookie() {

		SharedPreferences.Editor editor = PreferenceManager.getInstance()
				.getEditor();
		if (editor != null) {
			editor.remove("user");
			editor.putString("user", "");
			editor.commit();
		}
		cookie = null;
	}

	public boolean loadCookie() {
		isLogined = false;
		SharedPreferences preferences = PreferenceManager.getInstance()
				.getSharedPreferences();
		String json = preferences.getString("user", "");

		if (TextUtils.isEmpty(json)) {
			return false;
		}

		try {
			UserInfo user = new Gson().fromJson(json, UserInfo.class);
			if (user == null || user.getCookie() == null) {
				return false;
			}
			this.mUser = user;
			isLogined = true;
		} catch (Exception e) {
			Log.e("loadCookie", e.toString());
			return false;
		}

		return isLogined;
	}

	public void setUser(UserInfo user) {
		this.mUser = user;
		if (mUser != null && mUser.getCookie() != null) {
			cookie = mUser.getCookie();
			isLogined = true;
			saveCookieSync();
		}
	}

	private void saveCookieSync() {
		try {
			SharedPreferences.Editor editor = PreferenceManager.getInstance()
					.getEditor();
			if (editor != null) {
				Gson gson = new Gson();
				String json = gson.toJson(mUser);
				editor.putString("user", json);
				editor.commit();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void saveCookie() {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				saveCookieSync();
				return null;
			}

		}.execute();
	}

	public String cookie = "";

	public String getCookie() {
		if (!TextUtils.isEmpty(cookie)) {
			return cookie;
		}
		if (mUser != null) {
			cookie = mUser.getCookie();
		}
		return cookie;
	}

	public static UserContext getInstance() {
		if (mUserContext == null) {
			synchronized (UserContext.class) {
				mUserContext = new UserContext();
			}
		}
		return mUserContext;
	}

	private UserContext() {
	}

	public boolean isLogined() {
		return isLogined;
	}

	public String getUserName() {
		if (mUser == null) {
			return "";
		}
		return mUser.name;
	}

	public CharSequence getUserId() {
		if (mUser == null) {
			return "";
		}
		return mUser.id;
	}

	public void setUserHead(String url) {
		if (mUser != null) {
			mUser.head = url;
			saveCookie();
		}
	}

	public void setUserName(String name) {
		if (mUser != null) {
			mUser.name = name;
			saveCookie();
		}
	}

	public void setUserSex(int sex) {
		if (mUser != null) {
			mUser.sex = sex;
			saveCookie();
		}
	}
	
	public void setUserId(String id) {
		if (mUser != null) {
			mUser.id = id;
			saveCookie();
		}
	}

	public void setUserDepartment(String roomName) {
		if (mUser != null) {
			mUser.department = roomName;
			saveCookie();
		}
	}

	public void setUserHospital(String hospital) {
		if (mUser != null) {
			mUser.hos_fullname = hospital;
			saveCookie();
		}
	}

	public boolean isMyself(String id) {
		if (mUser != null) {
			return TextUtils.equals(id, mUser.id);
		}
		return false;
	}

	public void setPasswordFlag(boolean b) {
		if (mUser != null) {
			mUser.isset_passwd = b?1:0;
			saveCookie();
		}
	}

	public DoctorInfo getDoctorInfo() {
		DoctorInfo info = new DoctorInfo();
		if (mUser != null) {
			info.id = mUser.id;
			info.head = mUser.head;
			info.name = mUser.name;
		}
		return info;
	}

}
