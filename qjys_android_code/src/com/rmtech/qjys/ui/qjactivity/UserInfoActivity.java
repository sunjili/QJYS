package com.rmtech.qjys.ui.qjactivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import okhttp3.Call;

import org.greenrobot.eventbus.EventBus;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.signature.MediaStoreSignature;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.widget.EaseSwitchButton;
import com.hyphenate.exceptions.HyphenateException;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rmtech.qjys.QjConstant;
import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.R;
import com.rmtech.qjys.callback.BaseModelCallback;
import com.rmtech.qjys.db.InviteMessgeDao;
import com.rmtech.qjys.db.UserDao;
import com.rmtech.qjys.domain.InviteMessage;
import com.rmtech.qjys.domain.InviteMessage.InviteMesageStatus;
import com.rmtech.qjys.event.DoctorEvent;
import com.rmtech.qjys.hx.QjHelper;
import com.rmtech.qjys.model.DoctorInfo;
import com.rmtech.qjys.model.gson.MBase;
import com.rmtech.qjys.ui.BaseActivity;
import com.rmtech.qjys.ui.ChatActivity;
import com.rmtech.qjys.ui.view.CustomSimpleDialog;
import com.rmtech.qjys.ui.view.MeItemLayout;
import com.rmtech.qjys.ui.view.CustomSimpleDialog.Builder;
import com.rmtech.qjys.utils.DoctorListManager;
import com.rmtech.qjys.utils.DoctorListManager.OnGetDoctorInfoCallback;
import com.rmtech.qjys.utils.PreferenceManager;
import com.sjl.lib.alertview.AlertView;
import com.sjl.lib.db.DBUtil;
import com.umeng.analytics.MobclickAgent;

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
	public InviteMessage msg;
	private String from;
	public int location;
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
	private RelativeLayout rl_changyong;
	private View line_padding;
	private String message;
	
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
		message = getIntent().getStringExtra("message");
		if(doctorInfo == null) {
			return;
		}
		// 暂时先通过通讯录电话号list判断还有关系，以后有可能会改
		tempPhones.addAll(DoctorListManager.getInstance().getDoctorPhonNumberList());
		for(int i = 0;i<tempPhones.size();i++){
			if(doctorInfo.phone!=null&&doctorInfo.phone.equals(tempPhones.get(i))){
				isFriend = true;
				doctorInfo.isFriend = 1;
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
			line_padding.setVisibility(View.GONE);
			rl_changyong.setVisibility(View.GONE);
			if(from.equals("newFriendsMsg")){
				btnDelete.setVisibility(View.GONE);
				user_beizhu.setVisibility(View.VISIBLE);
				userPhone.setVisibility(View.VISIBLE);
				btnSendmessage.setText("通过验证");
				if(message!=null){
					tvHello.setText(message);
				}else {
					tvHello.setText(doctorInfo.getDisplayName() + "请求添加您为好友");
				}
			}else if(from.equals("qrcode")){
				setLeftTitle("扫一扫");
				btnDelete.setVisibility(View.GONE);
				user_beizhu.setVisibility(View.VISIBLE);
				userPhone.setVisibility(View.VISIBLE);
				btnSendmessage.setText("添加到通讯录");
			}else if(from.equals("group")){
				setLeftTitle("群组详情");
				btnDelete.setVisibility(View.GONE);
				user_beizhu.setVisibility(View.VISIBLE);
				userPhone.setVisibility(View.VISIBLE);
				btnSendmessage.setText("添加到通讯录");
			}else{
				btnDelete.setVisibility(View.GONE);
				user_beizhu.setVisibility(View.VISIBLE);
				userPhone.setVisibility(View.VISIBLE);
				btnSendmessage.setText("添加到通讯录");
			}
		}else{//btnDelete 暂时先隐藏掉
			if(from.equals("contactList")){
				setLeftTitle("通讯录");
				btnDelete.setVisibility(View.GONE);
				btnSendmessage.setText("发消息");
			}else if(from.equals("chat")){
				setLeftTitle("聊天");
				btnDelete.setVisibility(View.GONE);
				btnSendmessage.setText("发消息");
			}else if(from.equals("newFriendsMsg")){
				setLeftTitle("新的朋友");
				btnDelete.setVisibility(View.GONE);
				btnSendmessage.setText("发消息");
			}else if(from.equals("qrcode")){
				setLeftTitle("扫一扫");
				btnDelete.setVisibility(View.GONE);
				btnSendmessage.setText("发消息");
			}else if(from.equals("group")){
				setLeftTitle("群组详情");
				btnDelete.setVisibility(View.GONE);
				btnSendmessage.setText("发消息");
			}
		}
		
		setRightTitle("", null);
	}

	@SuppressLint("ResourceAsColor")
	private void initView() {
		user_beizhu = (MeItemLayout) findViewById(R.id.user_beizhu);
		user_beizhu.setOnClickListener(this);
		user_beizhu.v_bottom_line.setPadding(0, 0, 0, 0);
		if(doctorInfo != null && !TextUtils.isEmpty(doctorInfo.remark)) {
			user_beizhu.setRightText(doctorInfo.remark);
		} else {
			user_beizhu.setRightText("未设置");
		}
		line_padding = (View) findViewById(R.id.line_padding);
		rl_changyong = (RelativeLayout) findViewById(R.id.rl_changyong);
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
		tv_name.setText(!TextUtils.isEmpty(doctorInfo.remark) ? doctorInfo.remark : doctorInfo.name);
		// 这里改错了吧，昵称应该是用户名，没设用户名的话就应该是手机号
		tvNickname.setText("昵称："+doctorInfo.name);
		// tv_name 优先显示备注，没有备注就显示用户名，特别提醒：用户没设用户名的话 后台会自动把doctorInfo.name的内容设定为手机号，不需要咱们手动添加doctorInfo.phone
		// 非好友的话一定是没有备注的，那就显示用户名
		
		if(!TextUtils.isEmpty(doctorInfo.remark)){
			tvNickname.setVisibility(View.VISIBLE);
			vNikename.setVisibility(View.VISIBLE);
		}else {
			tvNickname.setVisibility(View.GONE);
			vNikename.setVisibility(View.GONE);
		}
		tvDepartment.setText((TextUtils.isEmpty(doctorInfo.department)||doctorInfo.department.equals("0"))?"":doctorInfo.department);
		tvHospital.setText(doctorInfo.hos_fullname);
		userPhone.setRightText(doctorInfo.phone);
		userPhone.setRightTextColor_c3264aa(UserInfoActivity.this);
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
	
	/**
	 * 同意好友请求
	 * @param username
	 */
	private void acceptInvitation(final InviteMessage msg) {
		final ProgressDialog pd = new ProgressDialog(getActivity());
		String str1 = getActivity().getResources().getString(R.string.Are_agree_with);
		final String str2 = getActivity().getResources().getString(R.string.Has_agreed_to);
		final String str3 = getActivity().getResources().getString(R.string.Agree_with_failure);
		pd.setMessage(str1);
		pd.setCanceledOnTouchOutside(false);
		pd.show();

		new Thread(new Runnable() {
			public void run() {
				// 调用sdk的同意方法
				try {
					if (msg.getStatus() == InviteMesageStatus.BEINVITEED) {//同意好友请求
						EMClient.getInstance().contactManager().acceptInvitation(msg.getFrom());
					} 
//					else if (msg.getStatus() == InviteMesageStatus.BEAPPLYED) { //同意加群申请
//						EMClient.getInstance().groupManager().acceptApplication(msg.getFrom(), msg.getGroupId());
//					} else if (msg.getStatus() == InviteMesageStatus.GROUPINVITATION) {
//					    EMClient.getInstance().groupManager().acceptInvitation(msg.getGroupId(), msg.getGroupInviter());
//					}
                    msg.setStatus(InviteMesageStatus.AGREED);
                    // 更新db
                    ContentValues values = new ContentValues();
                    values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg.getStatus().ordinal());
                    new InviteMessgeDao(getActivity()).updateMessage(msg.getId(), values);
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							EventBus.getDefault().post(new DoctorEvent(DoctorEvent.TYPE_ADD));
							pd.dismiss();
							doctorInfo.isFriend = 1;
							btnDelete.setVisibility(View.GONE);
							btnSendmessage.setText("发消息");
							user_beizhu.setVisibility(View.VISIBLE);
							userPhone.setVisibility(View.VISIBLE);
						}
					});
				} catch (final Exception e) {
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							pd.dismiss();
							Toast.makeText(getActivity(), str3 + e.getMessage(), 1).show();
						}
					});

				}
			}
		}).start();
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
		case R.id.btn_sendmessage:
			// Intent intent = new Intent(this, UserInfoBeizhuActivity.class);
			// startActivityForResult(intent, USER_BEIZHU);
			// break;
			if(!isFriend&&!from.equals("newFriendsMsg")){//TODO.
		        Intent intent = new Intent(UserInfoActivity.this, CaseAddFriendActivity.class);
		        intent.putExtra("userId", doctorInfo.id);
		        intent.putExtra("from", "UserInfoActivity");
		        startActivity(intent);
			}else if(!isFriend&&from.equals("newFriendsMsg")){
				//TODO 通过验证
				location = getIntent().getIntExtra("location", 0);
				msg = new InviteMessgeDao(getActivity()).getMessagesList().get(location);
				acceptInvitation(msg);
				line_padding.setVisibility(View.VISIBLE);
				rl_changyong.setVisibility(View.VISIBLE);
				tvHello.setText("");
				
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
			}
		    break;
		case R.id.user_phone:
			CustomSimpleDialog.Builder builder = new Builder(
					getActivity());
			builder.setTitle("");
			builder.setMessage(doctorInfo.phone);
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							
						}

					});
			builder.setPositiveButton("呼叫",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
									+ doctorInfo.phone));
							startActivity(intent);
						}
					});
			builder.create().show();
			break;
		case R.id.btn_delete://删除好友功能
			//TODO 一直感觉应该在doctorInfo加一个是否为好友的参数，相应的接口也需要改？暂时先这样写
			
			new AlertView("将联系人 " + doctorInfo.getDisplayName() + " 删除，同时删除\n与该联系人的聊天记录！", null, "取消", null, new String[] {"删除联系人"}, getActivity(),
					AlertView.Style.ActionSheet, new com.sjl.lib.alertview.OnItemClickListener() {

						@Override
						public void onItemClick(Object o, int position) {
							switch (position) {
							case 0://TODO 具体删除操作
//								Toast.makeText(UserInfoActivity.this, "删除联系人", Toast.LENGTH_SHORT).show();
								//TODO 有可能需要进一步调整
								String st1 = getResources().getString(R.string.deleting);
								final String st2 = getResources().getString(R.string.Delete_failed);
								final ProgressDialog pd = new ProgressDialog(getActivity());
								pd.setMessage(st1);
								pd.setCanceledOnTouchOutside(false);
								pd.show();
								
								QjHttp.deleteFriend(doctorInfo.id, new BaseModelCallback() {
									
									@Override
									public void onResponseSucces(MBase response) {
										new Thread(new Runnable() {
											@Override
											public void run() {		
												try {
													EMClient.getInstance().contactManager().deleteContact(doctorInfo.id);
													DoctorListManager.getInstance().deleteDoctorInfo(doctorInfo.id);
													
													// 删除db和内存中此用户的数据
													UserDao dao = new UserDao(getActivity());
													dao.deleteContact(doctorInfo.id);
													QjHelper.getInstance().getContactList().remove(doctorInfo.id);
													getActivity().runOnUiThread(new Runnable() {
														public void run() {
															pd.dismiss();
															UserInfoActivity.this.finish();
															EventBus.getDefault().post(new DoctorEvent(DoctorEvent.TYPE_DELETE));

														}
													});
													
												} catch (final Exception e) {
													getActivity().runOnUiThread(new Runnable() {
														public void run() {
															pd.dismiss();
															Toast.makeText(getActivity(), st2 + e.getMessage(), 1).show();
														}
													});

												}
											}
										}).start();
									}
									
									@Override
									public void onError(Call call, Exception e) {
										runOnUiThread(new Runnable() {
											
											@Override
											public void run() {
												pd.dismiss();
												Toast.makeText(getActivity(),st2, 1).show();
											}
										});

									}
								});
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
				tvNickname.setVisibility(View.VISIBLE);
				vNikename.setVisibility(View.VISIBLE);
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void setViewValue() {
		
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
