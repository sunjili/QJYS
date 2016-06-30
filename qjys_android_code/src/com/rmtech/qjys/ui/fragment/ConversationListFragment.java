package com.rmtech.qjys.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.text.style.TtsSpan.ElectronicBuilder;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMConversation.EMConversationType;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.NetUtils;
import com.rmtech.qjys.QjConstant;
import com.rmtech.qjys.R;
import com.rmtech.qjys.db.InviteMessgeDao;
import com.rmtech.qjys.model.CaseInfo;
import com.rmtech.qjys.model.DoctorInfo;
import com.rmtech.qjys.ui.ChatActivity;
import com.rmtech.qjys.ui.GroupDetailsActivity;
import com.rmtech.qjys.ui.MainActivity;
import com.rmtech.qjys.ui.view.CustomSimpleDialog;
import com.rmtech.qjys.ui.view.CustomSimpleDialog.Builder;
import com.rmtech.qjys.utils.DoctorListManager;
import com.rmtech.qjys.utils.GroupAndCaseListManager;

public class ConversationListFragment extends EaseConversationListFragment {

	private TextView errorText;
	private ImageView no_data_view;
	private View errormsg_layout;

	@Override
	protected void initView() {
		super.initView();
		View errorView = (ViewGroup) View.inflate(getActivity(), R.layout.em_chat_neterror_item, null);
		errorItemContainer.addView(errorView);
		errorText = (TextView) errorView.findViewById(R.id.tv_connect_errormsg);
		errormsg_layout = errorView.findViewById(R.id.errormsg_layout);
		// conversationListView.addHeaderView(errormsg_layout);
		no_data_view = (ImageView) errorView.findViewById(R.id.no_data_view);
		no_data_view.setVisibility(View.GONE);

	}
	
	/**
	 * 连接到服务器
	 */
	protected void onConnectionConnected() {
		Log.e("onConnectionConnected", "start");
		errormsg_layout.setVisibility(View.GONE);
//		conversationListView.hideErrorView();
		if (noData()) {
			errorItemContainer.setVisibility(View.VISIBLE);
			no_data_view.setVisibility(View.VISIBLE);
		} else {
			errorItemContainer.setVisibility(View.GONE);
			Log.e("onConnectionConnected", "in");
		}
	}

	@Override
	protected void onConnectionDisconnected() {
		super.onConnectionDisconnected();
//		if (NetUtils.hasNetwork(getActivity())) {
//			errorText.setText(R.string.can_not_connect_chat_server_connection);
//		} else {
//			errorText.setText(R.string.the_current_network);
//		}
		if (noData()) {
			no_data_view.setVisibility(View.VISIBLE);
		} else {
			errormsg_layout.setVisibility(View.GONE);
			no_data_view.setVisibility(View.GONE);
//			conversationListView.showErrorView();
		}
	}

	@Override
	protected void setUpView() {
		super.setUpView();
		// 注册上下文菜单
//		registerForContextMenu(conversationListView);
		conversationListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				EMConversation conversation = conversationListView.getItem(position);
				final String username = conversation.getUserName();
				if (username.equals(EMClient.getInstance().getCurrentUser()))
					Toast.makeText(getActivity(), R.string.Cant_chat_with_yourself, 0).show();
				else {
					// 进入聊天页面
					Intent intent = new Intent(getActivity(), ChatActivity.class);
					if (conversation.isGroup()) {
						
						boolean isContainsMyself = true;
						CaseInfo mCaseInfo = GroupAndCaseListManager.getInstance().getCaseInfoByGroupId(username);
						if(DoctorListManager.isGroupBeDeleted(username)||DoctorListManager.isGroupDeleted(username)){
							isContainsMyself = false;
						}
						if (mCaseInfo == null||conversation.getLastMessage().getBody().toString().
								substring(5, conversation.getLastMessage().getBody().toString().length()-1).equals("该病例讨论组已被管理员删除！")){
							CustomSimpleDialog.Builder builder = new Builder(getActivity());
							DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									try {
										// 删除此会话
										EMClient.getInstance().chatManager().deleteConversation(username, false);
										InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
										inviteMessgeDao.deleteMessage(username);
									} catch (Exception e) {
										e.printStackTrace();
									}
									refresh();

									// 更新消息未读数
									((MainActivity) getActivity()).updateUnreadLabel();
								}

							};
							builder.setTitle("");
							builder.setMessage("病例已删除");
							builder.setNegativeButton("取消", listener);
							builder.setPositiveButton("确定", listener);
							builder.create().show();
							return;
						} else if (!isContainsMyself){
							CustomSimpleDialog.Builder builder = new Builder(getActivity());
							DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									try {
										// 删除此会话
										EMClient.getInstance().chatManager().deleteConversation(username, false);
										InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
										inviteMessgeDao.deleteMessage(username);
									} catch (Exception e) {
										e.printStackTrace();
									}
									refresh();

									// 更新消息未读数
									((MainActivity) getActivity()).updateUnreadLabel();
								}
							};
							builder.setTitle("");
							builder.setMessage("您已不在该病例讨论组中或者该讨论组已被解散！");
							builder.setNegativeButton("取消", listener);
							builder.setPositiveButton("确定", listener);
							builder.create().show();
							return;
						}else {
							intent.putExtra(QjConstant.EXTRA_CHAT_TYPE, QjConstant.CHATTYPE_GROUP);
						}
					}
					// it's single chat
					intent.putExtra(QjConstant.EXTRA_USER_ID, username);
					startActivity(intent);
				}
			}
		});

	}

	@Override
	protected void initConversationList(List<EMConversation> sortList) {
		super.initConversationList(sortList);
		if (noData()) {
			no_data_view.setVisibility(View.VISIBLE);
			errorItemContainer.setVisibility(View.VISIBLE);
			conversationListView.setVisibility(View.GONE);
			errormsg_layout.setVisibility(View.GONE);
		} else {
			no_data_view.setVisibility(View.GONE);
			errorItemContainer.setVisibility(View.GONE);
			conversationListView.setVisibility(View.VISIBLE);
		}
	}

	private boolean noData() {
		return conversationList == null || conversationList.size() == 0;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		getActivity().getMenuInflater().inflate(R.menu.em_delete_message, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		boolean deleteMessage = false;
		if (item.getItemId() == R.id.delete_message) {
			deleteMessage = true;
		} else if (item.getItemId() == R.id.delete_conversation) {
			deleteMessage = false;
		}
		EMConversation tobeDeleteCons = conversationListView
				.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
		if (tobeDeleteCons == null) {
			return true;
		}
		try {
			// 删除此会话
			EMClient.getInstance().chatManager().deleteConversation(tobeDeleteCons.getUserName(), deleteMessage);
			InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
			inviteMessgeDao.deleteMessage(tobeDeleteCons.getUserName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		refresh();

		// 更新消息未读数
		((MainActivity) getActivity()).updateUnreadLabel();
		return true;
	}

}
