package com.rmtech.qjys.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import android.app.Activity;
import android.app.ProgressDialog;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.widget.EaseAlertDialog;
import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.R;
import com.rmtech.qjys.callback.QjHttpCallback;
import com.rmtech.qjys.callback.QjHttpCallbackNoParse;
import com.rmtech.qjys.hx.QjHelper;
import com.rmtech.qjys.model.DoctorInfo;
import com.rmtech.qjys.model.UserContext;
import com.rmtech.qjys.model.gson.MDoctorList;
import com.rmtech.qjys.model.gson.MUser;

public class DoctorListManager {
	static private DoctorListManager mInstance = null;

	private HashMap<String, DoctorInfo> mIdDoctorMap;

	public static synchronized DoctorListManager getInstance() {
		if (mInstance == null) {
			mInstance = new DoctorListManager();
		}
		return mInstance;
	}

	private MDoctorList mMDoctorList;

	private boolean isChanged;

	public void setIsChanged(boolean isChanged) {
		this.isChanged = isChanged;
	}

	public void getDoctorList(boolean needCache, final QjHttpCallbackNoParse<MDoctorList> callback) {

		if (mMDoctorList == null || mMDoctorList.isEmpty() || isChanged) {

			QjHttp.getDoctorList(needCache, new QjHttpCallbackNoParse<MDoctorList>() {

				@Override
				public void onError(Call call, Exception e) {
					if (callback != null) {
						callback.onError(call, e);
					}
				}

				@Override
				public void onResponseSucces(boolean iscache, MDoctorList response) {
					if (response.ret == 0 && response.data != null && !response.data.isEmpty()) {
						mMDoctorList = response;
						updateIdDoctorMap();
					}
					if (callback != null) {

						callback.onResponseSucces(iscache, response);
					}
				}
			});
		} else {
			if (callback != null) {
				callback.onResponseSucces(true, mMDoctorList);
			}
		}
	}

	protected void updateIdDoctorMap() {
		if (mMDoctorList != null && mMDoctorList.data != null) {
			if (mIdDoctorMap == null) {
				mIdDoctorMap = new HashMap<String, DoctorInfo>();
			}
			for (DoctorInfo info : mMDoctorList.data) {
				mIdDoctorMap.put(info.id, info);
			}
		}

	}

	public static void initDoctorList(List<EaseUser> contactList, List<DoctorInfo> lists) {
		contactList.clear();
		for (DoctorInfo info : lists) {

			EaseUser user = new EaseUser(info.id);
			EaseCommonUtils.setUserInitialLetter(user);
			user.setAvatar(info.head);
			user.setNick(info.name);
			contactList.add(user);
		}
		// 排序
		Collections.sort(contactList, new Comparator<EaseUser>() {

			@Override
			public int compare(EaseUser lhs, EaseUser rhs) {
				if (lhs.getInitialLetter().equals(rhs.getInitialLetter())) {
					return lhs.getNick().compareTo(rhs.getNick());
				} else {
					if ("#".equals(lhs.getInitialLetter())) {
						return 1;
					} else if ("#".equals(rhs.getInitialLetter())) {
						return -1;
					}
					return lhs.getInitialLetter().compareTo(rhs.getInitialLetter());
				}

			}
		});

	}

	// public String getNickByHXid(String username) {
	// if (mIdDoctorMap != null) {
	// DoctorInfo info = mIdDoctorMap.get(username);
	// if (info != null) {
	// return info.name;
	// }
	// }
	// return null;
	// }

	public static interface OnGetDoctorInfoCallback {
		public void onGet(DoctorInfo info);
	}

	public void getDoctorInfoByHXid(final String username, final OnGetDoctorInfoCallback callback) {
		if(TextUtils.equals(username, EMClient.getInstance().getCurrentUser())) {
			DoctorInfo doctorInfo = new DoctorInfo(UserContext.getInstance().getUser());
			if (doctorInfo != null) {
				if (callback != null) {
					callback.onGet(doctorInfo);
					return;
				}
			}
        }
		if (mIdDoctorMap != null) {
			DoctorInfo info = mIdDoctorMap.get(username);
			if (info != null) {
				if (callback != null) {
					callback.onGet(info);
					return;
				}
			}
		}
		QjHttp.getDoctorByIds(true, username, new QjHttpCallbackNoParse<MDoctorList>() {

			@Override
			public void onError(Call call, Exception e) {
				if (callback != null) {
					callback.onGet(null);
				}
			}

			@Override
			public void onResponseSucces(boolean iscache, MDoctorList response) {
				if (response != null && response.data != null) {

					for (DoctorInfo doc : response.data) {
						if (TextUtils.equals(doc.id, username)) {
							if (callback != null) {
								callback.onGet(doc);
							}
						}

						if (mIdDoctorMap == null) {
							mIdDoctorMap = new HashMap<String, DoctorInfo>();
						}
						for (DoctorInfo info : response.data) {
							mIdDoctorMap.put(info.id, info);
						}
					}
				} else {
					if (callback != null) {
						callback.onGet(null);
					}
				}
			}
		});
	}

