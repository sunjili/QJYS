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
import com.rmtech.qjys.utils.GroupAndCaseListManager;
import com.sjl.lib.http.okhttp.OkHttpUtils;

/***
 * 编辑病房号 页面
 * 
 * @author Administrator
 * 
 */
public class CaseRoomNumberActivity extends CaseEidtBaseActivity {
	/** 病房号 */
	private EditText et_room_number;
	private String roomNumber;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_case_room_number);
		setTitle("病房号");
		setLeftTitle("返回");
		setRightTitle("保存", new OnClickListener() {

			@Override
			public void onClick(View v) {
				roomNumber = et_room_number.getText().toString().trim();
				if (mCaseInfo != null) {
					HashMap<String, String> params = new HashMap<String, String>();
					params.put("ward_no", roomNumber);
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
								caseInfo.ward_no = roomNumber;
							}
							Intent data = new Intent();
							data.putExtra("string", roomNumber);
							setResult(MeNameActivity.RESULT_OK, data);
							finish();
						}
					});

					return;
				
				}
//				if (!TextUtils.isEmpty(roomNumber)) {
//					Intent data = new Intent();
//					data.putExtra("string", roomNumber);
//					setResult(MeNameActivity.RESULT_OK, data);
//				}
//				finish();
			}
		});
		initView();
	}

	private void initView() {
		et_room_number = (EditText) findViewById(R.id.et_room_number);
		setTextWhacher(CaseRoomNumberActivity.this, et_room_number, 30);
		if (mCaseInfo != null) {
			et_room_number.setText(mCaseInfo.ward_no);
		}
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, CaseRoomNumberActivity.class);
		context.startActivity(intent);
	}
}
