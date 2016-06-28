package com.rmtech.qjys.ui.qjactivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.R;
import com.rmtech.qjys.adapter.CommonAdapter;
import com.rmtech.qjys.adapter.ViewHolder;
import com.rmtech.qjys.callback.BaseModelCallback;
import com.rmtech.qjys.callback.QjHttpCallbackNoParse;
import com.rmtech.qjys.model.CaseInfo;
import com.rmtech.qjys.model.gson.MBase;
import com.rmtech.qjys.model.gson.MStateList;
import com.rmtech.qjys.model.gson.MStateList.StateInfo;
import com.rmtech.qjys.utils.GroupAndCaseListManager;
import com.sjl.lib.http.okhttp.OkHttpUtils;
import com.umeng.analytics.MobclickAgent;

public class EditCaseStateActivity extends CaseEidtBaseActivity {
	private EditText et_state_add;
	private ListView lv_state;

	private CommonAdapter<StateInfo> mAdapter;
	private Context context;
	List<StateInfo> listems;
	private StateInfo state = new StateInfo();
	private boolean isNew = false;
	private String stateStr = "";

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		context = EditCaseStateActivity.this;
		setContentView(R.layout.qj_case_state);
		setLeftTitle("取消");//244改完了！！！
		setBackImageGone();
		setRightTitle("保存", new OnClickListener() {

			@Override
			public void onClick(View v) {
				stateStr = et_state_add.getText().toString().trim();
				if (mCaseInfo != null) {
					if (TextUtils.equals(stateStr, mCaseInfo.treat_state)) {
						Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_LONG).show();
						Intent data = new Intent();
						data.putExtra("string", stateStr);
						setResult(MeNameActivity.RESULT_OK, data);
						finish();
						return;
					}
					HashMap<String, String> params = new HashMap<String, String>();
					params.put("treat_state", stateStr);
					params.put("patient_id", mCaseInfo.id);
					OkHttpUtils.post(QjHttp.URL_UPDATE_PATIENT, params, new BaseModelCallback() {

						@Override
						public void onError(Call call, Exception e) {
							Toast.makeText(getActivity(), "保存失败", Toast.LENGTH_LONG).show();
						}

						@Override
						public void onResponseSucces(MBase response) {
							Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_LONG).show();

							CaseInfo caseInfo = GroupAndCaseListManager.getInstance().getCaseInfoByCaseId(mCaseInfo.id);
							if (caseInfo != null) {
								caseInfo.treat_state = stateStr;
							}
							Intent data = new Intent();
							data.putExtra("string", stateStr);
							setResult(MeNameActivity.RESULT_OK, data);
							finish();
						}
					});

					return;

				}
				if (!TextUtils.isEmpty(stateStr)) {
					Intent data = new Intent();
					data.putExtra("string", stateStr);
					setResult(MeNameActivity.RESULT_OK, data);
				}
				finish();
			}
		});
		setTitle("设置就诊状态");
		initView();
	}

	private void initView() {
		et_state_add = (EditText) findViewById(R.id.et_state_add);
		setTextWhacher(EditCaseStateActivity.this, et_state_add, 15);
		lv_state = (ListView) findViewById(R.id.lv_state);
		if (mCaseInfo != null) {
			et_state_add.setText(mCaseInfo.treat_state);
		} else {
			String casestate = getIntent().getStringExtra("case_state");
			et_state_add.setText(casestate);
			isNew = true;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadData();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	private void loadData() {
		QjHttp.treateStateList(new QjHttpCallbackNoParse<MStateList>() {

			@Override
			public void onResponseSucces(boolean iscache, MStateList response) {
				listems = new ArrayList<StateInfo>();
				// for (int i = 0; i < userArray.length; i++) {
				// listems.add(userArray[i]);
				// }
				if(response!=null&&response.data!=null){
					listems.addAll(response.data);
				}
				mAdapter = new CommonAdapter<StateInfo>(getApplicationContext(), listems,
						R.layout.qj_case_edit_state_item) {

					@Override
					public void convert(final ViewHolder viewHolder, final StateInfo item) {
						if (TextUtils.equals(item.name,state.name)) {
							viewHolder.getView(R.id.iv_selcted).setVisibility(View.VISIBLE);
						} else {
							viewHolder.getView(R.id.iv_selcted).setVisibility(View.GONE);
						}
						viewHolder.setText(R.id.tv_name, item.name);
						viewHolder.getView(R.id.rl_item).setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								state = item;
								et_state_add.setText(state.name);
								viewHolder.getView(R.id.iv_selcted).setVisibility(View.VISIBLE);
								mAdapter.notifyDataSetChanged();
							}
						});
					}

				};
				lv_state.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();
			}

			@Override
			public void onError(Call call, Exception e) {

			}
		});
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Activity context,String case_state) {
		Intent intent = new Intent();
		intent.putExtra("case_state", case_state);
		intent.setClass(context, EditCaseStateActivity.class);
		context.startActivityForResult(intent, EditCaseActivity.REQUEST_CASE_STATE);
	}

	// @Override
	// public void onClick(View v) {
	// switch (v.getId()) {
	// case R.id.btn_state_add:
	// String string = et_state_add.getText().toString().trim();
	// if (!TextUtils.isEmpty(string)) {
	// if (!listems.contains(string)) {
	// listems.add(string);
	// mAdapter.notifyDataSetChanged();
	// et_state_add.setText("");
	// Toast.makeText(context, string + "添加成功", Toast.LENGTH_LONG)
	// .show();
	// } else {
	// Toast.makeText(context, string + "状态已存在", Toast.LENGTH_LONG)
	// .show();
	// }
	// }
	// break;
	//
	// default:
	// break;
	// }
	// }
}
