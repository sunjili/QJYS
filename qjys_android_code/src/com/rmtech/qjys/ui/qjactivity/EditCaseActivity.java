package com.rmtech.qjys.ui.qjactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import com.rmtech.qjys.R;
import com.rmtech.qjys.model.CaseInfo;
import com.rmtech.qjys.ui.BaseActivity;
import com.rmtech.qjys.ui.fragment.MeFragment;
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
	private CaseInfo meValue;
	private void setViewValue() {
		case_name.setRightText(meValue.name);
		if (meValue.sex==0) {
			case_sex.setRightText("男");
		} else {
			case_sex.setRightText("女");
		}
		case_age.setRightText(meValue.age);
		case_hospital.setRightText(meValue.hos_name);
		case_room.setRightText(meValue.department);
		case_room_number.setRightText(meValue.ward_no);
		case_diagnosis.setRightText(meValue.diagnose);
		case_state.setRightText(meValue.treat_state);
		}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.case_name:
			 jumpActivity(MeNameActivity.class,MeFragment.REQUEST_ME_NAME);
			break;
		case R.id.case_sex:
			jumpActivity(MeSexActivity.class,MeFragment.REQUEST_ME_SEX);
			break;
		case R.id.case_age:
			jumpActivity(CaseAgeActivity.class);
			break;
		case R.id.case_hospital:
			jumpActivity(MeHospitalActivity.class,MeFragment.REQUEST_ME_HOSPITAL);
			break;
		case R.id.case_room:
			jumpActivity(MeRoomActivity.class,MeFragment.REQUEST_ME_ROOM);
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
	private void jumpActivity(Class<?> cls,int  requestCode) {
		Intent intent = new Intent(context,cls);
		startActivityForResult(intent, requestCode);
	}
	private void jumpActivity(Class<?> cls,int  requestCode,String str) {
		Intent intent = new Intent(context,cls);
		intent.putExtra("string", str);
		startActivityForResult(intent, requestCode);
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
//		meValue=new CaseInfo();
		meValue = (CaseInfo) getIntent().getParcelableExtra("case_info");
		setViewValue();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		String string;
		switch (requestCode) {
		case MeFragment.REQUEST_ME_NAME:
			if(resultCode == Activity.RESULT_OK){
				string = data.getStringExtra("string");
				meValue.name=string;
				setViewValue();
			} 
			break;
		case MeFragment.REQUEST_ME_SEX:
			if(resultCode == Activity.RESULT_OK){
				 int sex = data.getIntExtra("int", meValue.sex);
				meValue.sex=sex;
				setViewValue();
			}
				break;
			case MeFragment.REQUEST_ME_HOSPITAL:
				if(resultCode == Activity.RESULT_OK){
					string = data.getStringExtra("string");
					meValue.hos_name=string;
					setViewValue();
				}
			
				break;
			case MeFragment.REQUEST_ME_ROOM:
				if(resultCode == Activity.RESULT_OK){
					string = data.getStringExtra("string");
					meValue.department=string;
					setViewValue();
				}

				break;
		
		default:
			break;
		}
		super.onActivityResult( requestCode,  resultCode,  data);
	}


	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Activity context,  CaseInfo caseInfo) {
		Intent intent = new Intent();
		intent.setClass(context, EditCaseActivity.class);
		intent.putExtra("case_info", (Parcelable)caseInfo);
		context.startActivity(intent);
	}

}
