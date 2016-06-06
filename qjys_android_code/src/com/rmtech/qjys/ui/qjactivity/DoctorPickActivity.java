package com.rmtech.qjys.ui.qjactivity;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import org.greenrobot.eventbus.EventBus;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
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
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.adapter.EaseContactAdapter;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.widget.EaseSidebar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rmtech.qjys.QjConstant;
import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.R;
import com.rmtech.qjys.callback.BaseModelCallback;
import com.rmtech.qjys.callback.QjHttpCallback;
import com.rmtech.qjys.callback.QjHttpCallbackNoParse;
import com.rmtech.qjys.event.CaseEvent;
import com.rmtech.qjys.model.CaseInfo;
import com.rmtech.qjys.model.DoctorInfo;
import com.rmtech.qjys.model.gson.MBase;
import com.rmtech.qjys.model.gson.MDoctorList;
import com.rmtech.qjys.model.gson.MGroupData;
import com.rmtech.qjys.ui.MainActivity;
import com.rmtech.qjys.ui.view.CustomSimpleDialog;
import com.rmtech.qjys.ui.view.CustomSimpleDialog.Builder;
import com.rmtech.qjys.utils.DoctorListManager;
import com.rmtech.qjys.utils.DoctorListManager.OnGetDoctorInfoCallback;
import com.rmtech.qjys.utils.GroupAndCaseListManager;

public class DoctorPickActivity extends CaseWithIdActivity {
	private ListView listView;
	/** 是否为一个新建的群组 */
	protected boolean isCreatingNewGroup;
	/** 是否为单选 */
	private boolean isSignleChecked;
	private PickContactAdapter contactAdapter;
	/** group中一开始就有的成员 */
	private List<DoctorInfo> exitingMembers;
	final List<EaseUser> alluserList = new ArrayList<EaseUser>();
	private ArrayList<DoctorInfo> selectedDoctorList;

	// public static final int TYPE_ADD_MEMBERS = 0;
	// public static final int TYPE_NEW_CASE = 1;
	// public static final int TYPE_DELETE_MEMBERS = 2;
	// public static final int TYPE_CHANGE_DOCTOR = 3;

	public int type = QjConstant.REQUEST_CODE_ADD_DOCTORS;

	private void onActionFinish() {
		if (type == QjConstant.REQUEST_CODE_DELETE_DOCTORS) {
			Toast.makeText(getApplicationContext(), "删除失败！", 1).show();

		} else {
			Toast.makeText(getApplicationContext(), "添加失败！", 1).show();
		}
	}

	private void loadData() {

		DoctorListManager.getInstance().getDoctorList(true, new QjHttpCallbackNoParse<MDoctorList>() {

			@Override
			public void onError(Call call, Exception e) {
				Toast.makeText(getApplicationContext(), "服务器拉取医生列表失败！", 1).show();
			}

			@Override
			public void onResponseSucces(boolean iscache, MDoctorList response) {
				if (response.ret == 0 && response.data != null && !response.data.isEmpty()) {
					if (type == QjConstant.REQUEST_CODE_DELETE_DOCTORS || 
							type == QjConstant.REQUEST_CODE_CHANGE_DOCTOR) {
						ArrayList<DoctorInfo> exitingMembers2 = getIntent().getParcelableArrayListExtra(
								"selectedDoctorList");

						for (DoctorInfo info : exitingMembers2) {

							EaseUser user = new EaseUser(info.id);
							EaseCommonUtils.setUserInitialLetter(user);
							user.setAvatar(info.head);
							user.setNick(info.getDisplayName());
							alluserList.add(user);
						}

						contactAdapter = new PickContactAdapter(DoctorPickActivity.this,
								R.layout.em_row_contact_with_checkbox, alluserList);
						listView.setAdapter(contactAdapter);
					} else if (type == QjConstant.REQUEST_CODE_ADD_DOCTORS) {
						DoctorListManager.initDoctorList(alluserList, response.data);
						initAdapter();
					} else {
						
						DoctorListManager.initDoctorList(alluserList, response.data);
						initAdapter();
						if (selectedDoctorList != null) {
							contactAdapter.updateCheckedList(selectedDoctorList);
							contactAdapter.notifyDataSetChanged();
						}
					}

				} else {
					Toast.makeText(getApplicationContext(), "没有好友！", 1).show();
				}
			}
		});
	}

