package com.rmtech.qjys.ui.qjactivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rmtech.qjys.QjConstant;
import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.R;
import com.rmtech.qjys.adapter.CommonAdapter;
import com.rmtech.qjys.adapter.ViewHolder;
import com.rmtech.qjys.callback.BaseModelCallback;
import com.rmtech.qjys.callback.QjHttpCallback;
import com.rmtech.qjys.model.CaseInfo;
import com.rmtech.qjys.model.HospitalInfo;
import com.rmtech.qjys.model.UserContext;
import com.rmtech.qjys.model.gson.MBase;
import com.rmtech.qjys.model.gson.MHosList;
import com.rmtech.qjys.ui.BaseActivity;
import com.rmtech.qjys.utils.GroupAndCaseListManager;
import com.sjl.lib.http.okhttp.OkHttpUtils;

/***
 * 医院 页面
 * 
 * @author Administrator
 * 
 */
public class MeHospitalActivity extends CaseEidtBaseActivity {
	/** 医院名称 */
	private EditText et_hospital;

	private ListView lv_hospital;
	private CommonAdapter mAdapter;
	private Context context;
	List<HospitalInfo> listems;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		context = MeHospitalActivity.this;
		setContentView(R.layout.qj_me_hospital);
		setLeftTitle("返回");
		setRightTitle("保存", new OnClickListener() {

			@Override
			public void onClick(View v) {
				final String hospital = et_hospital.getEditableText().toString();

				if (mCaseInfo != null) {

					HashMap<String, String> params = new HashMap<String, String>();
					params.put("hos_name", hospital);
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
								caseInfo.hos_fullname = hospital;
							}
							Intent data = new Intent();
							data.putExtra("string", hospital);
							setResult(MeNameActivity.RESULT_OK, data);
							finish();
						}
					});
					return;
				}

				if (TextUtils.equals(UserContext.getInstance().getUser().hos_fullname, hospital)) {
					Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_LONG).show();
					finish();
					return;
				}
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("hos_name", hospital);
				QjHttp.updateUserinfo(params, new BaseModelCallback() {

					@Override
					public void onError(Call call, Exception e) {
						Toast.makeText(getActivity(), "保存失败", Toast.LENGTH_LONG).show();
					}

					@Override
					public void onResponseSucces(MBase response) {
						UserContext.getInstance().setUserHospital(hospital);
						Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_LONG).show();

						Intent data = new Intent();
						data.putExtra("string", hospital);
						setResult(MeNameActivity.RESULT_OK, data);
						finish();
					}

				});

			}
		});
		setTitle("医院");
		initView();
		setValue();
	}

	private void setValue() {
		listems = new ArrayList<HospitalInfo>();
		String name = "";
		int type = getIntent().getIntExtra("type",0);
		
		if (mCaseInfo != null) {
			name = mCaseInfo.hos_fullname;
		} else {
			if(type == QjConstant.REQUEST_CODE_ADD_HOSPITAL) {
				name = getIntent().getStringExtra("hosPital");
			} else {
				if (UserContext.getInstance().getUser() != null) {
					name = UserContext.getInstance().getUser().hos_fullname;
				}
			}
		}
		et_hospital.setText(name);
	}

	Runnable searchRunnable = new Runnable() {

		@Override
		public void run() {
			QjHttp.getHosByName(et_hospital.getText().toString(), new QjHttpCallback<MHosList>() {

				@Override
				public MHosList parseNetworkResponse(String str) throws Exception {
					// TODO Auto-generated method stub
					return new Gson().fromJson(str, MHosList.class);
				}

				@Override
				public void onResponseSucces(MHosList response) {

					// for (int i = 0; i < s.length(); i++) {
					// listems.add("测试" + i);
					// }
					if (response.data == null) {
						return;
					}
					listems.clear();
					listems.addAll(response.data);
					mAdapter = new CommonAdapter<HospitalInfo>(getApplicationContext(), listems,
							R.layout.qj_me_hospital_item) {
						@Override
						public void convert(ViewHolder viewHolder, final HospitalInfo item) {
							// viewHolder.setTextAndColor(R.id.tv_name, item,
							// s.toString(), Color.rgb(52, 100, 169));
							viewHolder.setText(R.id.tv_name, item.fullname);
							viewHolder.getView(R.id.rl_hospital_item).setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// Toast.makeText(context, item,
									// Toast.LENGTH_SHORT).show();
									et_hospital.setText(item.fullname);
								}
							});
						}
					};
					lv_hospital.setAdapter(mAdapter);
					mAdapter.notifyDataSetChanged();
				}

				@Override
				public void onError(Call call, Exception e) {
					// TODO Auto-generated method stub

				}
			});

		}
	};

	private void initView() {
		lv_hospital = (ListView) findViewById(R.id.lv_hospital);
		et_hospital = (EditText) findViewById(R.id.et_hospital);
		et_hospital.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(final CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() == 0) {
					lv_hospital.setVisibility(View.INVISIBLE);
				} else {
					lv_hospital.setVisibility(View.VISIBLE);
				}
				lv_hospital.removeCallbacks(searchRunnable);
				lv_hospital.postDelayed(searchRunnable, 500);
			}
		});
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Activity context, String hosPital) {
		Intent intent = new Intent();
		intent.setClass(context, MeHospitalActivity.class);
		intent.putExtra("hosPital", hosPital);
		intent.putExtra("type", QjConstant.REQUEST_CODE_ADD_HOSPITAL);
		context.startActivityForResult(intent, QjConstant.REQUEST_CODE_ADD_HOSPITAL);
	}
}
