/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rmtech.qjys.ui.fragment;

import java.util.List;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import okhttp3.Call;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.util.EMLog;
import com.rmtech.qjys.R;
import com.rmtech.qjys.callback.QjHttpCallbackNoParse;
import com.rmtech.qjys.db.InviteMessgeDao;
import com.rmtech.qjys.db.UserDao;
import com.rmtech.qjys.event.CaseEvent;
import com.rmtech.qjys.event.DoctorEvent;
import com.rmtech.qjys.hx.QjHelper;
import com.rmtech.qjys.hx.QjHelper.DataSyncListener;
import com.rmtech.qjys.model.gson.MDoctorList;
import com.rmtech.qjys.ui.ChatActivity;
import com.rmtech.qjys.ui.GroupsActivity;
import com.rmtech.qjys.ui.NewFriendsMsgActivity;
import com.rmtech.qjys.ui.qjactivity.QjAddContactActivity;
import com.rmtech.qjys.ui.qjactivity.UserInfoActivity;
import com.rmtech.qjys.utils.DoctorListManager;
import com.rmtech.qjys.utils.GroupAndCaseListManager;
import com.rmtech.qjys.widget.ContactItemView;
import com.sjl.lib.utils.L;

/**
 * 联系人列表页
 * 
 */
public class ContactListFragment extends EaseContactListFragment {

	private static final String TAG = ContactListFragment.class.getSimpleName();
	// private ContactSyncListener contactSyncListener;
	// private BlackListSyncListener blackListSyncListener;
	// private ContactInfoSyncListener contactInfoSyncListener;
	private View loadingView;
	private ContactItemView applicationItem;
	private InviteMessgeDao inviteMessgeDao;