	public static interface OnAddFriendCallback {
		public void onSendRequestSuccess();

		public void onSendRequestError();
	}

	public static void addFriendByPhoneNumber(final Activity activity, String phoneNumber, final String reason,
			final OnAddFriendCallback callback) {
		if (TextUtils.isEmpty(phoneNumber)) {
			new EaseAlertDialog(activity, R.string.Please_enter_a_username).show();
			if (callback != null) {
				callback.onSendRequestError();
			}
			return;
		}
		// TODO 从服务器获取此contact,如果不存在提示不存在此用户
		QjHttp.serchContact(phoneNumber, new QjHttpCallback<MUser>() {

			@Override
			public MUser parseNetworkResponse(String response) throws Exception {
				MUser user = new Gson().fromJson(response, MUser.class);
				return user;
			}

			@Override
			public void onError(Call call, Exception e) {
				new EaseAlertDialog(activity, "用户未注册奇迹医生").show();
				if (callback != null) {
					callback.onSendRequestError();
				}
			}

			@Override
			public void onResponseSucces(MUser response) {
				// 服务器存在此用户，显示此用户和添加按钮
				if (response.data != null) {
					if (!TextUtils.isEmpty(response.data.id)) {
						sendAddFriendRequest(activity, response.data.id, reason, callback);
						return;
					}
				}
				new EaseAlertDialog(activity, "服务器未查询到此用户").show();
				if (callback != null) {
					callback.onSendRequestError();
				}

			}
		});
	}

	public static void sendAddFriendRequest(final Activity context, final String tagUserId, final String reason,
			final OnAddFriendCallback callback) {

		// final UserInfo info = (UserInfo) nameText.getTag();
		if (TextUtils.isEmpty(tagUserId)) {
			new EaseAlertDialog(context, R.string.not_add_myself).show();
			if (callback != null) {
				callback.onSendRequestError();
			}
			return;
		}
		if (EMClient.getInstance().getCurrentUser().equals(tagUserId)) {
			new EaseAlertDialog(context, R.string.not_add_myself).show();
			if (callback != null) {
				callback.onSendRequestError();
			}
			return;
		}

		if (QjHelper.getInstance().getContactList().containsKey(tagUserId)) {
			if (callback != null) {
				callback.onSendRequestError();
			}
			// 提示已在好友列表中(在黑名单列表里)，无需添加
			if (EMClient.getInstance().contactManager().getBlackListUsernames().contains(tagUserId)) {
				new EaseAlertDialog(context, R.string.user_already_in_contactlist).show();

				return;
			}
			new EaseAlertDialog(context, R.string.This_user_is_already_your_friend).show();
			return;
		}

		final ProgressDialog progressDialog = new ProgressDialog(context);
		String stri = context.getResources().getString(R.string.Is_sending_a_request);
		progressDialog.setMessage(stri);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();

		new Thread(new Runnable() {
			public void run() {

				try {
					// demo写死了个reason，实际应该让用户手动填入
					// String s =
					// context.getResources().getString(R.string.Add_a_friend);
					EMClient.getInstance().contactManager().addContact(tagUserId, reason);
					context.runOnUiThread(new Runnable() {
						public void run() {
							if (callback != null) {
								callback.onSendRequestSuccess();
							}
							progressDialog.dismiss();
							String s1 = context.getResources().getString(R.string.send_successful);
							Toast.makeText(context, s1, 1).show();
						}
					});
				} catch (final Exception e) {
					context.runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							String s2 = context.getResources().getString(R.string.Request_add_buddy_failure);
							Toast.makeText(context, s2 + e.getMessage(), 1).show();
							if (callback != null) {
								callback.onSendRequestError();
							}
						}
					});
				}
			}
		}).start();
	}

	public ArrayList<String> getDoctorPhonNumberList() {
		ArrayList<String> list = new ArrayList<String>();
		if (mMDoctorList != null && mMDoctorList.data != null) {
			for (DoctorInfo info : mMDoctorList.data) {
				list.add(info.phone);
			}
		}
		return list;
	}
}