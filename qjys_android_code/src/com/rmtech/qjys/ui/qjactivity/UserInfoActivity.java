package com.rmtech.qjys.ui.qjactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.rmtech.qjys.QjConstant;
import com.rmtech.qjys.R;
import com.rmtech.qjys.model.DoctorInfo;
import com.rmtech.qjys.ui.BaseActivity;
import com.rmtech.qjys.ui.ChatActivity;
import com.rmtech.qjys.ui.view.MeItemLayout;

/***
 * 详细资料 页面
 * 
 * @author Administrator
 * 
 */
public class UserInfoActivity extends BaseActivity implements OnClickListener {
	public static final int USER_BEIZHU = 0x5666;
	private MeItemLayout user_beizhu;
	private String beizhu;
	public DoctorInfo doctorInfo;

	private ImageView ivHead;
	private TextView tvBeizhu;
	private TextView tvNickname;
	private View vNikename;
	private TextView tvHello;
	private MeItemLayout userPhone;
	private MeItemLayout userBeizhu;
	private TextView tvHospitalLeft;
	private TextView tvHospital;
	private TextView tvDepartmentLeft;
	private TextView tvDepartment;
	private TextView tvChangyongLeft;
	private ToggleButton tbPull;
	private Button btnSendmessage;
	private TextView tv_name;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_userinfo);
		setTitle("详细资料");
		doctorInfo = getIntent().getParcelableExtra("DoctorInfo");
		setRightTitle("", null);
		initView();
	}

	private void initView() {
		user_beizhu = (MeItemLayout) findViewById(R.id.user_beizhu);
		user_beizhu.setOnClickListener(this);

		ivHead = (ImageView) findViewById(R.id.iv_head);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tvNickname = (TextView) findViewById(R.id.tv_nickname);
		vNikename = (View) findViewById(R.id.v_nikename);
		tvHello = (TextView) findViewById(R.id.tv_hello);
		userPhone = (MeItemLayout) findViewById(R.id.user_phone);
		userPhone.setOnClickListener(this);
		tvHospitalLeft = (TextView) findViewById(R.id.tv_hospital_left);
		tvHospital = (TextView) findViewById(R.id.tv_hospital);
		tvDepartmentLeft = (TextView) findViewById(R.id.tv_department_left);
		tvDepartment = (TextView) findViewById(R.id.tv_department);
		tvChangyongLeft = (TextView) findViewById(R.id.tv_changyong_left);
		tbPull = (ToggleButton) findViewById(R.id.tb_pull);
		btnSendmessage = (Button) findViewById(R.id.btn_sendmessage);
		btnSendmessage.setOnClickListener(this);
		tvHospital = (TextView) findViewById(R.id.tv_hospital);

		ImageLoader.getInstance().displayImage(doctorInfo.head, ivHead,
				QjConstant.optionsHead);
		tv_name.setText(doctorInfo.name);
		tvNickname.setText(doctorInfo.name);
		tvDepartment.setText(doctorInfo.department);
		tvHospital.setText(doctorInfo.hos_fullname);
		userPhone.setRightText(doctorInfo.phone);
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context, DoctorInfo info) {
		Intent intent = new Intent();
		intent.setClass(context, UserInfoActivity.class);
		intent.putExtra("DoctorInfo", (Parcelable) info);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.user_beizhu: {
			Intent intent = new Intent(this, UserInfoBeizhuActivity.class);
			startActivityForResult(intent, USER_BEIZHU);
			break;
		}
		case R.id.btn_sendmessage: {

			// Intent intent = new Intent(this, UserInfoBeizhuActivity.class);
			// startActivityForResult(intent, USER_BEIZHU);
			// break;
			if (doctorInfo.isMyself()) {
				Toast.makeText(getActivity(), "不能和自己聊天", 1).show();
			} else {
				Intent intent = new Intent(getActivity(), ChatActivity.class);
				intent.putExtra(QjConstant.EXTRA_CHAT_TYPE,
						QjConstant.CHATTYPE_SINGLE);
				intent.putExtra(QjConstant.EXTRA_USER_ID, doctorInfo.id);
				startActivity(intent);

			}
			break;
		}
		case R.id.user_phone:
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
					+ doctorInfo.phone));
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case USER_BEIZHU:
			if (resultCode == Activity.RESULT_OK) {
				beizhu = data.getStringExtra("string");
				setViewValue();
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void setViewValue() {
		user_beizhu.setRightText(beizhu);
	}
}
