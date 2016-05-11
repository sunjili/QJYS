package com.rmtech.qjys.ui.qjactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import android.widget.Toast;

import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.R;
import com.rmtech.qjys.callback.BaseModelCallback;
import com.rmtech.qjys.model.UserContext;
import com.rmtech.qjys.model.gson.MBase;
import com.rmtech.qjys.ui.BaseActivity;
import com.rmtech.qjys.ui.MainActivity;

/***
 * 设置新密码 页面
 * 
 * @author Administrator
 * 
 */
public class MePasswordNewActivity extends BaseActivity implements
		View.OnClickListener {
	private EditText et_password_1;
	private EditText et_password_2;
	private Button btn_set_password;
	private TextView tv_top;
	private TextView tv_bottom;
	private LinearLayout ll_bottom;
	/**密码1*/
	private String password1;
	/**确认密码*/
	private String password2;
	private Context context;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_me_password_new);
		initView();
		context = MePasswordNewActivity.this;
		if(MePasswordNewActivity.this.getIntent()!=null &&
				MePasswordNewActivity.this.getIntent().getIntExtra("isMe", 0)==1){
			tv_top.setVisibility(View.VISIBLE);
			tv_bottom.setVisibility(View.GONE);
			ll_bottom.setVisibility(View.GONE);
			et_password_1.setHint("请设置奇迹医生密码");
			et_password_2.setHint("请再次输入密码");
			btn_set_password.setVisibility(View.GONE);
			setTitle("设置密码");
			setRightTitle("完成", new OnClickListener() {

				@Override
				public void onClick(View v) {
					password1=et_password_1.getText().toString().trim();
					password2=et_password_2.getText().toString().trim();
					if (!TextUtils.isEmpty(password1)&&!TextUtils.isEmpty(password2)&&password1.equals(password2)) {
						//  保存新密码到服务器
						QjHttp.setPassword(password1, new BaseModelCallback() {
							
							@Override
							public void onResponseSucces(MBase response) {
								// TODO 改变UserInfo中的isset_passwd值？？怎么改
								
								Intent data=new Intent();
								data.putExtra("boolean", true);
								setResult(MeNameActivity.RESULT_OK, data);
								finish();
							}
							
							@Override
							public void onError(Call call, Exception e) {
								// TODO Auto-generated method stub
								Toast.makeText(MePasswordNewActivity.this, "密码设置失败!", Toast.LENGTH_LONG).show();
							}
						});
					}
				}
			});
		}else{
			tv_top.setVisibility(View.GONE);
			tv_bottom.setVisibility(View.VISIBLE);
			ll_bottom.setVisibility(View.VISIBLE);
			et_password_1.setHint("输入登录密码");
			et_password_2.setHint("重复输入密码");
			btn_set_password.setVisibility(View.VISIBLE);
			setTitle("奇迹医生-登录");
			setLeftGone();
			setRightTitle("跳过", new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MePasswordNewActivity.this, MainActivity.class);
					startActivity(intent);
					finish();
				}
			});
		}
	}

	private void initView() {
		et_password_1 = (EditText) findViewById(R.id.et_password_1);
		et_password_2 = (EditText) findViewById(R.id.et_password_2);
		btn_set_password = (Button) findViewById(R.id.btn_set_password);
		btn_set_password.setOnClickListener(this);
		tv_top = (TextView) findViewById(R.id.tv_top);
		tv_bottom = (TextView) findViewById(R.id.tv_bottom);
		ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, MePasswordNewActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.btn_set_password:
			password1=et_password_1.getText().toString().trim();
			password2=et_password_2.getText().toString().trim();
			if (!TextUtils.isEmpty(password1)&&!TextUtils.isEmpty(password2)&&password1.equals(password2)) {
				// 保存新密码到服务器
				QjHttp.setPassword(password1, new BaseModelCallback() {
					
					@Override
					public void onResponseSucces(MBase response) {
						// TODO 改变UserInfo中的isset_passwd值
						
						
						Intent data=new Intent();
						data.putExtra("boolean", true);
						setResult(MeNameActivity.RESULT_OK, data);
						finish();
					}
					
					@Override
					public void onError(Call call, Exception e) {
						// TODO Auto-generated method stub
						Toast.makeText(MePasswordNewActivity.this, "密码设置失败!", Toast.LENGTH_LONG).show();
					}
				});
				Intent intent = new Intent(MePasswordNewActivity.this, MainActivity.class);
				startActivity(intent);
				
				finish();
			}
			break;
		}
	}
}