	public static void show(Activity activity, CaseInfo caseinfo, List<DoctorInfo> selectedDoctorList, int type) {
		Intent intent = new Intent(activity, DoctorPickActivity.class);//
		setCaseInfo(intent, caseinfo);
		setCaseId(intent, caseinfo.id);
		intent.putExtra("type", type);
		if (selectedDoctorList != null) {
			intent.putParcelableArrayListExtra("selectedDoctorList", new ArrayList<DoctorInfo>(selectedDoctorList));
		}
		activity.startActivityForResult(intent, type);
	}
	public static void show(Activity activity, String caseinfoid, List<DoctorInfo> selectedDoctorList, int type) {
		Intent intent = new Intent(activity, DoctorPickActivity.class);//
		setCaseInfo(intent, GroupAndCaseListManager.getInstance().getCaseInfoByCaseId(caseinfoid));
		setCaseId(intent, caseinfoid);
		intent.putExtra("type", type);
		if (selectedDoctorList != null) {
			intent.putParcelableArrayListExtra("selectedDoctorList", new ArrayList<DoctorInfo>(selectedDoctorList));
		}
		activity.startActivityForResult(intent, type);
	}

	// public static void show(Activity activity, String caseid,
	// List<DoctorInfo> selectedDoctorList) {
	// Intent intent = new Intent(activity, DoctorPickActivity.class);//
	// setCaseId(intent, caseid);
	// intent.putExtra("type", TYPE_ADD_MEMBERS);
	// if (selectedDoctorList != null) {
	// intent.putParcelableArrayListExtra("selectedDoctorList", new
	// ArrayList<DoctorInfo>(selectedDoctorList));
	// }
	// activity.startActivityForResult(intent,
	// QjConstant.REQUEST_CODE_ADD_DOCTORS);
	// }

