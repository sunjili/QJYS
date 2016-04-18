package com.rmtech.qjys.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.callback.QjHttpCallbackNoParse;
import com.rmtech.qjys.model.DoctorInfo;
import com.rmtech.qjys.model.MDoctorList;

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

	public void getDoctorList(final QjHttpCallbackNoParse<MDoctorList> callback) {

		if (mMDoctorList == null || mMDoctorList.isEmpty()) {

			QjHttp.getDoctorList(true, new QjHttpCallbackNoParse<MDoctorList>() {

				@Override
				public void onError(Call call, Exception e) {
					callback.onError(call, e);
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
			} else {
				mIdDoctorMap.clear();
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

	public String getNickByHXid(String username) {
		if (mIdDoctorMap != null) {
			DoctorInfo info = mIdDoctorMap.get(username);
			if (info != null) {
				return info.name;
			}
		}
		return null;
	}
}