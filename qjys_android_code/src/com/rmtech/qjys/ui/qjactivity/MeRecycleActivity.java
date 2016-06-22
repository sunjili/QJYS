package com.rmtech.qjys.ui.qjactivity;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.easeui.widget.EaseTitleBar;
import com.nostra13.universalimageloader.utils.L;
import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.R;
import com.rmtech.qjys.callback.BaseModelCallback;
import com.rmtech.qjys.callback.QjHttpCallbackNoParse;
import com.rmtech.qjys.event.CaseEvent;
import com.rmtech.qjys.model.CaseInfo;
import com.rmtech.qjys.model.DoctorInfo;
import com.rmtech.qjys.model.UserContext;
import com.rmtech.qjys.model.gson.MBase;
import com.rmtech.qjys.model.gson.MPatientList;
import com.rmtech.qjys.model.gson.MPatientList.HospitalCaseInfo;
import com.rmtech.qjys.ui.BaseActivity;
import com.rmtech.qjys.ui.fragment.CaseFragment;
import com.rmtech.qjys.ui.view.CustomSimpleDialog;
import com.rmtech.qjys.ui.view.CustomSimpleDialog.Builder;
import com.rmtech.qjys.utils.DoctorListManager;
import com.rmtech.qjys.utils.GroupAndCaseListManager;
import com.sjl.lib.alertview.AlertView;
import com.sjl.lib.http.okhttp.OkHttpUtils;
import com.sjl.lib.pinnedheaderlistview.PinnedHeaderListView;
import com.sjl.lib.pinnedheaderlistview.PinnedHeaderListView.OnItemClickListener;
import com.sjl.lib.pinnedheaderlistview.SectionedBaseAdapter;
import com.sjl.lib.swipemenulistview.SwipeMenu;
import com.sjl.lib.swipemenulistview.SwipeMenuCreator;
import com.sjl.lib.swipemenulistview.SwipeMenuItem;
import com.sjl.lib.swipemenulistview.SwipeMenuListView;
import com.umeng.analytics.MobclickAgent;

/***
 * 病例回收站 页面
 * 
 * @author Administrator
 * 
 */