	@Override
	protected void initView() {
		super.initView();
		View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.em_contacts_header, null);
		HeaderItemClickListener clickListener = new HeaderItemClickListener();
		applicationItem = (ContactItemView) headerView.findViewById(R.id.application_item);
		applicationItem.setOnClickListener(clickListener);
		// headerView.findViewById(R.id.group_item).setOnClickListener(clickListener);
		// headerView.findViewById(R.id.chat_room_item).setOnClickListener(clickListener);
		// headerView.findViewById(R.id.robot_item).setOnClickListener(clickListener);
		// 添加headerview
		listView.addHeaderView(headerView);
		// 添加正在加载数据提示的loading view
		loadingView = LayoutInflater.from(getActivity()).inflate(R.layout.em_layout_loading_data, null);
		contentContainer.addView(loadingView);
		// 注册上下文菜单
		registerForContextMenu(listView);
		EventBus.getDefault().register(this);

	}
	
	@Subscribe
	public void onEvent(DoctorEvent event) {
		// mAdapter.add();
		L.d("onEvent " + event.type);
		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					DoctorListManager.getInstance().setIsChanged(true);
					refresh();
				}
			});
		}
	}

	@Override
	public void refresh() {
		// super.refresh();
		if (inviteMessgeDao == null) {
			inviteMessgeDao = new InviteMessgeDao(getActivity());
		}
		if (inviteMessgeDao.getUnreadMessagesCount() > 0) {
			applicationItem.showUnreadMsgView();
			applicationItem.setUnreadCount(inviteMessgeDao.getUnreadMessagesCount());
		} else {
			applicationItem.hideUnreadMsgView();
		}
		loadData(true);
	}
	
	EMMessageListener messageListener = new EMMessageListener() {

		@Override
		public void onMessageReceived(List<EMMessage> messages) {
			// 提示新消息
			for (EMMessage message : messages) {
				QjHelper.getInstance().getNotifier().onNewMsg(message);
			}
			inviteMessgeDao = new InviteMessgeDao(getActivity());
			if (inviteMessgeDao.getUnreadMessagesCount() > 0) {
				applicationItem.showUnreadMsgView();
				applicationItem.setUnreadCount(inviteMessgeDao.getUnreadMessagesCount());
			} else {
				applicationItem.hideUnreadMsgView();
			}
		}

		@Override
		public void onCmdMessageReceived(List<EMMessage> messages) {
		}

		@Override
		public void onMessageReadAckReceived(List<EMMessage> messages) {
		}

		@Override
		public void onMessageDeliveryAckReceived(List<EMMessage> message) {
		}

		@Override
		public void onMessageChanged(EMMessage message, Object change) {
		}
	};
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		EMClient.getInstance().chatManager().addMessageListener(messageListener);
		loadData(true);
	}

	@Override
	protected void setUpView() {
		titleBar.setRightImageResource(R.drawable.btn_addresslist_addcontacts);
		titleBar.setRightLayoutClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// startActivity(new Intent(getActivity(),
				// AddContactActivity.class));
				QjAddContactActivity.show(getActivity());
			}
		});

		// 设置联系人数据
		// setContactsMap(QjHelper.getInstance().getContactList());
		super.setUpView();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				EaseUser user = ((EaseUser) listView.getItemAtPosition(position));
				// // demo中直接进入聊天页面，实际一般是进入用户详情页
				// startActivity(new Intent(getActivity(),
				// ChatActivity.class).putExtra("userId", username));
				UserInfoActivity.show(getActivity(), user.doctorInfo, "contactList");
			}
		});

		// 进入添加好友页
		titleBar.getRightLayout().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				QjAddContactActivity.show(getActivity());
				// loadData();

			}
		});

		// contactSyncListener = new ContactSyncListener();
		// QjHelper.getInstance().addSyncContactListener(contactSyncListener);
		//
		// blackListSyncListener = new BlackListSyncListener();
		// QjHelper.getInstance().addSyncBlackListListener(blackListSyncListener);
		//
		// contactInfoSyncListener = new ContactInfoSyncListener();
		// QjHelper.getInstance().getUserProfileManager().addSyncContactInfoListener(contactInfoSyncListener);

		// if (QjHelper.getInstance().isContactsSyncedWithServer()) {
		// loadingView.setVisibility(View.GONE);
		// } else if (QjHelper.getInstance().isSyncingContactsWithServer()) {
		// loadingView.setVisibility(View.VISIBLE);
		// }
		contactListLayout.init(contactList);

		loadData(true);

	}
	

	private void loadData(boolean needCache) {

		loadingView.setVisibility(View.VISIBLE);
		DoctorListManager.getInstance().getDoctorList(needCache, new QjHttpCallbackNoParse<MDoctorList>() {

			@Override
			public void onError(Call call, Exception e) {
				loadingView.setVisibility(View.GONE);
			}

			@Override
			public void onResponseSucces(boolean iscache, MDoctorList response) {
				if (response.ret == 0 && response.data != null && !response.data.isEmpty()) {
					DoctorListManager.initDoctorList(contactList, response.data);
					contactListLayout.init(contactList);

				} else {

				}
				loadingView.setVisibility(View.GONE);
			}
		});

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);

		// if (contactSyncListener != null) {
		// QjHelper.getInstance().removeSyncContactListener(contactSyncListener);
		// contactSyncListener = null;
		// }
		//
		// if(blackListSyncListener != null){
		// QjHelper.getInstance().removeSyncBlackListListener(blackListSyncListener);
		// }
		//
		// if(contactInfoSyncListener != null){
		// QjHelper.getInstance().getUserProfileManager().removeSyncContactInfoListener(contactInfoSyncListener);
		// }
	}

	protected class HeaderItemClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.application_item:
				// 进入申请与通知页面
				startActivity(new Intent(getActivity(), NewFriendsMsgActivity.class));
				break;
			// case R.id.group_item:
			// // 进入群聊列表页面
			// startActivity(new Intent(getActivity(), GroupsActivity.class));
			// break;
			// case R.id.chat_room_item:
			// //进入聊天室列表页面
			// startActivity(new Intent(getActivity(),
			// PublicChatRoomsActivity.class));
			// break;
			// case R.id.robot_item:
			// //进入Robot列表页面
			// startActivity(new Intent(getActivity(), RobotsActivity.class));
			// break;

			default:
				break;
			}
		}

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		toBeProcessUser = (EaseUser) listView.getItemAtPosition(((AdapterContextMenuInfo) menuInfo).position);
		toBeProcessUsername = toBeProcessUser.getUsername();
		getActivity().getMenuInflater().inflate(R.menu.em_context_contact_list, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.delete_contact) {
			try {
				// 删除此联系人
				deleteContact(toBeProcessUser);
				// 删除相关的邀请消息
				InviteMessgeDao dao = new InviteMessgeDao(getActivity());
				dao.deleteMessage(toBeProcessUser.getUsername());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		} else if (item.getItemId() == R.id.add_to_blacklist) {
			moveToBlacklist(toBeProcessUsername);
			return true;
		}
		return super.onContextItemSelected(item);
	}

	/**
	 * 删除联系人
	 * 
	 * @param toDeleteUser
	 */
	public void deleteContact(final EaseUser tobeDeleteUser) {
		String st1 = getResources().getString(R.string.deleting);
		final String st2 = getResources().getString(R.string.Delete_failed);
		final ProgressDialog pd = new ProgressDialog(getActivity());
		pd.setMessage(st1);
		pd.setCanceledOnTouchOutside(false);
		pd.show();
		new Thread(new Runnable() {
			public void run() {
				try {
					EMClient.getInstance().contactManager().deleteContact(tobeDeleteUser.getUsername());
					// 删除db和内存中此用户的数据
					UserDao dao = new UserDao(getActivity());
					dao.deleteContact(tobeDeleteUser.getUsername());
					QjHelper.getInstance().getContactList().remove(tobeDeleteUser.getUsername());
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							contactList.remove(tobeDeleteUser);
							contactListLayout.refresh();

						}
					});
				} catch (final Exception e) {
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							Toast.makeText(getActivity(), st2 + e.getMessage(), 1).show();
						}
					});

				}

			}
		}).start();

	}

	class ContactSyncListener implements DataSyncListener {
		@Override
		public void onSyncComplete(final boolean success) {
			EMLog.d(TAG, "on contact list sync success:" + success);
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if (success) {
								loadingView.setVisibility(View.GONE);
								refresh();
							} else {
								String s1 = getResources().getString(R.string.get_failed_please_check);
								Toast.makeText(getActivity(), s1, 1).show();
								loadingView.setVisibility(View.GONE);
							}
						}

					});
				}
			});
		}
	}

	class BlackListSyncListener implements DataSyncListener {

		@Override
		public void onSyncComplete(boolean success) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					refresh();
				}
			});
		}

	};

	class ContactInfoSyncListener implements DataSyncListener {

		@Override
		public void onSyncComplete(final boolean success) {
			EMLog.d(TAG, "on contactinfo list sync success:" + success);
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					loadingView.setVisibility(View.GONE);
					if (success) {
						refresh();
					}
				}
			});
		}

	}

}
