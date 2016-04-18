package com.rmtech.qjys.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import android.text.TextUtils;
import android.util.Pair;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.callback.QjHttpCallback;
import com.rmtech.qjys.callback.QjHttpCallbackNoParse;
import com.rmtech.qjys.model.CaseInfo;
import com.rmtech.qjys.model.DoctorInfo;
import com.rmtech.qjys.model.MDoctorList;
import com.rmtech.qjys.model.MGroupList;

public class GroupAndCaseListManager {
	static private GroupAndCaseListManager mInstance = null;

	private HashMap<String, CaseInfo> mGIdCaseInfoMap;

	public static synchronized GroupAndCaseListManager getInstance() {
		if (mInstance == null) {
			mInstance = new GroupAndCaseListManager();
		}
		return mInstance;
	}

	private MGroupList mMGroupList;

	public void getGroupCaseInfo(String groupIds, final QjHttpCallbackNoParse<MGroupList> callback) {

		if (mMGroupList == null || mMGroupList.isEmpty()) {

			QjHttp.getGroupinfo(groupIds, new QjHttpCallback<MGroupList>() {

				@Override
				public MGroupList parseNetworkResponse(String str) throws Exception {
					return new Gson().fromJson(str, MGroupList.class);
				}

				@Override
				public void onResponseSucces(MGroupList response) {
					mMGroupList = response;
				}

				@Override
				public void onError(Call call, Exception e) {

				}
			});
		} else {
			if (callback != null) {
				callback.onResponseSucces(true, mMGroupList);
			}
		}
	}

	protected void updateIdDoctorMap() {
		if (mMGroupList != null && mMGroupList.data != null) {
			if (mGIdCaseInfoMap == null) {
				mGIdCaseInfoMap = new HashMap<String, CaseInfo>();
			}
			for (CaseInfo info : mMGroupList.data) {
				mGIdCaseInfoMap.put(info.group_id, info);
			}
		}

	}

	public static void initGroupList(List<EMConversation> list, QjHttpCallbackNoParse<MGroupList> callback) {
		Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
		// 过滤掉messages size为0的conversation
		/**
		 * 如果在排序过程中有新消息收到，lastMsgTime会发生变化 影响排序过程，Collection.sort会产生异常
		 * 保证Conversation在Sort过程中最后一条消息的时间不变 避免并发问题
		 */
		List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
		StringBuilder sb = new StringBuilder();
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
		String groupIds = sb.toString();
		if (TextUtils.isEmpty(groupIds)) {
			GroupAndCaseListManager.getInstance().getGroupCaseInfo(groupIds, callback);
		}
		try {
			sortConversationByLastChatTime(sortList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (Pair<Long, EMConversation> sortItem : sortList) {
			list.add(sortItem.second);
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

}