public class MeRecycleActivity extends BaseActivity {
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_me_recycle);
		setTitle("我的回收站");
		setLeftTitle("我");
		setRightTitle("", null);
		QjHttp.getPatientList("2", false, httpCallback);
		initView();
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, MeRecycleActivity.class);
		context.startActivity(intent);
	}

	private EaseTitleBar title_bar;
	private PtrClassicFrameLayout mPtrFrame;
	private ImageView mImageView;
	private PinnedHeaderListView mListView;
	private CaseSectionedAdapter mAdapter;
	private Button btn_delete;
	private LinearLayout ll_btn_delete;
	private View mNodataView;

	@Subscribe
	public void onEvent(CaseEvent event) {
		// mAdapter.add();
		L.d("onEvent " + event.type);
		QjHttp.getPatientList("2", false, httpCallback);

	}

	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	protected void initView() {
		EventBus.getDefault().register(this);
		title_bar = (EaseTitleBar) findViewById(R.id.title_bar);
		title_bar.setVisibility(View.GONE);
		mImageView = (ImageView) findViewById(R.id.list_view_with_empty_view_fragment_empty_view);
		mImageView.setVisibility(View.GONE);
		mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.list_view_with_empty_view_fragment_ptr_frame);
		ll_btn_delete = (LinearLayout) findViewById(R.id.ll_btn_delete);
		btn_delete = (Button) findViewById(R.id.btn_delete);
		btn_delete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mAdapter.removeAllData();
			}
		});
		
		mListView = (PinnedHeaderListView) findViewById(R.id.pinnedListView);
		mAdapter = new CaseSectionedAdapter();
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onSectionClick(AdapterView<?> adapterView, View view, int section, long id) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, final int section, final int position, long id) {
				// TODO Auto-generated method stub
//				CaseInfo info = mAdapter.getCaseInfoByPos(section, position);
//				if (DoctorListManager.isGroupDeleted(info.group_id) || DoctorListManager.isGroupBeDeleted(info.group_id) ) {
//					CustomSimpleDialog.Builder builder = new Builder(
//							getActivity());
//					builder.setTitle("提示");
//					String str = "群组已解散";
//					if(DoctorListManager.isGroupBeDeleted(info.group_id)) {
//						str = "你已被移除群组";
//					}
//					builder.setMessage(str);
//					
//					builder.setNegativeButton("确定",
//							new DialogInterface.OnClickListener() {
//
//								@Override
//								public void onClick(DialogInterface dialog,
//										int which) {
//									mAdapter.deleteItem(section, position);
//									dialog.dismiss();
//								}
//
//							});
//					builder.create().show();
//					return;
//				}
//				PhotoDataManagerActivity.show(getActivity(), info, null);

			}
		});
		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "open" item
				SwipeMenuItem openItem = new SwipeMenuItem(getActivity());
				// set item background
				openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
				// set item width
				openItem.setWidth(dp2px(100));
				// set item title
				openItem.setTitle("恢复");
				// set item title fontsize
				openItem.setTitleSize(18);
				// set item title font color
				openItem.setTitleColor(Color.WHITE);
				// add to menu
				menu.addMenuItem(openItem);

				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(dp2px(100));
				// set a icon
				deleteItem.setTitle("删除");
				
				deleteItem.setTitleSize(18);
				// set item title font color
				deleteItem.setTitleColor(Color.WHITE);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		// set creator
		mListView.setMenuCreator(creator);

		// step 2. listener item click event
		mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu, int index) { 
				switch (index) {
				case 0:
					mAdapter.restoreData(position);
					break;
				case 1:
					mAdapter.removeData(position);
					break;
				}
				return false;
			}
		});

		// set SwipeListener
		mListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

			@Override
			public void onSwipeStart(int position) {
				// swipe start
			}

			@Override
			public void onSwipeEnd(int position) {
				// swipe end
			}
		});

		// set MenuStateChangeListener
		mListView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
			@Override
			public void onMenuOpen(int position) {
			}

			@Override
			public void onMenuClose(int position) {
			}
		});
		mPtrFrame.setLastUpdateTimeRelateObject(this);
		mPtrFrame.disableWhenHorizontalMove(true);
		mPtrFrame.setPtrHandler(new PtrHandler() {
			@Override
			public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {

				// here check $mListView instead of $content
				return PtrDefaultHandler.checkContentCanBePulledDown(frame, mListView, header);
			}

			@Override
			public void onRefreshBegin(PtrFrameLayout frame) {
			}

		});
		mPtrFrame.addPtrUIHandler(new PtrUIHandler() {

			@Override
			public void onUIReset(PtrFrameLayout frame) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onUIRefreshPrepare(PtrFrameLayout frame) {
				// TODO Auto-generated method stub
				mListView.smoothCloseMenu();
			}

			@Override
			public void onUIRefreshComplete(PtrFrameLayout frame) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onUIRefreshBegin(PtrFrameLayout frame) {
				// TODO Auto-generated method stub
				mListView.smoothCloseMenu();
				updateData();

				Log.d("ssssssss", "onUIRefreshBegin");
			}

			@Override
			public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status,
					PtrIndicator ptrIndicator) {
				// TODO Auto-generated method stub

			}
		});
		// default is false
		mPtrFrame.setPullToRefresh(true);
		mPtrFrame.setPagingTouchSlop(0);
		// titleBar.setRightLayoutClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// AddCaseActivity.show(getActivity());
		// // PhotoDataManagerActivity.show(getActivity());
		//
		// }
		// });

	}

	QjHttpCallbackNoParse<MPatientList> httpCallback = new QjHttpCallbackNoParse<MPatientList>() {

		@Override
		public void onError(Call call, Exception e) {
			// TODO Auto-generated method stub
			mPtrFrame.refreshComplete();
			onDisplayData();
		}

		@Override
		public void onResponseSucces(boolean iscache, MPatientList response) {
			if (response != null && response.hasData()) {
				mAdapter.setData(response);
			}
			onDisplayData();
			mPtrFrame.refreshComplete();

		}
	};

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
	}

	private void updateData() {
		QjHttp.getPatientList("2", false, httpCallback);

		// TODO Auto-generated method stub
		// mListView.postDelayed(new Runnable() {
		//
		// @Override
		// public void run() {
		// displayData();
		// }
		// }, 1000);

	}

	private void displayData() {

		mImageView.setVisibility(View.GONE);

		// mAdapter.getDataList().addAll(data.optJson("data").optJson("list").toArrayList());
		mAdapter.notifyDataSetChanged();
	}

	// @Override
	// protected void setUpView() {
	// titleBar.setTitle("我的病例");
	//
	// }
	

	private void onDisplayData() {
		
		if (mAdapter != null && mAdapter.getCount() > 0) {
			mImageView.setVisibility(View.GONE);
			ll_btn_delete.setVisibility(View.VISIBLE);
		} else {
			mImageView.setVisibility(View.GONE);
			ll_btn_delete.setVisibility(View.GONE);
		}
	}

	public class CaseSectionedAdapter extends SectionedBaseAdapter {

		private List<HospitalCaseInfo> mPatientList;

		// private ArrayList<String> keyPositionList;

		@Override
		public Object getItem(int section, int position) {
			return null;
		}

		public void setData(MPatientList patientList) {
			this.mPatientList = patientList.data.lists;
			// if (keyPositionList == null) {
			// keyPositionList = new ArrayList<String>();
			// } else {
			// keyPositionList.clear();
			// }
			// keyPositionList.addAll(mPatientList.keySet());
			notifyDataSetChanged();
		}
		
		public void restoreData(int position){
			int section = getSectionForPosition(position);
			final int positionInSection = getPositionInSectionForPosition(position);
			final HospitalCaseInfo hosList = mPatientList.get(section);
			if (hosList != null && hosList.patients != null) {
				final CaseInfo caseinfo = hosList.patients
						.get(positionInSection);
				if (caseinfo != null
						&& caseinfo.admin_doctor != null
						&& UserContext.getInstance().isMyself(
								caseinfo.admin_doctor.id)) {
					new AlertView("确定还原？", null, "取消", new String[] { "确定" },
							null, getActivity(), AlertView.Style.Alert,
							new com.sjl.lib.alertview.OnItemClickListener() {

								@Override
								public void onItemClick(Object o, int position) {
									if (position == 0) {
										HashMap<String, String> params = new HashMap<String, String>();
										params.put("patient_id", caseinfo.id);
										params.put("state", 0 + "");
										OkHttpUtils.post(QjHttp.URL_UPDATE_PATIENT, params, new BaseModelCallback() {

											@Override
											public void onError(Call call, Exception e) {
												Toast.makeText(getActivity(), "还原失败", Toast.LENGTH_LONG).show();

											}

											@Override
											public void onResponseSucces(MBase response) {
												Toast.makeText(getActivity(), "还原成功", Toast.LENGTH_LONG).show();
												hosList.patients.remove(positionInSection);
												if (hosList.patients.size() == 0) {
													mPatientList.remove(hosList);
												}
												notifyDataSetChanged();
												onDisplayData();
											}
										});
									}
								}
							}).setCancelable(true).show();
				} 
			} else {
				Toast.makeText(getActivity(), "还原失败", 1).show();
			}
		}
		
		public void removeData(int position) {
			int section = getSectionForPosition(position);
			final int positionInSection = getPositionInSectionForPosition(position);
			final HospitalCaseInfo hosList = mPatientList.get(section);
			if (hosList != null && hosList.patients != null) {
				final CaseInfo caseinfo = hosList.patients
						.get(positionInSection);
				if (caseinfo != null
						&& caseinfo.admin_doctor != null
						&& UserContext.getInstance().isMyself(
								caseinfo.admin_doctor.id)) {
					new AlertView("确定删除？", null, "取消", new String[] { "确定" },
							null, getActivity(), AlertView.Style.Alert,
							new com.sjl.lib.alertview.OnItemClickListener() {

								@Override
								public void onItemClick(Object o, int position) {
									if (position == 0) {
										hosList.patients
												.remove(positionInSection);
										QjHttp.deletePatient("3", caseinfo.id, null);
//										GroupAndCaseListManager.getInstance().deleteGroupInfoInCase(caseinfo.group_id);
//										CaseFragment.deleteGrop(getActivity(),
//												caseinfo.group_id);
										if (hosList.patients.size() == 0) {
											mPatientList.remove(hosList);

										}
										notifyDataSetChanged();
										onDisplayData();

									}

								}
							}).setCancelable(true).show();
				} else {
					Toast.makeText(getActivity(), "没有权限，只有管理员才能删除", 1).show();
				}

			} else {
				Toast.makeText(getActivity(), "删除失败", 1).show();

			}
			Log.d("ssssssssss", "removeData position=" + position);
			Log.d("ssssssssss", "removeData section=" + section);
			Log.d("ssssssssss", "removeData positionInSection="
					+ positionInSection);
		}
		
		public void removeAllData(){
			List<CaseInfo> caseinfos = new ArrayList();
			String ids = "";
			for(int i = 0;i<getSectionCount();i++){
				for(int j=0;j<getCountForSection(i);j++){
					caseinfos.add(getCaseInfoByPos(i, j));
					ids = ids + getCaseInfoByPos(i, j).id + ",";
				}
			}
			ids = ids.substring(0, ids.length()-1);
			Log.e("ids", ids);
	        QjHttp.deletePatient("3", ids, new BaseModelCallback() {
				
				@Override
				public void onResponseSucces(MBase response) {
					// TODO Auto-generated method stub
					Log.e("deletePatient", "onResponseSucces");
					mPatientList.removeAll(mPatientList);
			        notifyDataSetChanged();
			        onDisplayData();
				}
				
				@Override
				public void onError(Call call, Exception e) {
					Log.e("deletePatient", "onError");
				}
			});
		}
		
		private void deleteItem(int section, int position) {
			final HospitalCaseInfo hosList = mPatientList.get(section);

			try {
				if (hosList != null && hosList.patients != null) {
					final CaseInfo caseinfo = hosList.patients
							.get(position);
					QjHttp.deletePatient("3", caseinfo.id, null);
				}
				hosList.patients.remove(position);

				if (hosList.patients.size() == 0) {
					mPatientList.remove(hosList);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			

			notifyDataSetChanged();
			onDisplayData();
		}
		
		@Override
		public long getItemId(int section, int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public int getSectionCount() {
			if (mPatientList == null) {
				return 0;
			}
			return mPatientList.size();
		}

		@Override
		public int getCountForSection(int section) {
			// if(mPatientList != null && mPatientList.get(section)) {
			// Map<String, List<CaseInfo>> = mPatientList.get(section);
			// }
			List<CaseInfo> list = getCaseListBySection(section);
			if (list == null) {
				return 0;
			}

			return list.size();
		}

		public List<CaseInfo> getCaseListBySection(int section) {
			// if (keyPositionList == null) {
			// return null;
			// }
			HospitalCaseInfo key = mPatientList.get(section);
			List<CaseInfo> list = key.patients;
			return list;
		}

		public CaseInfo getCaseInfoByPos(int section, int position) {
			List<CaseInfo> list = getCaseListBySection(section);
			if (list != null) {
				return list.get(position);
			}
			return null;
		}

		@Override
		public View getItemView(int section, int position, View convertView, ViewGroup parent) {
			LinearLayout layout = null;
			if (convertView == null) {
				LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(
						Context.LAYOUT_INFLATER_SERVICE);
				layout = (LinearLayout) inflator.inflate(R.layout.case_list_item, null);
			} else {
				layout = (LinearLayout) convertView;
			}
			ViewHolder viewHolder = (ViewHolder) layout.getTag();
			if (viewHolder == null) {
				viewHolder = new ViewHolder(layout);
			}
			CaseInfo info = getCaseInfoByPos(section, position);
			if (info != null) {
//				viewHolder.nameTv.setText(info.name);
				viewHolder.build(info);
			}
			//
			// ((TextView)
			// layout.findViewById(R.id.textItem)).setText("Section "
			// + section + " Item " + position);
			return layout;
		}

		@Override
		public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
			LinearLayout layout = null;
			if (convertView == null) {
				LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(
						Context.LAYOUT_INFLATER_SERVICE);
				layout = (LinearLayout) inflator.inflate(R.layout.case_list_header_item, null);
			} else {
				layout = (LinearLayout) convertView;
			}
			((TextView) layout.findViewById(R.id.textItem)).setText(mPatientList.get(section).hos_fullname);
			return layout;
		}

	}

	private static class ViewHolder {
		public TextView nameTv;
		public TextView genderTv;
		public TextView timeTv;
		public TextView doctorsTv;
		public TextView statusTv;
		public TextView contentTv;

		public ViewHolder(View container) {
			nameTv = (TextView) container.findViewById(R.id.name_tv);
			genderTv = (TextView) container.findViewById(R.id.gender_tv);
			timeTv = (TextView) container.findViewById(R.id.time_tv);
			doctorsTv = (TextView) container.findViewById(R.id.doctors_tv);
			statusTv = (TextView) container.findViewById(R.id.status_tv);
			contentTv = (TextView) container.findViewById(R.id.content_tv);
		}
		
		public void build(CaseInfo info) {

			nameTv.setText(info.getShowName());
			String genderStr = "";
			if (info.sex == 1) {
				genderStr = "男";// 99
				genderTv.setBackgroundResource(R.drawable.bg_gender_man);
			} else if (info.sex == 2) {
				genderStr = "女";
				genderTv.setBackgroundResource(R.drawable.bg_gender_woman);
			} else {
				genderStr = "未知";
				genderTv.setBackgroundResource(R.drawable.bg_gender_weizhi);
			}
			if (!TextUtils.isEmpty(info.age)
					&& !TextUtils.equals("0", info.age)) {
				genderStr = genderStr + info.age;
			}
			genderTv.setText(genderStr);

			String doctorsStr = "主管医生:" + info.admin_doctor.getDisplayName();
			if (info.participate_doctor != null
					&& !info.participate_doctor.isEmpty()) {
				StringBuilder sb = new StringBuilder();
				for (DoctorInfo doc : info.participate_doctor) {
					sb.append(doc.name + "、");
				}
				sb.append("等");
				if (!TextUtils.isEmpty(sb)) {
					doctorsStr = doctorsStr + " 参与医生:" + sb;
				}
			}
			doctorsTv.setText(doctorsStr);

			if (info.create_time > 0) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				String timeStr = format.format(new Date(
						info.create_time * 1000L));
				timeTv.setText(timeStr);
				timeTv.setVisibility(View.VISIBLE);
			} else {
				timeTv.setVisibility(View.GONE);
			}

			if (!TextUtils.isEmpty(info.treat_state)) {
				statusTv.setVisibility(View.VISIBLE);
				statusTv.setText(info.treat_state);
			} else {
				statusTv.setVisibility(View.GONE);
			}

			String contentStr = null;
			if (!TextUtils.isEmpty(info.department)) {
				contentStr = "[" + info.department + "]";
			} else {
				contentStr = "[未知科室]";
			}

			if (!TextUtils.isEmpty(info.diagnose)) {
				contentStr = contentStr + " 诊断:" + info.diagnose;
			} else {
				contentStr = contentStr + " " + "暂无诊断";
			}
			contentTv.setText(contentStr);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

}