	@Override
	protected boolean showTitleBar() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.em_activity_group_pick_contacts);
		type = getIntent().getIntExtra("type", QjConstant.REQUEST_CODE_ADD_DOCTORS);
		// if (exitingMembers == null) {
		// exitingMembers = new ArrayList<DoctorInfo>();
		// }
		if(type==QjConstant.REQUEST_CODE_ADD_DOCTORS){
			setTitle("添加讨论组成员");
			setLeftTitle("取消");
			setBackImageGone();
		}else if(type==QjConstant.REQUEST_CODE_DELETE_DOCTORS) {
			setTitle("删除讨论组成员");
			setLeftTitle("返回");
		}else if(type==QjConstant.REQUEST_CODE_CHANGE_DOCTOR) {
			setTitle("管理权限移交");
			setLeftTitle("返回");
		}else {
			
		}
		listView = (ListView) findViewById(R.id.list);

		((EaseSidebar) findViewById(R.id.sidebar)).setListView(listView);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
				checkBox.toggle();

			}
		});
		if (type == QjConstant.REQUEST_CODE_CHANGE_DOCTOR) {
			isSignleChecked = true;
			selectedDoctorList = getIntent().getParcelableArrayListExtra("selectedDoctorList");
			setRightTitle("确认", new OnClickListener() {

				@Override
				public void onClick(View v) {

					QjHttp.updateAdmin(caseInfo.id, getMembersString(), new BaseModelCallback() {

						@Override
						public void onResponseSucces(MBase response) {
							ArrayList<DoctorInfo> resultList = getToBeAddMembers();
							CaseEvent event = new CaseEvent(CaseEvent.TYPE_GROUP_CHANGED_ADMIN);

							CaseInfo newCase = GroupAndCaseListManager.getInstance().getCaseInfoByCaseId(caseInfo.id);
							if (newCase != null && !resultList.isEmpty()) {
								if(newCase.participate_doctor == null) {
									newCase.participate_doctor = new ArrayList<DoctorInfo>();
								}
								DoctorInfo newAdmin = resultList.get(0);
								newCase.participate_doctor.add(newCase.admin_doctor);
								newCase.participate_doctor.remove(newAdmin);
								newCase.admin_doctor = resultList.get(0);
							}
							// event.setAddDoctorList(caseInfo.id, resultList);
							event.setCaseInfoId(caseInfo.id);

							EventBus.getDefault().post(event);
							
							Intent intent = new Intent();
							intent.putParcelableArrayListExtra("selectedDoctorList", resultList);

							setResult(RESULT_OK, intent);

							finish();
						}

						@Override
						public void onError(Call call, Exception e) {
							// TODO Auto-generated method stub
							onActionFinish();
						}
					});
				}
			});
		} else if (type == QjConstant.REQUEST_CODE_DELETE_DOCTORS) {

			setRightTitle("删除", new OnClickListener() {

				@Override
				public void onClick(View v) {
					final int length = contactAdapter.isCheckedArray.length;
					String usernames = "";
					for (int i = 0; i < length; i++) {
						if (contactAdapter.isCheckedArray[i]) {
						    usernames = usernames + contactAdapter.getItem(i).getNick() + " ";
					    }
				    }
					CustomSimpleDialog.Builder builder = new Builder(DoctorPickActivity.this);  
			        builder.setTitle("提示");  
			        builder.setMessage("确定要删除讨论组成员 "+ usernames + "吗？");  
			        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							QjHttp.deleteMembers(caseInfo.id, getMembersString(), new BaseModelCallback() {

							@Override
							public void onResponseSucces(MBase response) {
								
								new Thread(new Runnable()  
						        {  
						            @Override  
						            public void run()  
						            {  
						                // TODO Auto-generated method stub  
						            	for (int i = 0; i < contactAdapter.isCheckedArray.length; i++) {
										    String username = contactAdapter.getItem(i).getUsername();
										    if (contactAdapter.isCheckedArray[i]) {
											    try {
												    EMClient.getInstance().groupManager()
														    .removeUserFromGroup(caseInfo.group_id, username);
											    } catch (Exception e) {
												    e.printStackTrace();
											    }
										    }
									    }
						            }  
						        }).start();
								
								ArrayList<DoctorInfo> resultList = getToBeAddMembers();
								CaseEvent event = new CaseEvent(CaseEvent.TYPE_GROUP_CHANGED_DELETE);
								CaseInfo newCase = GroupAndCaseListManager.getInstance().getCaseInfoByCaseId(caseInfo.id);
								if (newCase != null && null != newCase.participate_doctor) {
									newCase.participate_doctor.removeAll(resultList);
								}
								event.setCaseInfoId(caseInfo.id);

								EventBus.getDefault().post(event);
								setResult(RESULT_OK,
										new Intent().putParcelableArrayListExtra("deleteDoctorList", resultList));// ("newmembers",

								finish();
							}

							@Override
							public void onError(Call call, Exception e) {
								// TODO Auto-generated method stub
								onActionFinish();
							}
						});
							dialog.dismiss();
						}
					});  
			        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
			        builder.create().show();

					
				}
			});
		} else if (type == QjConstant.REQUEST_CODE_ADD_DOCTORS) {
			exitingMembers = getIntent().getParcelableArrayListExtra("selectedDoctorList");

			setRightTitle("确定", new OnClickListener() {

				@Override
				public void onClick(View v) {
					Log.d("sssssssssss", "setRightTitle onClick");
					QjHttp.addMembers(caseInfo, getMembersString(), new QjHttpCallback<MGroupData>() {

						@Override
						public void onResponseSucces(MGroupData response) {
							ArrayList<DoctorInfo> resultList = getToBeAddMembers();
							CaseEvent event = new CaseEvent(CaseEvent.TYPE_GROUP_CHANGED_ADD);

							CaseInfo newCase = GroupAndCaseListManager.getInstance().getCaseInfoByCaseId(caseInfo.id);
							if (newCase != null) {
								if (newCase.participate_doctor == null) {
									newCase.participate_doctor = new ArrayList<DoctorInfo>();
								}
								newCase.participate_doctor.addAll(resultList);
								event.setCaseInfoId(caseInfo.id);

								EventBus.getDefault().post(event);
							}
				

							Intent intent = new Intent();
							intent.putParcelableArrayListExtra("selectedDoctorList", resultList);// ("newmembers",
							intent.putExtra("group_id", response.data == null ? "" : response.data.group_id);
							setResult(RESULT_OK, intent);
							finish();
							// } else {
							// onActionFinish();
							// }
						}

						@Override
						public void onError(Call call, Exception e) {
							// TODO Auto-generated method stub
							onActionFinish();
						}

						@Override
						public MGroupData parseNetworkResponse(String str) throws Exception {
							// TODO Auto-generated method stub
							return new Gson().fromJson(str, MGroupData.class);
						}

					});
				}
			});
		} else if(type == QjConstant.REQUEST_CODE_AT) {
			isSignleChecked = true;
			setRightTitle("确定", new OnClickListener() {

				@Override
				public void onClick(View v) {
					ArrayList<DoctorInfo> resultList = getToBeAddMembers();
					DoctorInfo selDoc = resultList.get(0);
					Intent intent = new Intent();
					if(selDoc != null) {
						intent.putExtra("DoctorInfo", (Parcelable)selDoc);
					}
					setResult(RESULT_OK, intent);
					finish();
				}
			});
			
		} else {
			selectedDoctorList = getIntent().getParcelableArrayListExtra("selectedDoctorList");

			setRightTitle("确定", new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.putParcelableArrayListExtra("selectedDoctorList", getToBeAddMembers());// ("newmembers",
					setResult(RESULT_OK, intent);
					finish();
				}
			});
		}
		loadData();

		// String groupName = getIntent().getStringExtra("groupName");
		// if (groupId == null) {// 创建群组
		// isCreatingNewGroup = true;
		// } else {
		// // 获取此群组的成员列表
		// EMGroup group =
		// EMClient.getInstance().groupManager().getGroup(groupId);
		// exitingMembers = group.getMembers();
		// }
		// if (exitingMembers == null)
		// exitingMembers = new ArrayList<String>();
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

	}

	protected void initAdapter() {
		// TODO Auto-generated method stub
		contactAdapter = new PickContactAdapter(this, R.layout.em_row_contact_with_checkbox, alluserList);
		listView.setAdapter(contactAdapter);
	}

	// /**
	// * 确认选择的members
	// *
	// * @param v
	// */
	// public void save(View v) {
	// // setResult(RESULT_OK, new Intent().putExtra("newmembers",
	// // getToBeAddMembers().toArray(new String[0])));
	// // getToBeAddMembers().toArray(new
	// setResult(RESULT_OK, new
	// Intent().putParcelableArrayListExtra("selectedDoctorList",
	// getToBeAddMembers()));// ("newmembers",
	// // String[0])));
	// finish();
	// }

	/**
	 * 获取要被添加的成员
	 * 
	 * @return
	 */
	private ArrayList<DoctorInfo> getToBeAddMembers() {
		final ArrayList<DoctorInfo> members = new ArrayList<DoctorInfo>();
		if(contactAdapter.isCheckedArray != null) {
		int length = contactAdapter.isCheckedArray.length;
		for (int i = 0; i < length; i++) {
			String username = contactAdapter.getItem(i).getUsername();
			if (contactAdapter.isCheckedArray[i] && !isContainsInExitingMembers(username)) {
				DoctorListManager.getInstance().getDoctorInfoByHXid(username, new OnGetDoctorInfoCallback() {

					@Override
					public void onGet(DoctorInfo info) {
						members.add(info);
					}
				});
			}
		}
		}
		return members;
	}

	private boolean isContainsInExitingMembers(String username) {
		if (exitingMembers != null) {
			for (DoctorInfo info : exitingMembers) {
				if (TextUtils.equals(info.id, username)) {
					return true;
				}
			}
		}
		return false;
	}

	private String getMembersString() {
		ArrayList<DoctorInfo> list = getToBeAddMembers();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i).id);
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
		private List<EaseUser> users = new ArrayList<EaseUser>();

		public PickContactAdapter(Context context, int resource, List<EaseUser> users) {
			super(context, resource, users);
			this.users = users;
			isCheckedArray = new boolean[users.size()];
		}

		public void updateCheckedList(ArrayList<DoctorInfo> selectedDoctorList) {
			for (int i = 0; i < getCount(); i++) {
				EaseUser easeUser = getItem(i);
				for (DoctorInfo info : selectedDoctorList) {
					if (TextUtils.equals(info.id, easeUser.getUsername())) {
						isCheckedArray[i] = true;
						break;
					}
				}
			}
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
			// if (position > 0) {
			final String username = getItem(position).getUsername();
			// 选择框checkbox
			final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
			ImageView avatarView = (ImageView) view.findViewById(R.id.avatar);
			ImageLoader.getInstance().displayImage(users.get(position).doctorInfo.head, avatarView,
					QjConstant.optionsHead);
			TextView nameView = (TextView) view.findViewById(R.id.name);

			if (checkBox != null) {
				if (isContainsInExitingMembers(username)) {
					checkBox.setButtonDrawable(R.drawable.em_checkbox_bg_gray_selector);
				} else {
					checkBox.setButtonDrawable(R.drawable.em_checkbox_bg_selector);
				}
				// checkBox.setOnCheckedChangeListener(null);

				checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						// 群组中原来的成员一直设为选中状态
						if (isContainsInExitingMembers(username)) {
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

				if (isContainsInExitingMembers(username)) {
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
