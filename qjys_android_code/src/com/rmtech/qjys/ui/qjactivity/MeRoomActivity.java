package com.rmtech.qjys.ui.qjactivity;

import java.util.HashMap;

import okhttp3.Call;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.R;
import com.rmtech.qjys.callback.BaseModelCallback;
import com.rmtech.qjys.model.CaseInfo;
import com.rmtech.qjys.model.UserContext;
import com.rmtech.qjys.model.gson.MBase;
import com.rmtech.qjys.ui.BaseActivity;
import com.rmtech.qjys.utils.GroupAndCaseListManager;
import com.sjl.lib.http.okhttp.OkHttpUtils;
import com.umeng.analytics.MobclickAgent;

/***
 * 编辑科室 页面
 * 
 * @author Administrator
 * 
 */
public class MeRoomActivity extends CaseEidtBaseActivity {
	private EditText et_name;
	private String roomName;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_me_room);
		setTitle("科室");
		setLeftTitle("返回");
		setRightTitle("保存", new OnClickListener() {

			@Override
			public void onClick(View v) {

				roomName = et_name.getText().toString().trim();
				if (mCaseInfo != null) {

					HashMap<String, String> params = new HashMap<String, String>();
					params.put("department", roomName);
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
								caseInfo.department = roomName;
							}
							Intent data = new Intent();
							data.putExtra("string", roomName);
							setResult(MeNameActivity.RESULT_OK, data);
							finish();
						}
					});

					return;
				
				}
				if (TextUtils.equals(UserContext.getInstance().getUser().department, roomName)) {
					Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_LONG).show();
					finish();
					return;
				}
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("department", roomName);
				QjHttp.updateUserinfo(params, new BaseModelCallback() {

					@Override
					public void onError(Call call, Exception e) {
						Toast.makeText(getActivity(), "保存失败", Toast.LENGTH_LONG).show();
					}

					@Override
					public void onResponseSucces(MBase response) {
						UserContext.getInstance().setUserDepartment(roomName);
						Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_LONG).show();

						Intent data = new Intent();
						data.putExtra("string", roomName);
						setResult(MeNameActivity.RESULT_OK, data);
						finish();
					}

				});

			}
		});
		initView();
	}

	private void initView() {
		et_name = (EditText) findViewById(R.id.et_room_name);
		setTextWhacher(MeRoomActivity.this, et_name, 60);
		if (mCaseInfo != null) {
			et_name.setText(mCaseInfo.department.equals("0")?"":mCaseInfo.department);
		} else if (UserContext.getInstance().getUser().department != null) {
			et_name.setText(UserContext.getInstance().getUser().department.equals("0")?"":UserContext.getInstance().getUser().department);
		}
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, MeRoomActivity.class);
		context.startActivity(intent);
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
