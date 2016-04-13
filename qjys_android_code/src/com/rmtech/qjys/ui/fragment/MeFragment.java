package com.rmtech.qjys.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.rmtech.qjys.R;
import com.rmtech.qjys.ui.qjactivity.MeAboutActivity;
import com.rmtech.qjys.ui.qjactivity.MeCleanMemoryActivity;
import com.rmtech.qjys.ui.qjactivity.MeHospitalActivity;
import com.rmtech.qjys.ui.qjactivity.MeNameActivity;
import com.rmtech.qjys.ui.qjactivity.MePasswordChangeActivity;
import com.rmtech.qjys.ui.qjactivity.MePhoneActivity;
import com.rmtech.qjys.ui.qjactivity.MeRoomActivity;
import com.rmtech.qjys.ui.qjactivity.MeSexActivity;
import com.rmtech.qjys.ui.qjactivity.MeTreatmentStateActivity;
import com.rmtech.qjys.ui.view.MeItemLayout;

/**
 *我  界面
 * 
 * 
 */
public class MeFragment extends Fragment implements OnClickListener {
	/***  头像  */
	private ImageView iv_head;
	/***   */
	private RelativeLayout rl_head;
	/**  名字         */
	private MeItemLayout  me_name;
	/**  性别         */
	private MeItemLayout  me_sex;
	/**  医院        */
	private MeItemLayout  me_hospital;
	/**  科室         */
	private MeItemLayout  me_room;
	/**  手机号         */
	private MeItemLayout  me_phone;
	/**  登录密码         */
	private MeItemLayout  me_password;
	/**  病例就诊状态        */
	private MeItemLayout  me_treatment_state;
	/**  临床诊疗规范及流程         */
	private MeItemLayout  me_standard_and_flow;
	/** 清理存储空间         */
	private MeItemLayout  me_clear_memory;
	/**  病例回收站         */
	private MeItemLayout  me_recycle;
	/**  关于奇迹医生         */
	private MeItemLayout  me_about;
	/**   退出登录	 */
	private Button btn_logout;

	private FragmentActivity context;
	
	private void setViewValue() {
		me_name.setRightText("111111");
		me_sex.setRightText("111111");
		me_hospital.setRightText("111111");
		me_room.setRightText("111111");
		
		me_phone.setRightText("13812345678");
		me_password.setRightText("");
		
//		me_treatment_state.setRightText("111111");
//		me_standard_and_flow.setRightText("111111");
//		me_clear_memory.setRightText("111111");
//		me_recycle.setRightText("111111");
//		me_about.setRightText("111111");

	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_head:
			Toast.makeText(context, "头像", Toast.LENGTH_SHORT).show();
			break;
		case R.id.me_name:
			jumpActivity(MeNameActivity.class);
			break;
		case R.id.me_sex:
			jumpActivity(MeSexActivity.class);
		break;
		case R.id.me_hospital:
			jumpActivity(MeHospitalActivity.class);
		break;
		case R.id.me_room:
			jumpActivity(MeRoomActivity.class);
		break;
		
		
		case R.id.me_phone:
			jumpActivity(MePhoneActivity.class);
		break;
		case R.id.me_password:
			//设置新密码
//			jumpActivity(MePasswordNewActivity.class);
			//更改密码
			jumpActivity(MePasswordChangeActivity.class);
		break;
		
		
		case R.id.me_treatment_state:
			jumpActivity(MeTreatmentStateActivity.class);
		break;
		case R.id.me_standard_and_flow:
			Toast.makeText(context, "11111", Toast.LENGTH_SHORT).show();
		break;
		case R.id.me_clear_memory:    
		jumpActivity(MeCleanMemoryActivity.class);
		break;
		case R.id.me_recycle:
			Toast.makeText(context, "11111", Toast.LENGTH_SHORT).show();
		break;
		
		case R.id.me_about:
			jumpActivity(MeAboutActivity.class);

		break;
		case R.id.btn_logout:
			Toast.makeText(context, "退出登录", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}
	private void jumpActivity(Class<?> cls) {
		Intent intent=new Intent(context,cls);
		startActivity(intent);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View inflate = inflater.inflate(R.layout.em_fragment_me, container, false);
		context =MeFragment.this.getActivity();
		initView(inflate);
		return inflate;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		setViewValue();
	}


	private void initView(View inflate) {
		me_name=(MeItemLayout) inflate.findViewById(R.id.me_name);
		me_name.setOnClickListener(this);
		me_sex=(MeItemLayout) inflate.findViewById(R.id.me_sex);
		me_sex.setOnClickListener(this);
		me_hospital=(MeItemLayout) inflate.findViewById(R.id.me_hospital);
		me_hospital.setOnClickListener(this);
		me_room=(MeItemLayout) inflate.findViewById(R.id.me_room);
		me_room.setOnClickListener(this);
		
		
		me_phone=(MeItemLayout) inflate.findViewById(R.id.me_phone);
		me_phone.setOnClickListener(this);
		me_password=(MeItemLayout) inflate.findViewById(R.id.me_password);
		me_password.setOnClickListener(this);
		
		
		me_treatment_state=(MeItemLayout) inflate.findViewById(R.id.me_treatment_state);
		me_treatment_state.setOnClickListener(this);
		me_standard_and_flow=(MeItemLayout) inflate.findViewById(R.id.me_standard_and_flow);
		me_standard_and_flow.setOnClickListener(this);
		me_clear_memory=(MeItemLayout) inflate.findViewById(R.id.me_clear_memory);
		me_clear_memory.setOnClickListener(this);
		me_recycle=(MeItemLayout) inflate.findViewById(R.id.me_recycle);
		me_recycle.setOnClickListener(this);
		me_about=(MeItemLayout) inflate.findViewById(R.id.me_about);
		me_about.setOnClickListener(this);
		
		
		btn_logout=(Button) inflate.findViewById(R.id.btn_logout);
		btn_logout.setOnClickListener(this);
		
		iv_head=(ImageView) inflate.findViewById(R.id.iv_head);
		rl_head=(RelativeLayout) inflate.findViewById(R.id.rl_head);
		rl_head.setOnClickListener(this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

}
