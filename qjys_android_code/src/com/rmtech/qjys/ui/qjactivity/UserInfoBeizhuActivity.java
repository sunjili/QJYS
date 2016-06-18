package com.rmtech.qjys.ui.qjactivity;

import okhttp3.Call;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Text;

import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.R;
import com.rmtech.qjys.callback.BaseModelCallback;
import com.rmtech.qjys.model.DoctorInfo;
import com.rmtech.qjys.model.gson.MBase;
import com.rmtech.qjys.ui.BaseActivity;
import com.rmtech.qjys.utils.DoctorListManager;
import com.rmtech.qjys.utils.DoctorListManager.OnGetDoctorInfoCallback;
import com.umeng.analytics.MobclickAgent;

/***
 * 编辑备注 页面
 * 
 * @author Administrator
 * 
 */
public class UserInfoBeizhuActivity extends BaseActivity {
	/** 姓名 */
	private EditText et_name;
	private String name;
	private DoctorInfo mDoctorInfo;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		mDoctorInfo = getIntent().getParcelableExtra("DoctorInfo");
		if (mDoctorInfo == null) {
			finish();
			return;
		}
		setContentView(R.layout.qj_userinfo_beizhu);
		setTitle("备注信息");
		setLeftTitle("返回");
		setRightTitle("保存", new OnClickListener() {

			@Override
			public void onClick(View v) {
				name = et_name.getText().toString().trim();
				if (TextUtils.equals(name, mDoctorInfo.remark)) {
					Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_LONG).show();
					return;
				}
				QjHttp.setRemark(mDoctorInfo.id, name, new BaseModelCallback() {

					@Override
					public void onResponseSucces(MBase response) {
						Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_LONG).show();
						DoctorListManager.getInstance().getDoctorInfoByHXid(mDoctorInfo.id, new OnGetDoctorInfoCallback() {
							
							@Override
							public void onGet(DoctorInfo info) {
								if(info != null) {
									info.remark = name;
								}
							}
						});
						Intent data = new Intent();
						data.putExtra("string", name);
						setResult(UserInfoBeizhuActivity.RESULT_OK, data);
						finish();
					}

					@Override
					public void onError(Call call, Exception e) {
						Toast.makeText(getActivity(), "保存失败", Toast.LENGTH_LONG).show();
					}
				});
				
			}
		});
		initView();
	}

	private void initView() {
		et_name = (EditText) findViewById(R.id.et_name);
		setTextWhacher(UserInfoBeizhuActivity.this, et_name, 30);
		et_name.setText(TextUtils.isEmpty(mDoctorInfo.remark)?mDoctorInfo.name:mDoctorInfo.remark);
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Activity context, int requestCode, DoctorInfo doctorInfo) {
		Intent intent = new Intent();
		intent.setClass(context, UserInfoBeizhuActivity.class);
		intent.putExtra("DoctorInfo", (Parcelable) doctorInfo);
		context.startActivityForResult(intent, requestCode);
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
