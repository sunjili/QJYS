package com.rmtech.qjys.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import android.text.TextUtils;
import android.util.Pair;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.callback.QjHttpCallbackNoParse;
import com.rmtech.qjys.event.CaseEvent;
import com.rmtech.qjys.model.CaseInfo;
import com.rmtech.qjys.model.gson.MGroupList;
import com.rmtech.qjys.model.gson.MPatientList;
import com.rmtech.qjys.model.gson.MPatientList.HospitalCaseInfo;

public class GroupAndCaseListManager {
	static private GroupAndCaseListManager mInstance = null;

	private Map<String, CaseInfo> mGIdCaseInfoMap = Collections.synchronizedMap(new HashMap<String, CaseInfo>()); // key:
																													// groupid
	private Map<String, CaseInfo> mCaseIdCaseInfoMap = Collections.synchronizedMap(new HashMap<String, CaseInfo>()); // key:caseid
	private Set<CaseInfo> mCaseInfoSet = Collections.synchronizedSet(new HashSet<CaseInfo>());

	private MGroupList mMGroupList;

	private boolean isChanged;

	public static synchronized GroupAndCaseListManager getInstance() {
		if (mInstance == null) {
			mInstance = new GroupAndCaseListManager();

		}
		return mInstance;

	}

	private GroupAndCaseListManager() {
		// EventBus.getDefault().register(this);
	}

	// @Subscribe
	// public void onEvent(CaseEvent event) {
	// if (event == null) {
	// return;
	// }
	// CaseInfo caseInfo = mCaseIdCaseInfoMap.get(event.caseInfoId);
	// if (caseInfo == null) {
	// return;
	// }
	// switch (event.type) {
	// case CaseEvent.TYPE_GROUP_CHANGED_ADD:
	// if (event.addDoctorList != null) {
	// if (caseInfo.participate_doctor == null) {
	// caseInfo.participate_doctor = new ArrayList<>();
	// }
	// caseInfo.participate_doctor.addAll(event.addDoctorList);
	// }
	// break;
	// case CaseEvent.TYPE_GROUP_CHANGED_DELETE:
	// if (event.deleteDoctorList != null) {
	// if (caseInfo.participate_doctor != null) {
	// caseInfo.participate_doctor
	// .removeAll(event.deleteDoctorList);
	// }
	// }
	// break;
	// case CaseEvent.TYPE_GROUP_CHANGED_ADMIN:
	// if (event.addDoctorList != null && !event.addDoctorList.isEmpty()) {
	// caseInfo.admin_doctor = event.addDoctorList.get(0);
	// }
	// break;
	// }
	// }

	public static void getPatientList(boolean needCache, final QjHttpCallbackNoParse<MPatientList> callback) {
		QjHttp.getPatientList("0", true, new QjHttpCallbackNoParse<MPatientList>() {

			@Override
			public void onError(Call call, Exception e) {
				if (callback != null) {
					callback.onError(call, e);
				}
			}

			@Override
			public void onResponseSucces(boolean iscache, MPatientList response) {
				if (callback != null) {
					callback.onResponseSucces(iscache, response);
				}
				if (response != null && response.hasData()) {
					for (HospitalCaseInfo hInfo : response.data.lists) {
						if (hInfo != null) {
							GroupAndCaseListManager.getInstance().updataCacheByCaseList(hInfo.patients, true);
						}
					}
				}
			}
		});

	}

	protected void updataCacheByCaseList(List<CaseInfo> caselist, boolean hasFlow) {
		if (caselist == null) {
			return;
		}
		for (CaseInfo info : caselist) {
			if (mCaseInfoSet.contains(info)) {
				if (hasFlow) {
					mCaseInfoSet.remove(info);
					mCaseInfoSet.add(info);
					mCaseIdCaseInfoMap.put(info.id, info);
					if (!TextUtils.isEmpty(info.group_id)) {
						mGIdCaseInfoMap.put(info.group_id, info);
					}
				}
			} else {
				mCaseInfoSet.add(info);
				mCaseIdCaseInfoMap.put(info.id, info);
				if (!TextUtils.isEmpty(info.group_id)) {
					mGIdCaseInfoMap.put(info.group_id, info);
				}
			}

		}
	}

