package com.rmtech.qjys.ui.qjactivity;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hyphenate.easeui.adapter.EaseContactAdapter;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.widget.EaseSidebar;
import com.rmtech.qjys.QjConstant;
import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.R;
import com.rmtech.qjys.callback.BaseModelCallback;
import com.rmtech.qjys.callback.QjHttpCallbackNoParse;
import com.rmtech.qjys.model.gson.MBase;
import com.rmtech.qjys.model.gson.MDoctorList;
import com.rmtech.qjys.utils.DoctorListManager;

public class DoctorPickActivity extends CaseWithIdActivity {
	private ListView listView;
	/** 是否为一个新建的群组 */
	protected boolean isCreatingNewGroup;
	/** 是否为单选 */
	private boolean isSignleChecked;
	private PickContactAdapter contactAdapter;
	/** group中一开始就有的成员 */
	private List<String> exitingMembers;
	final List<EaseUser> alluserList = new ArrayList<EaseUser>();

	private void loadData() {

		DoctorListManager.getInstance().getDoctorList(new QjHttpCallbackNoParse<MDoctorList>() {

			@Override
			public void onError(Call call, Exception e) {
			}

			@Override
			public void onResponseSucces(boolean iscache, MDoctorList response) {
				if (response.ret == 0 && response.data != null && !response.data.isEmpty()) {
					DoctorListManager.initDoctorList(alluserList, response.data);
					initAdapter();

				} else {

				}
			}
		});
	}

	public static void show(Activity activity, String patient_id) {
		Intent intent = new Intent(activity, DoctorPickActivity.class);//
		setCaseId(intent, patient_id);
		activity.startActivityForResult(intent, QjConstant.REQUEST_CODE_ADD_DOCTORS);
	}

	@Override
	protected boolean showTitleBar() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.em_activity_group_pick_contacts);
		setTitle("添加讨论组成员");
		setRightTitle("确定", new OnClickListener() {

			@Override
			public void onClick(View v) {
				QjHttp.addMembers(caseId, getMembersString(), new BaseModelCallback() {

					@Override
					public void onResponseSucces(MBase response) {
						// TODO Auto-generated method stub
						finish();
					}

					@Override
					public void onError(Call call, Exception e) {
						// TODO Auto-generated method stub

					}
				});
			}
		});
		// String groupName = getIntent().getStringExtra("groupName");
		// if (groupId == null) {// 创建群组
		// isCreatingNewGroup = true;
		// } else {
		// // 获取此群组的成员列表
		// EMGroup group =
		// EMClient.getInstance().groupManager().getGroup(groupId);
		// exitingMembers = group.getMembers();
		// }
		if (exitingMembers == null)
			exitingMembers = new ArrayList<String>();
		// 获取好友列表
		// final List<EaseUser> alluserList = new ArrayList<EaseUser>();
		// for (EaseUser user :
		// QjHelper.getInstance().getContactList().values()) {
		// if (!user.getUsername().equals(QjConstant.NEW_FRIENDS_USERNAME) &
		// !user.getUsername().equals(QjConstant.GROUP_USERNAME) &
		// !user.getUsername().equals(QjConstant.CHAT_ROOM) &
		// !user.getUsername().equals(QjConstant.CHAT_ROBOT))
		// alluserList.add(user);
		// }
		// // 对list进行排序
		// Collections.sort(alluserList, new Comparator<EaseUser>() {
		//
		// @Override
		// public int compare(EaseUser lhs, EaseUser rhs) {
		// if(lhs.getInitialLetter().equals(rhs.getInitialLetter())){
		// return lhs.getNick().compareTo(rhs.getNick());
		// }else{
		// if("#".equals(lhs.getInitialLetter())){
		// return 1;
		// }else if("#".equals(rhs.getInitialLetter())){
		// return -1;
		// }
		// return lhs.getInitialLetter().compareTo(rhs.getInitialLetter());
		// }
		//
		// }
		// });

		listView = (ListView) findViewById(R.id.list);

		((EaseSidebar) findViewById(R.id.sidebar)).setListView(listView);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
				checkBox.toggle();

			}
		});
		loadData();
	}

	protected void initAdapter() {
		// TODO Auto-generated method stub
		contactAdapter = new PickContactAdapter(this, R.layout.em_row_contact_with_checkbox, alluserList);
		listView.setAdapter(contactAdapter);
	}

	/**
	 * 确认选择的members
	 * 
	 * @param v
	 */
	public void save(View v) {
		// setResult(RESULT_OK, new Intent().putExtra("newmembers",
		// getToBeAddMembers().toArray(new String[0])));
		setResult(RESULT_OK, new Intent().putStringArrayListExtra("getToBeAddMembers()", getToBeAddMembers()));// ("newmembers",
																												// getToBeAddMembers().toArray(new
																												// String[0])));
		finish();
	}

	/**
	 * 获取要被添加的成员
	 * 
	 * @return
	 */
	private ArrayList<String> getToBeAddMembers() {
		ArrayList<String> members = new ArrayList<String>();
		int length = contactAdapter.isCheckedArray.length;
		for (int i = 0; i < length; i++) {
			String username = contactAdapter.getItem(i).getUsername();
			if (contactAdapter.isCheckedArray[i] && !exitingMembers.contains(username)) {
				members.add(username);
			}
		}

		return members;
	}

	private String getMembersString() {
		ArrayList<String> list = getToBeAddMembers();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i));
			if (i < list.size() - 1) {
				sb.append(",");

			}
		}
		return sb.toString();
	}

	/**
	 * adapter
	 */
	private class PickContactAdapter extends EaseContactAdapter {

		private boolean[] isCheckedArray;

		public PickContactAdapter(Context context, int resource, List<EaseUser> users) {
			super(context, resource, users);
			isCheckedArray = new boolean[users.size()];
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
			// if (position > 0) {
			final String username = getItem(position).getUsername();
			// 选择框checkbox
			final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
			ImageView avatarView = (ImageView) view.findViewById(R.id.avatar);
			TextView nameView = (TextView) view.findViewById(R.id.name);

			if (checkBox != null) {
				if (exitingMembers != null && exitingMembers.contains(username)) {
					checkBox.setButtonDrawable(R.drawable.em_checkbox_bg_gray_selector);
				} else {
					checkBox.setButtonDrawable(R.drawable.em_checkbox_bg_selector);
				}
				// checkBox.setOnCheckedChangeListener(null);

				checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						// 群组中原来的成员一直设为选中状态
						if (exitingMembers.contains(username)) {
							isChecked = true;
							checkBox.setChecked(true);
						}
						isCheckedArray[position] = isChecked;
						// 如果是单选模式
						if (isSignleChecked && isChecked) {
							for (int i = 0; i < isCheckedArray.length; i++) {
								if (i != position) {
									isCheckedArray[i] = false;
								}
							}
							contactAdapter.notifyDataSetChanged();
						}

					}
				});
				// 群组中原来的成员一直设为选中状态
				if (exitingMembers.contains(username)) {
					checkBox.setChecked(true);
					isCheckedArray[position] = true;
				} else {
					checkBox.setChecked(isCheckedArray[position]);
				}
			}
			// }
			return view;
		}
	}

	public void back(View view) {
		finish();
	}

}
