package com.rmtech.qjys.ui.qjactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;
import okhttp3.Call;

import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.R;
import com.rmtech.qjys.callback.BaseModelCallback;
import com.rmtech.qjys.model.UserContext;
import com.rmtech.qjys.model.gson.MBase;
import com.rmtech.qjys.ui.BaseActivity;
import com.rmtech.qjys.utils.PasswordTextWatcher;
import com.umeng.analytics.MobclickAgent;

/***
 * 设置新密码 页面
 * 
 * @author Administrator
 * 
 */
public class MePasswordChangeActivity extends BaseActivity implements
		View.OnClickListener {
	private EditText et_password_old;
	private EditText et_password_new_1;
	private EditText et_password_new_2;
	/**登录密码*/
	private String password_old;
	/**新密码*/
	private String password1;
	/**确认新密码*/
	private String password2;
	private Context context;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_me_password_change);
		setTitle("设置密码");
		setLeftTitle("我");
		context = MePasswordChangeActivity.this;
		setRightTitle("完成", new OnClickListener() {

			@Override
			public void onClick(View v) {
			
				password_old=et_password_old.getText().toString().trim();
				password1=et_password_new_1.getText().toString().trim();
				password2=et_password_new_2.getText().toString().trim();
				if(password_old.length()<8){
					Toast.makeText(MePasswordChangeActivity.this, "请输入完整的密码！", Toast.LENGTH_LONG).show();
					return;
				}
				if(!password1.equals(password2)){
					Toast.makeText(MePasswordChangeActivity.this, "两次输入的密码不相同！", Toast.LENGTH_LONG).show();
					return;
				}
				if(password1.length()==0||password2.length()==0){
					Toast.makeText(MePasswordChangeActivity.this, "输入的密码不能为空！", Toast.LENGTH_LONG).show();
					return;
				}
				if (!TextUtils.isEmpty(password1)&&!TextUtils.isEmpty(password2)&&password1.equals(password2)
						&&password1.length()>=8) {
					//  保存新密码到服务器
					QjHttp.resetPassword(password_old, password1, new BaseModelCallback() {
						
						@Override
						public void onResponseSucces(MBase response) {
							// TODO 改变UserInfo中的isset_passwd值？？怎么改
							
							Intent data=new Intent();
							data.putExtra("boolean", true);
							setResult(MeNameActivity.RESULT_OK, data);
							UserContext.getInstance().setPasswordFlag(true);
							finish();
						}
						
						@Override
						public void onError(Call call, Exception e) {
							// TODO Auto-generated method stub
							Toast.makeText(MePasswordChangeActivity.this, "密码设置失败!", Toast.LENGTH_LONG).show();
						}
					});
				}else if (!TextUtils.isEmpty(password1)&&!TextUtils.isEmpty(password2)&&password1.equals(password2)
						&&password1.length() < 8){
					Toast.makeText(MePasswordChangeActivity.this, "您输入的新密码不足8位，请重新输入!", Toast.LENGTH_LONG).show();
					et_password_new_1.setText("");
					et_password_new_2.setText("");
				}
			}
		});
		initView();
	}

	private void initView() {
		et_password_old = (EditText) findViewById(R.id.et_password_old);
		setTextWhacher(MePasswordChangeActivity.this, et_password_old, 16);
		et_password_new_1 = (EditText) findViewById(R.id.et_password_new_1);
		setTextWhacher(MePasswordChangeActivity.this, et_password_new_1, 16);
		et_password_new_2 = (EditText) findViewById(R.id.et_password_new_2);
		setTextWhacher(MePasswordChangeActivity.this, et_password_new_2, 16);
		et_password_new_1.addTextChangedListener(new PasswordTextWatcher(et_password_new_1, getActivity()) {
			
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                super.afterTextChanged(s);
            }
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
                // TODO Auto-generated method stub
                super.beforeTextChanged(s, start, count, after);
            }
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                // TODO Auto-generated method stub
                super.onTextChanged(s, start, before, count);
            }
		});
		et_password_new_2.addTextChangedListener(new PasswordTextWatcher(et_password_new_2, getActivity()) {
			
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                super.afterTextChanged(s);
            }
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
                // TODO Auto-generated method stub
                super.beforeTextChanged(s, start, count, after);
            }
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                // TODO Auto-generated method stub
                super.onTextChanged(s, start, before, count);
            }
		});

	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, MePasswordChangeActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
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
