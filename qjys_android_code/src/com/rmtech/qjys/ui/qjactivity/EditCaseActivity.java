package com.rmtech.qjys.ui.qjactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.rmtech.qjys.R;
import com.rmtech.qjys.ui.BaseActivity;
import com.rmtech.qjys.ui.view.MeItemLayout;

public class EditCaseActivity extends BaseActivity implements
		View.OnClickListener {
	/**姓名*/
	private MeItemLayout case_name;
	/**性别*/
	private MeItemLayout case_sex;
	/**年龄*/
	private MeItemLayout case_age;
	/**医院*/
	private MeItemLayout case_hospital;
	/**科室*/
	private MeItemLayout case_room;
	/**病房号*/
	private MeItemLayout case_room_number;
	/**诊断*/
	private MeItemLayout case_diagnosis;
	/**就诊状态*/
	private MeItemLayout case_state;
	private Context context;
	private void setValue() {
		case_name.setRightText("测试111");
		case_sex.setRightText("测试111");
		case_age.setRightText("测试111");
		case_hospital.setRightText("测试111");
		case_room.setRightText("测试111");
		case_room_number.setRightText("测试111");
		case_diagnosis.setRightText("测试111");
		case_state.setRightText("测试111");	
		}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.case_name:
			jumpActivity(MeNameActivity.class);
			break;
		case R.id.case_sex:
			jumpActivity(MeSexActivity.class);
			break;
		case R.id.case_age:
			jumpActivity(CaseAgeActivity.class);
			break;
		case R.id.case_hospital:
			jumpActivity(MeHospitalActivity.class);
			break;
		case R.id.case_room:
			jumpActivity(MeRoomActivity.class);
			break;
		case R.id.case_room_number:
			jumpActivity(CaseRoomNumberActivity.class);
			break;
		case R.id.case_diagnosis:
			//TODO 
			jumpActivity(MeRoomActivity.class);
			break;
		case R.id.case_state:
			//TODO 
			jumpActivity(MeRoomActivity.class);
			break;
			
		default:
			break;
		}
	}

	private void jumpActivity(Class<?> cls) {
		Intent intent = new Intent(context, cls);
		startActivity(intent);
	}

	private void initView() {
		case_name = (MeItemLayout) findViewById(R.id.case_name);
		case_name.setOnClickListener(this);
		case_sex = (MeItemLayout) findViewById(R.id.case_sex);
		case_sex.setOnClickListener(this);
		case_age = (MeItemLayout) findViewById(R.id.case_age);
		case_age.setOnClickListener(this);
		case_hospital = (MeItemLayout) findViewById(R.id.case_hospital);
		case_hospital.setOnClickListener(this);
		case_room = (MeItemLayout) findViewById(R.id.case_room);
		case_room.setOnClickListener(this);
		case_room_number = (MeItemLayout) findViewById(R.id.case_room_number);
		case_room_number.setOnClickListener(this);
		case_diagnosis = (MeItemLayout) findViewById(R.id.case_diagnosis);
		case_diagnosis.setOnClickListener(this);
		case_state = (MeItemLayout) findViewById(R.id.case_state);
		case_state.setOnClickListener(this);
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_qj_edit_case);
		setTitle("编辑病例");
		context = EditCaseActivity.this;
		initView();
		setValue();
	}



	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Activity context) {
		Intent intent = new Intent();
		intent.setClass(context, EditCaseActivity.class);
		context.startActivity(intent);
	}

}
