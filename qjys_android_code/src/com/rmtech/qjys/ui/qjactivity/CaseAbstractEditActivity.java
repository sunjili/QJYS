package com.rmtech.qjys.ui.qjactivity;

import java.util.HashMap;

import okhttp3.Call;

import org.greenrobot.eventbus.EventBus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.rmtech.qjys.QjConstant;
import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.R;
import com.rmtech.qjys.callback.BaseModelCallback;
import com.rmtech.qjys.event.CaseEvent;
import com.rmtech.qjys.model.CaseInfo;
import com.rmtech.qjys.model.gson.MBase;
import com.rmtech.qjys.utils.GroupAndCaseListManager;
import com.sjl.lib.http.okhttp.OkHttpUtils;
import com.umeng.analytics.MobclickAgent;

/***
 * 摘要
 * 
 * @author Administrator
 * 
 */
public class CaseAbstractEditActivity extends CaseWithIdActivity {

	private EditText tv_content;
	protected String absString;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_case_abs_edit);
		setTitle("编辑病例摘要");
		initView();
		setLeftTitle("返回");
		setRightTitle("保存", new OnClickListener() {

			@Override
			public void onClick(View v) {
				absString = tv_content.getText().toString().trim();
				
				if (caseInfo != null) {
					HashMap<String, String> params = new HashMap<String, String>();
					params.put("abs", absString);
					params.put("patient_id", caseInfo.id);
					OkHttpUtils.post(QjHttp.URL_UPDATE_PATIENT, params, new BaseModelCallback() {

						@Override
						public void onError(Call call, Exception e) {
							Toast.makeText(getActivity(), "保存失败", Toast.LENGTH_LONG).show();

						}

						@Override
						public void onResponseSucces(MBase response) {
							Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_LONG).show();

							CaseInfo newCase = GroupAndCaseListManager.getInstance().getCaseInfoByCaseId(caseInfo.id);
							if (newCase != null) {
								newCase.abs = absString;
							}
							CaseEvent event = new CaseEvent(CaseEvent.TYPE_EDIT);
							event.setCaseInfoId(caseInfo.id);
							EventBus.getDefault().post(event);
							Intent data = new Intent();
							data.putExtra("string", absString);
							setResult(MeNameActivity.RESULT_OK, data);
							finish();
						}
					});
				}
			}
		});
	}

	private void initView() {

		tv_content = (EditText) findViewById(R.id.et_content);
		setTextWhacher(CaseAbstractEditActivity.this, tv_content, 8000);
		Editable etext = tv_content.getText();
		Selection.setSelection(etext, etext.length());
		if (caseInfo != null && !TextUtils.isEmpty(caseInfo.abs)) {
			tv_content.setText(caseInfo.abs);
		}
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Activity context, CaseInfo caseInfo) {
		Intent intent = new Intent();
		setCaseInfo(intent, caseInfo);
		intent.setClass(context, CaseAbstractEditActivity.class);
		context.startActivityForResult(intent, QjConstant.REQUEST_CODE_EDIT_CASE);
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
