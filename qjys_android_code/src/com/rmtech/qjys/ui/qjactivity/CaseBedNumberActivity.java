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

/***
 * 编辑病床号 页面
 * 
 * @author Administrator
 * 
 */
public class CaseBedNumberActivity extends CaseEidtBaseActivity {
	/** 床号 */
	private EditText et_bed;
	private String bed;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_case_bed);
		setTitle("病床号");
		setRightTitle("保存", new OnClickListener() {

			@Override
			public void onClick(View v) {
				bed = et_bed.getText().toString().trim();
				if (mCaseInfo != null) {

					HashMap<String, String> params = new HashMap<String, String>();
					params.put("bed_no", bed);
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
								caseInfo.bed_no = bed;
							}
							Intent data = new Intent();
							data.putExtra("string", bed);
							setResult(MeNameActivity.RESULT_OK, data);
							finish();
						}
					});

					return;

				}
				if (!TextUtils.isEmpty(bed)) {
					Intent data = new Intent();
					data.putExtra("string", bed);
					setResult(MeNameActivity.RESULT_OK, data);
					finish();
				}
			}
		});
		initView();
	}

	private void initView() {
		et_bed = (EditText) findViewById(R.id.et_bed);
		if (mCaseInfo != null) {
			et_bed.setText(mCaseInfo.bed_no);
		}
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, CaseBedNumberActivity.class);
		context.startActivity(intent);
	}
}