	//
	// protected void updateGIdCaseMap() {
	// if (mMGroupList != null && mMGroupList.data != null) {
	// if (mGIdCaseInfoMap == null) {
	// mGIdCaseInfoMap = new HashMap<String, CaseInfo>();
	// }
	// for (CaseInfo info : mMGroupList.data) {
	// mGIdCaseInfoMap.put(info.group_id, info);
	// }
	// }
	//
	// }

	public void getGroupCaseInfo(final boolean needCache, String groupIds,
			final QjHttpCallbackNoParse<MGroupList> callback) {

		// if (mMGroupList == null || mMGroupList.isEmpty()) {

		QjHttp.getGroupinfo(needCache, groupIds, new QjHttpCallbackNoParse<MGroupList>() {

			@Override
			public MGroupList parseNetworkResponse(String str) throws Exception {
				return new Gson().fromJson(str, MGroupList.class);
			}

			@Override
			public void onResponseSucces(boolean isCache, MGroupList response) {
				mMGroupList = response;
				if (response != null && response.data != null) {
					updataCacheByCaseList(response.data, false);
				}
				if (callback != null) {
					callback.onResponseSucces(response);
				}
			}

			@Override
			public void onError(Call call, Exception e) {
				if (callback != null) {
					callback.onError(call, e);
				}
			}
		});
		// } else {
		// if (callback != null) {
		// callback.onResponseSucces(true, mMGroupList);
		// }
		// }
	}

	public static void initGroupList(boolean needCache, List<EMConversation> list,
			QjHttpCallbackNoParse<MGroupList> callback) {
		Map<String, EMConversation> conversations = null;
		try {
			conversations = EMClient.getInstance().chatManager().getAllConversations();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// 过滤掉messages size为0的conversation
		/**
		 * 如果在排序过程中有新消息收到，lastMsgTime会发生变化 影响排序过程，Collection.sort会产生异常
		 * 保证Conversation在Sort过程中最后一条消息的时间不变 避免并发问题
		 */
		List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
		StringBuilder sb = new StringBuilder();
		if (conversations != null) {
			synchronized (conversations) {
				for (EMConversation conversation : conversations.values()) {
					if (conversation.getAllMessages().size() != 0) {
						sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(),
								conversation));
						if (conversation.isGroup()) {
							sb.append(conversation.getUserName()).append(",");
						}
					}
				}
			}
		}
		String groupIds = sb.toString();

		try {
			sortConversationByLastChatTime(sortList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (Pair<Long, EMConversation> sortItem : sortList) {
			list.add(sortItem.second);
		}

		if (!TextUtils.isEmpty(groupIds)) {
			GroupAndCaseListManager.getInstance().getGroupCaseInfo(needCache,
					groupIds.substring(0, groupIds.length() - 1), callback);
		} else {
			callback.onError(null, new Exception("no group"));
		}

	}

	private static void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
		Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
			@Override
			public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

				if (con1.first == con2.first) {
					return 0;
				} else if (con2.first > con1.first) {
					return 1;
				} else {
					return -1;
				}
			}

		});
	}

	public CaseInfo getCaseInfoByGroupId(String groupId) {
		if (mGIdCaseInfoMap == null) {
			return null;
		}
		return mGIdCaseInfoMap.get(groupId);
	}

	public void setIsChanged(boolean b) {
		this.isChanged = b;

	}

	public CaseInfo getCaseInfoByCaseId(String caseId) {
		if (!TextUtils.isEmpty(caseId)) {
			return mCaseIdCaseInfoMap.get(caseId);
		}
		return null;
	}

}