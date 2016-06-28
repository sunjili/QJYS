package com.rmtech.qjys.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.Call;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
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
import com.rmtech.qjys.ui.qjactivity.QjLoginActivity;
import com.rmtech.qjys.ui.view.CustomSimpleDialog;
import com.rmtech.qjys.ui.view.CustomSimpleDialog.Builder;
import com.sjl.lib.db.DBUtil;

public class DoctorListManager {
	static private DoctorListManager mInstance = null;

	private static HashSet<String> mDeletedFriends;
	private static HashSet<String> mDeletedGroups;
	private static HashSet<String> mBeDeletedGroups;

	private HashMap<String, DoctorInfo> mIdDoctorMap;
	
	private MDoctorList mMDoctorList;

	private boolean isChanged;

	
	public void clear() {
		if(mDeletedFriends != null) {
			mDeletedFriends.clear();
		}
		if(mDeletedGroups != null) {
			mDeletedGroups.clear();
		}
		if(mBeDeletedGroups != null) {
			mBeDeletedGroups.clear();
		}
		if(mIdDoctorMap != null) {
			mIdDoctorMap.clear();
		}
		if(mMDoctorList != null) {
			mMDoctorList = null;
		}
	}

	public static synchronized DoctorListManager getInstance() {
		if (mInstance == null) {
			mInstance = new DoctorListManager();
		}
		return mInstance;
	}

	
	public void setIsChanged(boolean isChanged) {
		this.isChanged = isChanged;
	}

