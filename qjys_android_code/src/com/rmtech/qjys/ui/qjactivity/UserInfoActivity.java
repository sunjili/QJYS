package com.rmtech.qjys.ui.qjactivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.widget.EaseSwitchButton;
import com.hyphenate.exceptions.HyphenateException;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rmtech.qjys.QjConstant;
import com.rmtech.qjys.R;
import com.rmtech.qjys.model.DoctorInfo;
import com.rmtech.qjys.ui.BaseActivity;
import com.rmtech.qjys.ui.ChatActivity;
import com.rmtech.qjys.ui.view.MeItemLayout;
import com.rmtech.qjys.utils.DoctorListManager;
import com.rmtech.qjys.utils.DoctorListManager.OnGetDoctorInfoCallback;
import com.rmtech.qjys.utils.PreferenceManager;
import com.sjl.lib.alertview.AlertView;
import com.sjl.lib.db.DBUtil;

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
	private String from;

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
	private EaseSwitchButton tbPull;
	private Button btnSendmessage;
	private Button btnDelete;
	private TextView tv_name;
	
	private ArrayList<String> tempPhones = new ArrayList<String>();
	private boolean isFriend = false;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_userinfo);
		setTitle("详细资料");
		HashSet<String> mostUseList = DoctorListManager.getChangyongList();
		
		doctorInfo = getIntent().getParcelableExtra("DoctorInfo");
		from = getIntent().getStringExtra("from");

		// 暂时先通过通讯录电话号list判断还有关系，以后有可能会改
		tempPhones.addAll(DoctorListManager.getInstance().getDoctorPhonNumberList());
		for(int i = 0;i<tempPhones.size();i++){
			if(doctorInfo.phone!=null&&doctorInfo.phone.equals(tempPhones.get(i))){
				isFriend = true;
				break;
			}
		}
		initView();
		if(mostUseList!=null&&mostUseList.contains(doctorInfo.id)){
			tbPull.openSwitch();
		}else {
			tbPull.closeSwitch();
		}
		if(!isFriend){
			setLeftTitle("通讯录");
			if(from.equals("newFriendsMsg")){//暂时不知道从哪跳转过来
				btnDelete.setVisibility(View.GONE);
				user_beizhu.setVisibility(View.GONE);
				userPhone.setVisibility(View.VISIBLE);
				btnSendmessage.setText("通过验证");
			}else{
				btnDelete.setVisibility(View.GONE);
				user_beizhu.setVisibility(View.VISIBLE);
				userPhone.setVisibility(View.GONE);
				btnSendmessage.setText("添加到通讯录");
			}
		}else{
			if(from.equals("contactList")){
				setLeftTitle("通讯录");
				btnDelete.setVisibility(View.VISIBLE);
				btnSendmessage.setText("发消息");
			}else if(from.equals("chat")||from.equals("group")){
				setLeftTitle("聊天");
				btnDelete.setVisibility(View.VISIBLE);
				btnSendmessage.setText("发消息");
			}
		}
		
		
		setRightTitle("", null);
	}

	private void initView() {
		user_beizhu = (MeItemLayout) findViewById(R.id.user_beizhu);
		user_beizhu.setOnClickListener(this);
		if(doctorInfo != null && !TextUtils.isEmpty(doctorInfo.remark)) {
			user_beizhu.setRightText(doctorInfo.remark);
		} else {
			user_beizhu.setRightText("未设置");
		}
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
		tbPull = (EaseSwitchButton) findViewById(R.id.tb_pull);//TODO 设置为常用联系人功能需要做！！！！
		tbPull.setOnClickListener(this);
		btnSendmessage = (Button) findViewById(R.id.btn_sendmessage);
		btnSendmessage.setOnClickListener(this);
		btnDelete =  (Button) findViewById(R.id.btn_delete);
		btnDelete.setOnClickListener(this);
		tvHospital = (TextView) findViewById(R.id.tv_hospital);

		ImageLoader.getInstance().displayImage(doctorInfo.head, ivHead,
				QjConstant.optionsHead);
		tv_name.setText(doctorInfo.remark!=null ? doctorInfo.remark : doctorInfo.name);
		tvNickname.setText(doctorInfo.name);
		tvDepartment.setText(doctorInfo.department);
		tvHospital.setText(doctorInfo.hos_fullname);
		userPhone.setRightText(doctorInfo.phone);
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context, DoctorInfo info, String from) {
		Intent intent = new Intent();
		intent.setClass(context, UserInfoActivity.class);
		intent.putExtra("DoctorInfo", (Parcelable) info);
		intent.putExtra("from", from);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.user_beizhu: {
			UserInfoBeizhuActivity.show(this,USER_BEIZHU, doctorInfo);
//			Intent intent = new Intent(this, UserInfoBeizhuActivity.class);
//			startActivityForResult(intent, USER_BEIZHU);
			break;
		}
		case R.id.btn_sendmessage: {

			// Intent intent = new Intent(this, UserInfoBeizhuActivity.class);
			// startActivityForResult(intent, USER_BEIZHU);
			// break;
			if(!isFriend&&!from.equals("newFriendsMsg")){//TODO 添加到通讯录；这里条件不确定，以后有可能增加从群聊点进来但不是好友的情况。
				//TODO 添加好友功能
				try {
					EMClient.getInstance().contactManager().addContact(doctorInfo.id, "");
					Toast.makeText(UserInfoActivity.this, "好友请求已发送！", Toast.LENGTH_SHORT).show();
				} catch (HyphenateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(from.equals("newFriendsMsg")){
				//TODO 通过验证
				
			}else{//发消息
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
		}
		case R.id.user_phone:
			if(isFriend){
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ doctorInfo.phone));
				startActivity(intent);
			}else{
				//TODO 非好友，不能直接拨打电话
			}
			break;
		case R.id.btn_delete://删除好友功能
			//TODO 一直感觉应该在doctorInfo加一个是否为好友的参数，相应的接口也需要改？暂时先这样写
			//TODO 暂时先完成删除好友功能，上面灰色提示的文字还没做
			
			new AlertView(null, null, "取消", null, new String[] {"删除联系人"}, getActivity(),
					AlertView.Style.ActionSheet, new com.sjl.lib.alertview.OnItemClickListener() {

						@Override
						public void onItemClick(Object o, int position) {
							switch (position) {
							case 0://TODO 具体删除操作
								Toast.makeText(UserInfoActivity.this, "删除联系人", Toast.LENGTH_SHORT).show();
								//TODO 有可能需要进一步调整
								try {
									EMClient.getInstance().contactManager().deleteContact(doctorInfo.id);
								} catch (HyphenateException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								break;
							}
						}
					})
			.show();
			break;
		case R.id.tb_pull:
			if(tbPull.isSwitchOpen()){
				tbPull.closeSwitch();
				DoctorListManager.setMostUse(doctorInfo.id,false);
				
			}else{
				tbPull.openSwitch();
				DoctorListManager.setMostUse(doctorInfo.id,true);

			}
		

			break;
		}
	}
	// 对ids字符串的操作，包含对docinfo.id的删除的添加
//	public String splitString(String id){
//		String str1 = mSharedPreferences.getString("changyong", null);
//		String str2 = "";
//		int j = 0;
//		int i = 0;
//		if(str1!=null){
//			String[] strArray = str1.split(",");
//			for(i=0;i<strArray.length;i++){
//				if(strArray[i].equals(id)){
//					continue;
//				}else{
//					j++;
//				}
//				str2 = str2 + strArray[i] + ",";
//			}
//			if(j==i){
//				str2 = str1 + "," + id;
//			}else {
//				str2 = str2.substring(0, str2.length()-1);
//			}
//			return str2;
//		}else{
//			return id;
//		}
//	}

	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case USER_BEIZHU:
			if (resultCode == Activity.RESULT_OK) {
				beizhu = data.getStringExtra("string");
				user_beizhu.setRightText(beizhu);
				tv_name.setText(beizhu);
				doctorInfo.remark = beizhu;
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void setViewValue() {
		
	}
}
