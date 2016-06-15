package com.rmtech.qjys.ui.qjactivity;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.easeui.widget.EaseAlertDialog;
import com.hyphenate.easeui.widget.EaseExpandGridView;
import com.hyphenate.easeui.widget.EaseSwitchButton;
import com.hyphenate.easeui.widget.EaseAlertDialog.AlertDialogUser;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rmtech.qjys.QjConstant;
import com.rmtech.qjys.R;
import com.rmtech.qjys.model.CaseInfo;
import com.rmtech.qjys.model.DoctorInfo;
import com.rmtech.qjys.ui.BaseActivity;
import com.rmtech.qjys.utils.DoctorListManager;
import com.rmtech.qjys.utils.DoctorListManager.OnGetDoctorInfoCallback;
import com.sjl.lib.multi_image_selector.view.SquaredImageView;
import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ChatDetailActivity  extends BaseActivity implements OnClickListener{
	
	// 清空所有聊天记录
		private RelativeLayout clearAllHistory;
		// private RelativeLayout blacklistLayout;
		// private RelativeLayout changeGroupNameLayout;
		// private RelativeLayout idLayout;
		// private TextView idText;
		private EaseSwitchButton switchButton;
		private RelativeLayout rl_switch_block_groupmsg;
		private TextView name_tv;
		private TextView nike_tv;
		private TextView title;
		private TextView left_title;
		private int requestType;
		private SquaredImageView avatar;
		private String toChatUsername;
		private boolean isMian;
		public DoctorInfo doctorInfo;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.qj_chatdetail);
		toChatUsername = getIntent().getStringExtra("toChatUsername");
		title = (TextView) findViewById(R.id.title);
		title.setText("聊天消息设置");
		left_title = (TextView) findViewById(R.id.left_title);
		left_title.setText("返回");
		avatar = (SquaredImageView) findViewById(R.id.avatar);
		avatar.setOnClickListener(this);
		name_tv = (TextView) findViewById(R.id.name_tv);
		nike_tv = (TextView) findViewById(R.id.nike_tv);
		switchButton = (EaseSwitchButton) findViewById(R.id.switch_btn);
		clearAllHistory = (RelativeLayout) findViewById(R.id.clear_all_history);
		clearAllHistory.setOnClickListener(this);
		rl_switch_block_groupmsg = (RelativeLayout) findViewById(R.id.rl_switch_block_groupmsg);
		switchButton = (EaseSwitchButton) findViewById(R.id.switch_btn);
		rl_switch_block_groupmsg.setOnClickListener(this);
		if(toChatUsername!=null){
			loadData(toChatUsername);
		}
//		name_tv.setText(EaseUserUtils.getUserInfo(toChatUsername).getNick()); //应该是备注名
//		nike_tv.setText("昵称：" + EaseUserUtils.getUserInfo(toChatUsername).getNick());
//		ImageLoader.getInstance().displayImage(EaseUserUtils.getUserInfo(toChatUsername).getAvatar(), avatar,
//				QjConstant.optionsHead);
	}

	private void loadData(String toChatUsername) {
		// TODO Auto-generated method stub
		DoctorListManager.getInstance().getDoctorInfoByHXid(toChatUsername, new OnGetDoctorInfoCallback() {
			
			@Override
			public void onGet(DoctorInfo info) {
				if(info != null) {
					doctorInfo = info;
					name_tv.setText(TextUtils.isEmpty(info.name)?info.phone:info.name);
					if(TextUtils.isEmpty(info.remark)) {
						nike_tv.setVisibility(View.GONE);
					} else {
						nike_tv.setText("昵称：" + info.remark);
					}
					ImageLoader.getInstance().displayImage(info.head, avatar,
							QjConstant.optionsHead);
				}
			}
		});
	}
	
	@Override
	public void back(View view) {
		// TODO Auto-generated method stub
		super.back(view);
		finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.clear_all_history:
			String msg = getResources().getString(R.string.Whether_to_empty_all_chats);
	        new EaseAlertDialog(getActivity(),null, msg, null,new AlertDialogUser() {
	            
	            @Override
	            public void onResult(boolean confirmed, Bundle bundle) {
	                if(confirmed){
	                    // 清空会话
	                    EMClient.getInstance().chatManager().deleteConversation(toChatUsername, true);
	                    Toast.makeText(getActivity(), "消息记录已清空", Toast.LENGTH_SHORT).show();
	                }
	            }
	        }, true).show();;
			break;

		case R.id.rl_switch_block_groupmsg:
			if(switchButton.isSwitchOpen()){
				switchButton.closeSwitch();
				//TODO 取消 免打扰
				
			}else {
				switchButton.openSwitch();
				//TODO 设置为 免打扰
				
			}
			break;
		case R.id.avatar:
			UserInfoActivity.show(ChatDetailActivity.this, doctorInfo, "chat");
			break;
		}
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
