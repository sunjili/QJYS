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
package com.rmtech.qjys.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMConversation.EMConversationType;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.widget.EaseAlertDialog;
import com.hyphenate.easeui.widget.EaseAlertDialog.AlertDialogUser;
import com.hyphenate.easeui.widget.EaseExpandGridView;
import com.hyphenate.easeui.widget.EaseSwitchButton;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rmtech.qjys.QjConstant;
import com.rmtech.qjys.R;
import com.rmtech.qjys.model.CaseInfo;
import com.rmtech.qjys.model.DoctorInfo;
import com.rmtech.qjys.model.UserContext;
import com.rmtech.qjys.ui.fragment.ChatFragment;
import com.rmtech.qjys.ui.qjactivity.DoctorPickActivity;
import com.rmtech.qjys.ui.qjactivity.UserInfoActivity;
import com.rmtech.qjys.utils.DoctorListManager;
import com.rmtech.qjys.utils.DoctorListManager.OnGetDoctorInfoCallback;
import com.rmtech.qjys.utils.GroupAndCaseListManager;
import com.sjl.lib.multi_image_selector.view.SquaredImageView;

public class GroupDetailsActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = "GroupDetailsActivity";
	private static final int REQUEST_CODE_ADD_USER = 0;
	private static final int REQUEST_CODE_EXIT = 1;
	private static final int REQUEST_CODE_EXIT_DELETE = 2;
	private static final int REQUEST_CODE_EDIT_GROUPNAME = 5;

	private EaseExpandGridView userGridview;
	private String groupId;
	// private ProgressBar loadingPB;
	private Button exitBtn;
	private Button deleteBtn;
	private EMGroup group;
	private GridAdapter adapter;
	private ProgressDialog progressDialog;

	private RelativeLayout rl_switch_block_groupmsg;

	public static GroupDetailsActivity instance;

	String st = "";
	// 清空所有聊天记录
	private RelativeLayout clearAllHistory;
	// private RelativeLayout blacklistLayout;
	// private RelativeLayout changeGroupNameLayout;
	// private RelativeLayout idLayout;
	// private TextView idText;
	private EaseSwitchButton switchButton;
	private GroupChangeListener groupChangeListener;
	private CaseInfo caseInfo;
	private View change_admin_view;
	private TextView name_tv;
	private TextView nike_tv;
	private int requestType;
	private SquaredImageView avatar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		caseInfo = getIntent().getParcelableExtra("caseInfo");
		requestType = getIntent().getIntExtra("type", 0);// ("caseInfo");

		// 获取传过来的groupid
		groupId = caseInfo.group_id;// getIntent().getStringExtra("groupId");
		group = EMClient.getInstance().groupManager().getGroup(groupId);
		// we are not supposed to show the group if we don't find the group
		
		if (caseInfo == null) {
			finish();
			return;
		}
		setContentView(R.layout.em_activity_group_details);
		instance = this;
		st = getResources().getString(R.string.people);
		clearAllHistory = (RelativeLayout) findViewById(R.id.clear_all_history);
		userGridview = (EaseExpandGridView) findViewById(R.id.gridview);
		// loadingPB = (ProgressBar) findViewById(R.id.progressBar);
		exitBtn = (Button) findViewById(R.id.btn_exit_grp);
		deleteBtn = (Button) findViewById(R.id.btn_exitdel_grp);
		change_admin_view = findViewById(R.id.change_admin_view);
		change_admin_view.setOnClickListener(this);
		avatar = (SquaredImageView) findViewById(R.id.avatar);
		name_tv = (TextView) findViewById(R.id.name_tv);
		nike_tv = (TextView) findViewById(R.id.nike_tv);
		rl_switch_block_groupmsg = (RelativeLayout) findViewById(R.id.rl_switch_block_groupmsg);
		switchButton = (EaseSwitchButton) findViewById(R.id.switch_btn);

		rl_switch_block_groupmsg.setOnClickListener(this);
		
		// idText.setText(groupId);
		if (requestType == QjConstant.REQUEST_CODE_NEW_CASE_DOCTOR) {
			exitBtn.setVisibility(View.GONE);
			deleteBtn.setVisibility(View.GONE);
			clearAllHistory.setVisibility(View.GONE);
			rl_switch_block_groupmsg.setVisibility(View.GONE);
			change_admin_view.setVisibility(View.GONE);
			findViewById(R.id.group_setting_view).setVisibility(View.GONE);
		} else if (!isOwner()) {
			exitBtn.setVisibility(View.VISIBLE);
			deleteBtn.setVisibility(View.GONE);
			change_admin_view.setVisibility(View.GONE);
			// blacklistLayout.setVisibility(View.GONE);
			// changeGroupNameLayout.setVisibility(View.GONE);
		} else {
			exitBtn.setVisibility(View.GONE);
			deleteBtn.setVisibility(View.VISIBLE);
		}
		groupChangeListener = new GroupChangeListener();
		EMClient.getInstance().groupManager().addGroupChangeListener(groupChangeListener);

		// ((TextView)
		// findViewById(R.id.group_name)).setText(group.getGroupName() + "(" +
		// group.getAffiliationsCount() + st);
		if (requestType == QjConstant.REQUEST_CODE_NEW_CASE_DOCTOR) {
			((TextView) findViewById(R.id.group_name)).setText("创建" + caseInfo.name + "病例讨论组");

		} else {
			((TextView) findViewById(R.id.group_name)).setText(caseInfo.name);
		}

		

		// 保证每次进详情看到的都是最新的group
		// updateGroup();

		// 设置OnTouchListener
		userGridview.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (adapter.isInDeleteMode) {
						adapter.isInDeleteMode = false;
						adapter.notifyDataSetChanged();
						return true;
					}
					break;
				default:
					break;
				}
				return false;
			}
		});

		clearAllHistory.setOnClickListener(this);
		// blacklistLayout.setOnClickListener(this);
		// changeGroupNameLayout.setOnClickListener(this);
	}

	
	@Override
	protected void onResume() {
		super.onResume();
		if(caseInfo != null) {
			CaseInfo newCaseInfo = GroupAndCaseListManager.getInstance().getCaseInfoByCaseId(caseInfo.id);
			if(newCaseInfo != null) {
				caseInfo = newCaseInfo;
				
			}
		}
		if(caseInfo.admin_doctor != null) {
			DoctorListManager.getInstance().getDoctorInfoByHXid(caseInfo.admin_doctor.id, new OnGetDoctorInfoCallback() {
				
				@Override
				public void onGet(DoctorInfo info) {
					if(info != null) {
						caseInfo.admin_doctor = info;
					}
					name_tv.setText(caseInfo.admin_doctor.name);
					nike_tv.setText("昵称：" + caseInfo.admin_doctor.name);
					ImageLoader.getInstance().displayImage(caseInfo.admin_doctor.head, avatar,
							QjConstant.optionsHead);
					
				}
			});
		}
		
		
		List<DoctorInfo> participateDoctors = caseInfo.participate_doctor;
		adapter = new GridAdapter(this, R.layout.em_grid, participateDoctors);
		userGridview.setAdapter(adapter);
	}


	@Override
	public void finish() {
		Intent intent = new Intent();
		intent.putExtra("caseInfo", (Parcelable) caseInfo);
		setResult(RESULT_OK, intent);
		super.finish();
	}

	private boolean isOwner() {
		if (caseInfo == null || caseInfo.admin_doctor == null) {
			return false;
		}
		return TextUtils.equals(UserContext.getInstance().getUserId(), caseInfo.admin_doctor.id);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		String st1 = getResources().getString(R.string.being_added);
		String st2 = getResources().getString(R.string.is_quit_the_group_chat);
		String st3 = getResources().getString(R.string.chatting_is_dissolution);
		String st4 = getResources().getString(R.string.are_empty_group_of_news);
		String st5 = getResources().getString(R.string.is_modify_the_group_name);
		final String st6 = getResources().getString(R.string.Modify_the_group_name_successful);
		final String st7 = getResources().getString(R.string.change_the_group_name_failed_please);

		if (resultCode == RESULT_OK) {
			if (progressDialog == null) {
				progressDialog = new ProgressDialog(GroupDetailsActivity.this);
				progressDialog.setMessage(st1);
				progressDialog.setCanceledOnTouchOutside(false);
			}
			switch (requestCode) {
			case REQUEST_CODE_ADD_USER: {// 添加群成员
//				final String[] newmembers = data.getStringArrayExtra("newmembers");
//				progressDialog.setMessage(st1);
//				progressDialog.show();
//				addMembersToGroup(newmembers);
				break;
			}
			case QjConstant.REQUEST_CODE_ADD_DOCTORS: {// 添加群成员
				ArrayList<DoctorInfo> currentDoctorList = data.getParcelableArrayListExtra("selectedDoctorList");
				groupId = data.getStringExtra("group_id");
//				if (caseInfo != null) {
//					if (caseInfo.participate_doctor == null) {
//						caseInfo.participate_doctor = new ArrayList<DoctorInfo>();
//					}
//					caseInfo.participate_doctor.addAll(currentDoctorList);
//					GroupAndCaseListManager.getInstance().setIsChanged(true);
//				}
//				progressDialog.setMessage(st1);
//				progressDialog.show();
//
//				String[] newmembers = new String[currentDoctorList.size()];
//				for (int i = 0; i < currentDoctorList.size(); i++) {// DoctorInfo
//																	// info :
//																	// currentDoctorList)
//																	// {
//					newmembers[i] = currentDoctorList.get(i).id;
//				}
//				addMembersToGroup(newmembers);
//				refreshMembers();
				break;
			}
			case QjConstant.REQUEST_CODE_DELETE_DOCTORS:// 删除群成员
//				ArrayList<DoctorInfo> deleteDoctorList = data.getParcelableArrayListExtra("deleteDoctorList");
				// progressDialog.setMessage(st1);
				// progressDialog.show();

				// String[] newmembers = new String[currentDoctorList.size()];
				// for(int i = 0; i < currentDoctorList.size();i++){//DoctorInfo
				// info : currentDoctorList) {
				// newmembers[i] = currentDoctorList.get(i).id;
				// }
				// addMembersToGroup(newmembers);
//				adapter.deleteMembers(deleteDoctorList);
//				caseInfo.participate_doctor.removeAll(deleteDoctorList);
//				adapter.notifyDataSetChanged();
//				 refreshMembers();

				break;
			case REQUEST_CODE_EXIT: // 退出群
				progressDialog.setMessage(st2);
				progressDialog.show();
				exitGrop();
				break;
			case REQUEST_CODE_EXIT_DELETE: // 解散群
				progressDialog.setMessage(st3);
				progressDialog.show();
				deleteGrop();
				break;

			case REQUEST_CODE_EDIT_GROUPNAME: // 修改群名称
				final String returnData = data.getStringExtra("data");
				if (!TextUtils.isEmpty(returnData)) {
					progressDialog.setMessage(st5);
					progressDialog.show();

					new Thread(new Runnable() {
						public void run() {
							try {
								EMClient.getInstance().groupManager().changeGroupName(groupId, returnData);
								runOnUiThread(new Runnable() {
									public void run() {
										((TextView) findViewById(R.id.group_name)).setText(returnData + "("
												+ caseInfo.getGroupCount() + st);
										progressDialog.dismiss();
										Toast.makeText(getApplicationContext(), st6, 0).show();
									}
								});

							} catch (HyphenateException e) {
								e.printStackTrace();
								runOnUiThread(new Runnable() {
									public void run() {
										progressDialog.dismiss();
										Toast.makeText(getApplicationContext(), st7, 0).show();
									}
								});
							}
						}
					}).start();
				}
				break;
			default:
				break;
			}
		}
	}

	protected void addUserToBlackList(final String username) {
		final ProgressDialog pd = new ProgressDialog(this);
		pd.setCanceledOnTouchOutside(false);
		pd.setMessage(getString(R.string.Are_moving_to_blacklist));
		pd.show();
		new Thread(new Runnable() {
			public void run() {
				try {
					EMClient.getInstance().groupManager().blockUser(groupId, username);
					runOnUiThread(new Runnable() {
						public void run() {
							refreshMembers();
							pd.dismiss();
							Toast.makeText(getApplicationContext(), R.string.Move_into_blacklist_success, 0).show();
						}
					});
				} catch (HyphenateException e) {
					runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							Toast.makeText(getApplicationContext(), R.string.failed_to_move_into, 0).show();
						}
					});
				}
			}
		}).start();
	}

	private void refreshMembers() {
		adapter.clear();
		List<DoctorInfo> members = new ArrayList<DoctorInfo>();
		// members.add(caseInfo.admin_doctor);
		members.addAll(caseInfo.participate_doctor);
		adapter.addAll(members);

		adapter.notifyDataSetChanged();
	}

	/**
	 * 点击退出群组按钮
	 * 
	 * @param view
	 */
	public void exitGroup(View view) {
		startActivityForResult(new Intent(this, ExitGroupDialog.class), REQUEST_CODE_EXIT);

	}

	/**
	 * 点击解散群组按钮
	 * 
	 * @param view
	 */
	public void exitDeleteGroup(View view) {
		startActivityForResult(new Intent(this, ExitGroupDialog.class).putExtra("deleteToast",
				getString(R.string.dissolution_group_hint)), REQUEST_CODE_EXIT_DELETE);

	}

	/**
	 * 清空群聊天记录
	 */
	private void clearGroupHistory() {

		EMConversation conversation = EMClient.getInstance().chatManager()
				.getConversation(caseInfo.group_id, EMConversationType.GroupChat);
		if (conversation != null) {
			conversation.clearAllMessages();
		}
		Toast.makeText(this, R.string.messages_are_empty, 0).show();
	}

	/**
	 * 退出群组
	 * 
	 * @param groupId
	 */
	private void exitGrop() {
		String st1 = getResources().getString(R.string.Exit_the_group_chat_failure);
		new Thread(new Runnable() {
			public void run() {
				try {
					EMClient.getInstance().groupManager().leaveGroup(groupId);
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							setResult(RESULT_OK);
							finish();
							if (ChatActivity.activityInstance != null)
								ChatActivity.activityInstance.finish();
						}
					});
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							Toast.makeText(
									getApplicationContext(),
									getResources().getString(R.string.Exit_the_group_chat_failure) + " "
											+ e.getMessage(), 1).show();
						}
					});
				}
			}
		}).start();
	}

	/**
	 * 解散群组
	 * 
	 * @param groupId
	 */
	private void deleteGrop() {
		final String st5 = getResources().getString(R.string.Dissolve_group_chat_tofail);
		new Thread(new Runnable() {
			public void run() {
				try {
					EMClient.getInstance().groupManager().destroyGroup(groupId);
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							setResult(RESULT_OK);
							finish();
							if (ChatActivity.activityInstance != null)
								ChatActivity.activityInstance.finish();
						}
					});
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							Toast.makeText(getApplicationContext(), st5 + e.getMessage(), 1).show();
						}
					});
				}
			}
		}).start();
	}

	/**
	 * 增加群成员
	 * 
	 * @param newmembers
	 */
	private void addMembersToGroup(final String[] newmembers) {
		final String st6 = getResources().getString(R.string.Add_group_members_fail);
		new Thread(new Runnable() {

			public void run() {
				try {
					// 创建者调用add方法
					if (isOwner()) {
						try {
							EMClient.getInstance().groupManager().addUsersToGroup(groupId, newmembers);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						// 一般成员调用invite方法
						try {
							EMClient.getInstance().groupManager().inviteUser(groupId, newmembers, null);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					runOnUiThread(new Runnable() {
						public void run() {
							refreshMembers();
							// if (group == null) {
							// QjHttp.getGroupinfo(true, group_ids,
							// callback)
							((TextView) findViewById(R.id.group_name)).setText(caseInfo.name + "("
									+ caseInfo.getGroupCount() + st);
							// } else {
							// ((TextView)
							// findViewById(R.id.group_name)).setText(group.getGroupName()
							// + "("
							// + group.getAffiliationsCount() + st);
							// }
							progressDialog.dismiss();
						}
					});
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							Toast.makeText(getApplicationContext(), st6 + e.getMessage(), 1).show();
						}
					});
				}
			}
		}).start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_switch_block_groupmsg: // 屏蔽或取消屏蔽群组
			toggleBlockGroup();
			break;

		case R.id.clear_all_history: // 清空聊天记录
			String st9 = getResources().getString(R.string.sure_to_empty_this);
			new EaseAlertDialog(GroupDetailsActivity.this, null, st9, null, new AlertDialogUser() {

				@Override
				public void onResult(boolean confirmed, Bundle bundle) {
					if (confirmed) {
						clearGroupHistory();
					}
				}
			}, true).show();

			break;

		case R.id.rl_blacklist: // 黑名单列表
			startActivity(new Intent(GroupDetailsActivity.this, GroupBlacklistActivity.class).putExtra("groupId",
					groupId));
			break;

		// case R.id.rl_change_group_name:
		// startActivityForResult(new Intent(this,
		// EditActivity.class).putExtra("data", group.getGroupName()),
		// REQUEST_CODE_EDIT_GROUPNAME);
		// break;
		case R.id.change_admin_view:
			ArrayList<DoctorInfo> list = new ArrayList<>();
			list.add(caseInfo.admin_doctor);
			DoctorPickActivity.show(GroupDetailsActivity.this, caseInfo, list, QjConstant.REQUEST_CODE_CHANGE_DOCTOR);

			break;
		default:
			break;
		}

	}

	private void toggleBlockGroup() {
		if (switchButton.isSwitchOpen()) {
			EMLog.d(TAG, "change to unblock group msg");
			if (progressDialog == null) {
				progressDialog = new ProgressDialog(GroupDetailsActivity.this);
				progressDialog.setCanceledOnTouchOutside(false);
			}
			progressDialog.setMessage(getString(R.string.Is_unblock));
			progressDialog.show();
			new Thread(new Runnable() {
				public void run() {
					try {
						EMClient.getInstance().groupManager().unblockGroupMessage(groupId);
						runOnUiThread(new Runnable() {
							public void run() {
								switchButton.closeSwitch();
								progressDialog.dismiss();
							}
						});
					} catch (Exception e) {
						e.printStackTrace();
						runOnUiThread(new Runnable() {
							public void run() {
								progressDialog.dismiss();
								Toast.makeText(getApplicationContext(), R.string.remove_group_of, 1).show();
							}
						});

					}
				}
			}).start();

		} else {
			String st8 = getResources().getString(R.string.group_is_blocked);
			final String st9 = getResources().getString(R.string.group_of_shielding);
			EMLog.d(TAG, "change to block group msg");
			if (progressDialog == null) {
				progressDialog = new ProgressDialog(GroupDetailsActivity.this);
				progressDialog.setCanceledOnTouchOutside(false);
			}
			progressDialog.setMessage(st8);
			progressDialog.show();
			new Thread(new Runnable() {
				public void run() {
					try {
						EMClient.getInstance().groupManager().blockGroupMessage(groupId);
						runOnUiThread(new Runnable() {
							public void run() {
								switchButton.openSwitch();
								progressDialog.dismiss();
							}
						});
					} catch (Exception e) {
						e.printStackTrace();
						runOnUiThread(new Runnable() {
							public void run() {
								progressDialog.dismiss();
								Toast.makeText(getApplicationContext(), st9, 1).show();
							}
						});
					}

				}
			}).start();
		}
	}

	/**
	 * 群组成员gridadapter
	 * 
	 * @author admin_new
	 * 
	 */
	private class GridAdapter extends ArrayAdapter<DoctorInfo> {

		private int res;
		public boolean isInDeleteMode;
		private Context mContext;
		private List<DoctorInfo> objects;

		public GridAdapter(Context context, int textViewResourceId, List<DoctorInfo> objects) {
			super(context, textViewResourceId, objects);
			 this.objects = objects;
			this.mContext = context;
			res = textViewResourceId;
			isInDeleteMode = false;
		}

		public void deleteMembers(ArrayList<DoctorInfo> deleteDoctorList) {
			if (deleteDoctorList != null) {
				for (DoctorInfo info : deleteDoctorList) {
					this.remove(info);
				}
			}
		}

		@Override
		public View getView(final int position, View convertView, final ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(getContext()).inflate(res, null);
				holder.imageView = (SquaredImageView) convertView.findViewById(R.id.iv_avatar);
				holder.textView = (TextView) convertView.findViewById(R.id.tv_name);
				holder.badgeDeleteView = (ImageView) convertView.findViewById(R.id.badge_delete);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final LinearLayout button = (LinearLayout) convertView.findViewById(R.id.button_avatar);
			// 最后一个item，减人按钮
			if (position == getCount() - 1) {
				holder.textView.setText("");
				// 设置成删除按钮
				holder.imageView.setImageResource(R.drawable.btn_message_subtract);
				// button.setCompoundDrawablesWithIntrinsicBounds(0,
				// R.drawable.smiley_minus_btn, 0, 0);
				// 如果不是创建者或者没有相应权限，不提供加减人按钮
				if (!isOwner()) {
					// if current user is not group admin, hide add/remove btn
					convertView.setVisibility(View.INVISIBLE);
				} else { // 显示删除按钮
					if (isInDeleteMode) {
						// 正处于删除模式下，隐藏删除按钮
						convertView.setVisibility(View.INVISIBLE);
					} else {
						// 正常模式
						convertView.setVisibility(View.VISIBLE);
						convertView.findViewById(R.id.badge_delete).setVisibility(View.INVISIBLE);
					}
					final String st10 = getResources().getString(R.string.The_delete_button_is_clicked);
					button.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// EMLog.d(TAG, st10);
							// isInDeleteMode = true;
							// notifyDataSetChanged();
							DoctorPickActivity.show(GroupDetailsActivity.this, caseInfo, caseInfo.participate_doctor,
									QjConstant.REQUEST_CODE_DELETE_DOCTORS);

						}
					});
				}
			} else if (position == getCount() - 2) { // 添加群组成员按钮
				holder.textView.setText("");
				holder.imageView.setImageResource(R.drawable.btn_message_add);
				// button.setCompoundDrawablesWithIntrinsicBounds(0,
				// R.drawable.smiley_add_btn, 0, 0);
				// 如果不是创建者或者没有相应权限
				if (!isOwner()) {
					// if current user is not group admin, hide add/remove btn
					convertView.setVisibility(View.INVISIBLE);
				} else {
					// 正处于删除模式下,隐藏添加按钮
					if (isInDeleteMode) {
						convertView.setVisibility(View.INVISIBLE);
					} else {
						convertView.setVisibility(View.VISIBLE);
						convertView.findViewById(R.id.badge_delete).setVisibility(View.INVISIBLE);
					}
					final String st11 = getResources().getString(R.string.Add_a_button_was_clicked);
					button.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							EMLog.d(TAG, st11);
							// 进入选人页面
							DoctorPickActivity.show(GroupDetailsActivity.this, caseInfo, caseInfo.participate_doctor,
									QjConstant.REQUEST_CODE_ADD_DOCTORS);
							// startActivityForResult((new
							// Intent(GroupDetailsActivity.this,
							// GroupPickContactsActivity.class).putExtra("groupId",
							// groupId)),
							// REQUEST_CODE_ADD_USER);
						}
					});
				}
			} else { // 普通item，显示群组成员
				final DoctorInfo user = objects.get(position);
				if(user != null) {
					DoctorListManager.getInstance().getDoctorInfoByHXid(user.id, new OnGetDoctorInfoCallback() {
						
						@Override
						public void onGet(DoctorInfo info) {
							if(info != null) {
								holder.textView.setText(info.name);
								ImageLoader.getInstance().displayImage(info.head, holder.imageView,
									QjConstant.optionsHead);
							} else {
								holder.textView.setText(user.name);
								ImageLoader.getInstance().displayImage(user.head, holder.imageView,
										QjConstant.optionsHead);
							}
						}
					});
				}
				convertView.setVisibility(View.VISIBLE);
				button.setVisibility(View.VISIBLE);
				if (isInDeleteMode) {
					// 如果是删除模式下，显示减人图标
					convertView.findViewById(R.id.badge_delete).setVisibility(View.VISIBLE);
				} else {
					convertView.findViewById(R.id.badge_delete).setVisibility(View.INVISIBLE);
				}
				final String st12 = getResources().getString(R.string.not_delete_myself);
				final String st13 = getResources().getString(R.string.Are_removed);
				final String st14 = getResources().getString(R.string.Delete_failed);
				final String st15 = getResources().getString(R.string.confirm_the_members);
				button.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (isInDeleteMode) {
							DoctorPickActivity.show(GroupDetailsActivity.this, caseInfo, caseInfo.participate_doctor,
									QjConstant.REQUEST_CODE_ADD_DOCTORS);
							// 如果是删除自己，return
							// if
							// (EMClient.getInstance().getCurrentUser().equals(username))
							// {
							// new EaseAlertDialog(GroupDetailsActivity.this,
							// st12).show();
							// return;
							// }
							// if
							// (!NetUtils.hasNetwork(getApplicationContext())) {
							// Toast.makeText(getApplicationContext(),
							// getString(R.string.network_unavailable),
							// 0).show();
							// return;
							// }
							// EMLog.d("group", "remove user from group:" +
							// username);
							// deleteMembersFromGroup(username);
						} else {
							if(!user.isMyself()){
								DoctorListManager.getInstance().getDoctorInfoByHXid(user.id, new OnGetDoctorInfoCallback() {
									
									@Override
									public void onGet(DoctorInfo info) {
										// TODO Auto-generated method stub
										UserInfoActivity.show(mContext, info, "group");
									}
								});
							}
						}
					}

				});

				button.setOnLongClickListener(new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						// if(EMClient.getInstance().getCurrentUser().equals(username))
						// return true;
						// if
						// (group.getOwner().equals(EMClient.getInstance().getCurrentUser()))
						// {
						// new EaseAlertDialog(GroupDetailsActivity.this, null,
						// st15, null, new AlertDialogUser() {
						//
						// @Override
						// public void onResult(boolean confirmed, Bundle
						// bundle) {
						// if(confirmed){
						// addUserToBlackList(username);
						// }
						// }
						// }, true).show();
						//
						// }
						return false;
					}
				});
			}
			return convertView;
		}

		@Override
		public int getCount() {
			return super.getCount() + 2;
		}
	}

	/**
	 * 删除群成员
	 * 
	 * @param username
	 */
	protected void deleteMembersFromGroup(final String username) {
		// final ProgressDialog deleteDialog = new
		// ProgressDialog(GroupDetailsActivity.this);
		// deleteDialog.setMessage(st13);
		// deleteDialog.setCanceledOnTouchOutside(false);
		// deleteDialog.show();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// 删除被选中的成员
					EMClient.getInstance().groupManager().removeUserFromGroup(groupId, username);
					// isInDeleteMode = false;
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// deleteDialog.dismiss();
							refreshMembers();
							((TextView) findViewById(R.id.group_name)).setText(caseInfo.name + "("
									+ caseInfo.getGroupCount() + st);
						}
					});
				} catch (final Exception e) {
					// deleteDialog.dismiss();
					// runOnUiThread(new Runnable() {
					// public void run() {
					// Toast.makeText(getApplicationContext(), st14 +
					// e.getMessage(), 1).show();
					// }
					// });
				}

			}
		}).start();
	}

	protected void updateGroup() {
		new Thread(new Runnable() {
			public void run() {
				try {
					EMClient.getInstance().groupManager().getGroupFromServer(groupId);

					runOnUiThread(new Runnable() {
						public void run() {
							((TextView) findViewById(R.id.group_name)).setText(group.getGroupName() + "("
									+ group.getAffiliationsCount() + ")");
							// loadingPB.setVisibility(View.INVISIBLE);
							refreshMembers();
							if (EMClient.getInstance().getCurrentUser().equals(group.getOwner())) {
								// 显示解散按钮
								exitBtn.setVisibility(View.GONE);
								deleteBtn.setVisibility(View.VISIBLE);
							} else {
								// 显示退出按钮
								exitBtn.setVisibility(View.VISIBLE);
								deleteBtn.setVisibility(View.GONE);
							}

							// update block
							EMLog.d(TAG, "group msg is blocked:" + group.isMsgBlocked());
							if (group.isMsgBlocked()) {
								switchButton.openSwitch();
							} else {
								switchButton.closeSwitch();
							}
						}
					});

				} catch (Exception e) {
					// runOnUiThread(new Runnable() {
					// public void run() {
					// loadingPB.setVisibility(View.INVISIBLE);
					// }
					// });
				}
			}
		}).start();
	}

	public void back(View view) {
		setResult(RESULT_OK);
		finish();
	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_OK);
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		instance = null;
	}

	private static class ViewHolder {
		SquaredImageView imageView;
		TextView textView;
		ImageView badgeDeleteView;
	}

	private class GroupChangeListener implements EMGroupChangeListener {

		@Override
		public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onApplicationReceived(String groupId, String groupName, String applyer, String reason) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onApplicationAccept(String groupId, String groupName, String accepter) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onApplicationDeclined(String groupId, String groupName, String decliner, String reason) {

		}

		@Override
		public void onInvitationAccpted(String groupId, String inviter, String reason) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					refreshMembers();
				}

			});

		}

		@Override
		public void onInvitationDeclined(String groupId, String invitee, String reason) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onUserRemoved(String groupId, String groupName) {
			finish();

		}

		@Override
		public void onGroupDestroy(String groupId, String groupName) {
			finish();

		}

		@Override
		public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String inviteMessage) {
			// TODO Auto-generated method stub

		}

	}

	public static void show(Activity activity, CaseInfo caseInfo2) {
		Intent intent = new Intent(activity, GroupDetailsActivity.class);
		intent.putExtra("caseInfo", (Parcelable) caseInfo2);
		activity.startActivityForResult(intent, ChatFragment.REQUEST_CODE_GROUP_DETAIL);
	}

	public static void show(Activity activity, CaseInfo caseInfo2, int type) {
		Intent intent = new Intent(activity, GroupDetailsActivity.class);
		intent.putExtra("caseInfo", (Parcelable) caseInfo2);
		intent.putExtra("type", type);
		activity.startActivityForResult(intent, type);
	}

}
