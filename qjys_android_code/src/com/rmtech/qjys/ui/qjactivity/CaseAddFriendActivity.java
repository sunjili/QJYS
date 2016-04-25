package com.rmtech.qjys.ui.qjactivity;

import java.io.Serializable;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.rmtech.qjys.R;
import com.rmtech.qjys.model.PhoneContact;
import com.rmtech.qjys.ui.BaseActivity;
import com.rmtech.qjys.utils.DoctorListManager;
import com.rmtech.qjys.utils.DoctorListManager.OnAddFriendCallback;

/***
 * 添加新朋友 页面
 * 
 * @author Administrator
 * 
 */
public class CaseAddFriendActivity extends BaseActivity {
	/** 姓名 */
	private EditText et_name;
	private String name;
	private int position;
	private ImageView iv_clean;
	int[] state1 = new int[] {};
	private PhoneContact phoneContact;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_case_friend_add);
		setTitle("添加新朋友");
		phoneContact = (PhoneContact) getIntent().getSerializableExtra("class");
		setRightTitle("发送", new OnClickListener() {
			@Override
			public void onClick(View v) {
				DoctorListManager.addFriendByPhoneNumber(getActivity(), phoneContact.getPhone(), et_name.getText()
						.toString(), new OnAddFriendCallback() {

							@Override
							public void onSendRequestSuccess() {
								name = et_name.getText().toString().trim();
								Intent data = new Intent();
								Bundle bundle = new Bundle();
								bundle.putSerializable("class", phoneContact);
								data.putExtras(bundle);
								setResult(CaseAddFriendActivity.RESULT_OK, data);
								finish();
							}

							@Override
							public void onSendRequestError() {
								// TODO Auto-generated method stub
//								name = et_name.getText().toString().trim();
//								Intent data = new Intent();
//								Bundle bundle = new Bundle();
//								bundle.putSerializable("class", phoneContact);
//								data.putExtras(bundle);
//								setResult(CaseAddFriendActivity.RESULT_OK, data);
//								finish();
							}
				});
				
			}
		});
		initView();
	}

	private void initView() {
		et_name = (EditText) findViewById(R.id.et_name);
		if (phoneContact != null && phoneContact.getName() != null) {
			et_name.setText("我是 " + phoneContact.getName());
		}
		iv_clean = (ImageView) findViewById(R.id.iv_clean);
		iv_clean.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				et_name.setText("");
			}
		});
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, CaseAddFriendActivity.class);
		context.startActivity(intent);
	}
}