	public void getDoctorList(boolean needCache, final QjHttpCallbackNoParse<MDoctorList> callback) {

		if (mMDoctorList == null || mMDoctorList.isEmpty() || isChanged) {

			if(isChanged) {
				needCache = false;
			}
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
						updateIdDoctorMap(1);
					}
					if (callback != null) {
						callback.onResponseSucces(iscache, response);
					}
					isChanged = false;
				}
			});
		} else {
			if (callback != null) {
				callback.onResponseSucces(true, mMDoctorList);
			}
		}
	}

	protected void updateIdDoctorMap(int isFriend) {
		final HashSet<String> cyIds = getChangyongList();
        if(cyIds==null){
	        return;
        }
        if(mDeletedFriends == null) {
        	getDeletedFriends();
        }
        if(mDeletedFriends != null) {
        	for(String doc_id : mDeletedFriends) {
        		getDoctorInfoByHXid(doc_id, new OnGetDoctorInfoCallback() {
					
					@Override
					public void onGet(DoctorInfo info) {
						if(info != null) {
							info.isFriend = 0;
						}
					}
				});
        	}
        }
		if (mMDoctorList != null && mMDoctorList.data != null) {
			if (mIdDoctorMap == null) {
				mIdDoctorMap = new HashMap<String, DoctorInfo>();
			}
			for (DoctorInfo info : mMDoctorList.data) {
				info.isFriend = isFriend;
				if (cyIds.contains(info.id)) {
					info.mostUser = 1;
				} else {
					info.mostUser = 0;
				}
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
			user.setNick(info.getDisplayName());
			contactList.add(user);
		}
		// 排序
		Collections.sort(contactList, new Comparator<EaseUser>() {

			@Override
			public int compare(EaseUser lhs, EaseUser rhs) {
				if (lhs.doctorInfo != null && rhs.doctorInfo != null) {
					if (lhs.doctorInfo.mostUser != rhs.doctorInfo.mostUser) {
						return -lhs.doctorInfo.mostUser + rhs.doctorInfo.mostUser;
					}
				}
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
		if (TextUtils.equals(username, UserContext.getInstance().getUserId())) {
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

	public static void addFriendByPhoneNumber(final Activity activity, final String phoneNumber, final String reason,
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
				
				if (response.data != null) {
					if (!TextUtils.isEmpty(response.data.id)) {
						sendAddFriendRequest(activity, response.data.id, reason, callback);
						return;
					}
				}
//				new EaseAlertDialog(activity, "服务器未查询到此用户").show();

				CustomSimpleDialog.Builder builder = new Builder(activity);  
		        builder.setTitle("提示");  
		        builder.setMessage("您的好友尚未安装奇迹医生App，赶紧给Ta发短信提醒Ta下载吧！");  
		        builder.setPositiveButton("发短信", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 发短信操作
						Uri smsToUri = Uri.parse("smsto:" + phoneNumber);    
					    Intent mIntent = new Intent(android.content.Intent.ACTION_SENDTO, smsToUri);  
					    mIntent.putExtra("sms_body", "我是 " + UserContext.getInstance().getUserName() 
					        + ",正在使用奇迹医生App，申请加你为好友，你可以访问此链接下载并安装奇迹医生App：" + "http://m.qijiyisheng.com "
					    		+ "，然后通过搜索我的手机号就可以加我为好友了！【奇迹医生】");
					    activity.startActivity(mIntent);
						dialog.dismiss();
					}
				});  
                builder.setNegativeButton("取消", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						dialog.dismiss();
					}
				}); 
		        builder.create().show();
				if (callback != null) {
					callback.onSendRequestError();
				}

			}
		});
	}

	public static void sendAddFriendRequest(final Activity context, final String tagUserId, final String reason,
			final OnAddFriendCallback callback) {

		// final UserInfo info = (UserInfo) nameText.getTag();
//		if (TextUtils.isEmpty(tagUserId)) {
//			new EaseAlertDialog(context, R.string.not_add_myself).show();
//			if (callback != null) {
//				callback.onSendRequestError();
//			}
//			return;
//		}
		if (EMClient.getInstance().getCurrentUser().equals(tagUserId)) {
			new EaseAlertDialog(context, R.string.not_add_myself).show();
			if (callback != null) {
				callback.onSendRequestError();
			}
			return;
		}
		
		DoctorListManager.getInstance().getDoctorInfoByHXid(tagUserId, new OnGetDoctorInfoCallback() {
			
			@Override
			public void onGet(DoctorInfo info) {
				if(info != null && info.isFriend == 1) {
					if (callback != null) {
						callback.onSendRequestError();
					}
					// 提示已在好友列表中(在黑名单列表里)，无需添加
//					if (EMClient.getInstance().contactManager().getBlackListUsernames().contains(tagUserId)) {
//						new EaseAlertDialog(context, R.string.user_already_in_contactlist).show();
//
//						return;
//					}
//					new EaseAlertDialog(context, R.string.This_user_is_already_your_friend).show();
//					return;
				}
				
			}
		});
		
//		if (QjHelper.getInstance().getContactList().containsKey(tagUserId)) {
//			if (callback != null) {
//				callback.onSendRequestError();
//			}
//			// 提示已在好友列表中(在黑名单列表里)，无需添加
//			if (EMClient.getInstance().contactManager().getBlackListUsernames().contains(tagUserId)) {
//				new EaseAlertDialog(context, R.string.user_already_in_contactlist).show();
//
//				return;
//			}
//			new EaseAlertDialog(context, R.string.This_user_is_already_your_friend).show();
//			return;
//		}

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


	public static HashSet<String> getChangyongList() {
		HashSet<String> set = new HashSet<String>();
		return (HashSet<String>) DBUtil.getCache("changyong");
	}
	
	public static HashSet<String> getDeleteGroupIds() {
		try {
			return (HashSet<String>) DBUtil.getCache("DeleteGroupIds");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	public static HashSet<String> getBeDeleteGroupIds() {
		try {
			return (HashSet<String>) DBUtil.getCache("BeDeletedGroups");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	private static HashSet<String>  getDeletedFriends() {
		try {
			return (HashSet<String>) DBUtil.getCache("DeletedFriends");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void initData() {
		mDeletedFriends = getDeletedFriends();
		mDeletedGroups = getDeleteGroupIds();
		mBeDeletedGroups = getBeDeleteGroupIds();
	}

	public static boolean isGroupDeleted(String group_id) {
		if(mDeletedGroups != null) {
			return mDeletedGroups.contains(group_id);
		}
		return false;
	}
	
	public static boolean isGroupBeDeleted(String group_id) {
		if(mBeDeletedGroups != null) {
			return mBeDeletedGroups.contains(group_id);
		}
		return false;
	}
	
	public static void addDeletedFriends(String doc_id) {
		DoctorListManager.getInstance().getDoctorInfoByHXid(doc_id, new OnGetDoctorInfoCallback() {

			@Override
			public void onGet(DoctorInfo info) {
				if (info != null) {
					info.isFriend = 0;
				}
			}
		});
		mDeletedFriends = getDeletedFriends();
		if(mDeletedFriends==null){
			mDeletedFriends = new HashSet<String>();
		}
		mDeletedFriends.add(doc_id);
		DBUtil.saveCache("DeletedFriends", mDeletedFriends);
	}
	
	public static void addDeletedGroupIds(String groupId) {
		
		mDeletedGroups = getDeleteGroupIds();
		if(mDeletedGroups==null){
			mDeletedGroups = new HashSet<String>();
		}
		mDeletedGroups.add(groupId);
		DBUtil.saveCache("DeleteGroupIds", mDeletedGroups);
	}
	
	public static void removeDeletedGroupIds(String groupId){
		mDeletedGroups = getDeleteGroupIds();
		if(mDeletedGroups==null){
			mDeletedGroups = new HashSet<String>();
		}
		if(mDeletedGroups.contains(groupId)){
			mDeletedGroups.remove(groupId);
		}
		DBUtil.saveCache("DeleteGroupIds", mDeletedGroups);
	}
	
	public static void addBeDeletedGroupIds(String groupId) {
		
		mBeDeletedGroups = getBeDeleteGroupIds();
		if(mBeDeletedGroups==null){
			mBeDeletedGroups = new HashSet<String>();
		}
		mBeDeletedGroups.add(groupId);
		DBUtil.saveCache("BeDeletedGroups", mBeDeletedGroups);
	}
	
	public static void removeBeDeletedGroupIds(String groupId){
		mBeDeletedGroups = getBeDeleteGroupIds();
		if(mBeDeletedGroups==null){
			mBeDeletedGroups = new HashSet<String>();
		}
		if(mBeDeletedGroups.contains(groupId)){
			mBeDeletedGroups.remove(groupId);
		}
		DBUtil.saveCache("BeDeletedGroups", mBeDeletedGroups);
	}
	
	public static void setMostUse(String doc_id, final boolean mostUse) {
//		DoctorListManager.getInstance().setIsChanged(true);// = true;
		DoctorListManager.getInstance().getDoctorInfoByHXid(doc_id, new OnGetDoctorInfoCallback() {

			@Override
			public void onGet(DoctorInfo info) {
				if (info != null) {
					info.mostUser = mostUse ? 1 : 0;
				}
			}
		});
		HashSet<String> set = getChangyongList();
		if(set==null){
			set = new HashSet<String>();
		}
		if (mostUse) {
			set.add(doc_id);
		} else {
			set.remove(doc_id);
		}
		HashSet<String> newSet = new HashSet<String>();
		newSet.addAll(set);
		DBUtil.saveCache("changyong", newSet);
//		PreferenceManager.getInstance().getEditor().remove("changyong");
//		PreferenceManager.getInstance().getEditor().putStringSet("changyong", newSet).commit();

	}

	public void deleteDoctorInfo(String id) {
		if(mIdDoctorMap != null) {
			mIdDoctorMap.remove(id);
		}
	}

}