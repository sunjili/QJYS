package com.rmtech.qjys.ui.qjactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rmtech.qjys.R;
import com.rmtech.qjys.ui.BaseActivity;
import com.umeng.analytics.MobclickAgent;

/***
 * 更换手机号 页面
 * 
 * @author Administrator
 * 
 */
public class MePhoneNewActivity extends BaseActivity implements
		View.OnClickListener {
	private EditText et_phone;
	/**手机号*/
	private String phone;
	private EditText et_verify;
	/**验证码*/
	private String verify;
	private Button btn_get_verify;
	private Context context;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_me_phone_new);
		setTitle("更换手机号");
		setLeftTitle("返回");
		context = MePhoneNewActivity.this;
		setRightTitle("保存", new OnClickListener() {

			@Override
			public void onClick(View v) {
				phone=et_phone.getText().toString().trim();
				verify=et_verify.getText().toString().trim();
				//TODO  校验验证码
				if(!TextUtils.isEmpty(phone)&&!TextUtils.isEmpty(verify)){
					//TODO  变更绑定的手机，需要接口支持、
					
				}else{
					
				}
				
				finish();
			}
		});
		initView();
	}

	private void initView() {
		btn_get_verify = (Button) findViewById(R.id.btn_get_verify);
		btn_get_verify.setOnClickListener(this);
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_verify = (EditText) findViewById(R.id.et_verify);
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, MePhoneNewActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_get_verify:
			timer.start();
			//TODO 获取验证码
			Toast.makeText(context, "获取验证码", Toast.LENGTH_LONG).show();
			break;

		default:
			break;
		}
	}
	CountDownTimer timer = new CountDownTimer(60000, 1000) {
		@Override
		public void onTick(long millisUntilFinished) {
			et_phone.setFocusable(false);
			btn_get_verify.setText((millisUntilFinished / 1000) + "秒后可重发");
			btn_get_verify.setEnabled(false);
		}

		@Override
		public void onFinish() {
			et_phone.setFocusable(true);
			btn_get_verify.setText("获取验证码");
			btn_get_verify.setEnabled(true);
			btn_get_verify
					.setBackgroundResource(R.drawable.qj_me_greenbutton_selector);
		}
	};
	
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
