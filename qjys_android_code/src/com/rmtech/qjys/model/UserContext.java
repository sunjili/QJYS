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

		SharedPreferences.Editor editor = PreferenceManager.getInstance().getEditor();
		if (editor != null) {
			editor.remove("user");
			editor.putString("user", "");
			editor.commit();
		}
		cookie = null;
	}

	public boolean loadCookie() {
		isLogined = false;
		SharedPreferences preferences = PreferenceManager.getInstance().getSharedPreferences();
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
			isLogined = true;
			saveCookie();
		}
	}

	private void saveCookie() {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				try {
					SharedPreferences.Editor editor = PreferenceManager.getInstance().getEditor();
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
		return mUser.name;
	}

	public CharSequence getUserId() {
		// TODO Auto-generated method stub
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

	public void setUserDepartment(String roomName) {
		if (mUser != null) {
			mUser.department = roomName;
			saveCookie();
		}
	}

}
