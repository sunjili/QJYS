/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rmtech.qjys.ui.qjactivity;

import okhttp3.Call;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.rmtech.qjys.QjApplication;
import com.rmtech.qjys.R;
import com.rmtech.qjys.callback.OnLoginListener;
import com.rmtech.qjys.db.DemoDBManager;
import com.rmtech.qjys.hx.QjHelper;
import com.rmtech.qjys.model.UserContext;
import com.rmtech.qjys.model.UserInfo;
import com.rmtech.qjys.model.gson.MUser;
import com.rmtech.qjys.ui.BaseActivity;
import com.rmtech.qjys.ui.MainActivity;
import com.rmtech.qjys.ui.RegisterActivity;
import com.rmtech.qjys.ui.view.LoginBaseView;
import com.rmtech.qjys.ui.view.LoginPassWordView;
import com.rmtech.qjys.ui.view.LoginVcodeView;

/**
 * 登陆页面
 * 
 */
public class QjLoginActivity extends BaseActivity {

	private static final String TAG = QjLoginActivity.class.getSimpleName();
	private boolean autoLogin = false;
	private boolean progressShow;

	private LoginVcodeView mLoginVcodeView;
	private LoginPassWordView mLoginPassWordView;
	private LoginBaseView mCurrentLoginView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_qj_login);
		mLoginPassWordView = (LoginPassWordView) findViewById(R.id.login_pw_layout);
		mLoginVcodeView = (LoginVcodeView) findViewById(R.id.login_vcode_layout);

		mLoginVcodeView.setOnLoginListener(mOnLoginListener);
		mLoginPassWordView.setOnLoginListener(mOnLoginListener);
		mCurrentLoginView = mLoginVcodeView;

	}

	private void onLoginError() {
		Toast.makeText(QjLoginActivity.this, "登录失败!", Toast.LENGTH_LONG).show();
		mCurrentLoginView.logining = false;
		if (mProgressDialog != null && !QjLoginActivity.this.isFinishing() && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}

	private void onLoginSuccess() {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(QjLoginActivity.this, "登录成功!", Toast.LENGTH_LONG).show();
				mCurrentLoginView.logining = false;
				if (mProgressDialog != null && !QjLoginActivity.this.isFinishing() && mProgressDialog.isShowing()) {
					mProgressDialog.dismiss();
				}
				
				//这里有疑问，打开这两个activity的条件分别是什么？
				if (UserContext.getInstance().getUser().isset_passwd!=1){
					Intent intent = new Intent(QjLoginActivity.this, MePasswordNewActivity.class);
					startActivity(intent);
				}else{
					Intent intent = new Intent(QjLoginActivity.this, MainActivity.class);
					startActivity(intent);
				}
				finish();
			}
		});
		
	}

	OnLoginListener mOnLoginListener = new OnLoginListener() {

		@Override
		public void onError(Call call, Exception e) {
			onLoginError();
		}


		@Override
		public void onChange() {

		}

		@Override
		public void onResponseSucces(MUser response) {
			mCurrentLoginView.logining = false;
			if (response.data == null) {
				onLoginError();
			} else {
				onQjLogined(response.data);
			}
		}
	};
	private ProgressDialog mProgressDialog;

	@Override
	protected void onResume() {
		super.onResume();
	}

	protected void onQjLogined(UserInfo userinfo) {
		UserContext.getInstance().setUser(userinfo);
		if (QjHelper.getInstance().isLoggedIn()) {
			autoLogin = true;
			onLoginSuccess();
		} else {

			String currentUsername = userinfo.id;
			String currentPassword = userinfo.hxpasswd;

			if (TextUtils.isEmpty(currentUsername) || TextUtils.isEmpty(currentPassword)) {
				onLoginError();
				Toast.makeText(this, "聊天服务器登录失败！", Toast.LENGTH_SHORT).show();
				return;
			}

			// After logout，the DemoDB may still be accessed due to async
			// callback, so the DemoDB will be re-opened again.
			// close it before login to make sure DemoDB not overlap
			DemoDBManager.getInstance().closeDB();

			// reset current user name before login
			QjHelper.getInstance().setCurrentUserName(currentUsername);

			final long start = System.currentTimeMillis();
			// 调用sdk登陆方法登陆聊天服务器
			Log.d(TAG, "EMClient.getInstance().login");
			EMClient.getInstance().login(currentUsername, currentPassword, new EMCallBack() {

				@Override
				public void onSuccess() {
					Log.d(TAG, "login: onSuccess");

					// ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
					// ** manually load all local groups and
					EMClient.getInstance().groupManager().loadAllGroups();
					EMClient.getInstance().chatManager().loadAllConversations();

					// 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
					boolean updatenick = EMClient.getInstance().updateCurrentUserNick(
							QjApplication.currentUserNick.trim());
					if (!updatenick) {
						Log.e("LoginActivity", "update current user nick fail");
					}
					// 异步获取当前用户的昵称和头像(从自己服务器获取，demo使用的一个第三方服务)
					QjHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();

					// 进入主页面
					onLoginSuccess();
				}

				@Override
				public void onProgress(int progress, String status) {
					Log.d(TAG, "login: onProgress");
				}

				@Override
				public void onError(final int code, final String message) {
					Log.d(TAG, "login: onError: " + code);
					// if (!progressShow) {
					// return;
					// }
					runOnUiThread(new Runnable() {
						public void run() {
							onLoginError();
						}
					});
				}
			});
		}
	}

	public void onChangeClick(View view) {
		if (mLoginVcodeView.getVisibility() == View.VISIBLE) {
			mLoginVcodeView.setVisibility(View.GONE);
			mLoginPassWordView.setVisibility(View.VISIBLE);
			mCurrentLoginView = mLoginPassWordView;
		} else {
			mCurrentLoginView = mLoginVcodeView;
			mLoginVcodeView.setVisibility(View.VISIBLE);
			mLoginPassWordView.setVisibility(View.GONE);
		}
		//LoginPasswordActivity.show(QjLoginActivity.this);
	}

	/**
	 * 登录
	 * 
	 * @param view
	 */
	public void login(View view) {
		if (!EaseCommonUtils.isNetWorkConnected(this)) {
			Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(mCurrentLoginView.login(view)) {
			progressShow = true;
			mProgressDialog = new ProgressDialog(QjLoginActivity.this);
			mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					Log.d("sssssss", "EMClient.getInstance().onCancel");
					progressShow = false;
				}
			});
			mProgressDialog.setMessage(getString(R.string.Is_landing));
			try {
				mProgressDialog.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 注册
	 * 
	 * @param view
	 */
	public void register(View view) {
		startActivityForResult(new Intent(this, RegisterActivity.class), 0);
	}

	public static void show(Context mainActivity) {
		Intent intent = new Intent();
		intent.setClass(mainActivity, QjLoginActivity.class);
		intent .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		mainActivity.startActivity(intent);

	}
}