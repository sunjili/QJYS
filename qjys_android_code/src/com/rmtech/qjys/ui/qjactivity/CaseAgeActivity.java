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
import com.rmtech.qjys.model.gson.MBase;
import com.rmtech.qjys.ui.BaseActivity;
import com.rmtech.qjys.utils.GroupAndCaseListManager;
import com.sjl.lib.http.okhttp.OkHttpUtils;
import com.umeng.analytics.MobclickAgent;

/***
 * 编辑年龄 页面
 * 
 * @author Administrator
 * 
 */
public class CaseAgeActivity extends CaseEidtBaseActivity {
	/** 年龄 */
	private EditText et_age;
	private String age;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_case_age);
		setTitle("年龄");
		setLeftTitle("返回");
		setRightTitle("保存", new OnClickListener() {

			@Override
			public void onClick(View v) {
				age = et_age.getText().toString().trim();
				if (mCaseInfo != null) {
					HashMap<String, String> params = new HashMap<String, String>();
					params.put("age", age);
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
								caseInfo.age = age;
							}
							Intent data = new Intent();
							data.putExtra("string", age);
							setResult(MeNameActivity.RESULT_OK, data);
							finish();
						}
					});
				}
			}

		});
		initView();
	}

	private void initView() {
		et_age = (EditText) findViewById(R.id.et_age);
		if (mCaseInfo != null) {
			et_age.setText(mCaseInfo.age);
		}
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, CaseAgeActivity.class);
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
