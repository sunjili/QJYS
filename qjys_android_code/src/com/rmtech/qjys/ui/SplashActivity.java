package com.rmtech.qjys.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.rmtech.qjys.R;
import com.rmtech.qjys.callback.QjHttpCallbackNoParse;
import com.rmtech.qjys.hx.QjHelper;
import com.rmtech.qjys.model.UserContext;
import com.rmtech.qjys.model.gson.MDoctorList;
import com.rmtech.qjys.ui.qjactivity.QjLoginActivity;
import com.rmtech.qjys.utils.DoctorListManager;
import com.rmtech.qjys.utils.GroupAndCaseListManager;

/**
 * 开屏页
 *
 */
public class SplashActivity extends BaseActivity {
	private RelativeLayout rootLayout;
	private TextView versionText;
	
	private static final int sleepTime = 2000;

	@Override
	protected void onCreate(Bundle arg0) {
		setContentView(R.layout.em_activity_splash);
		super.onCreate(arg0);

		rootLayout = (RelativeLayout) findViewById(R.id.splash_root);
		versionText = (TextView) findViewById(R.id.tv_version);

		versionText.setText(getVersion());
		AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
		animation.setDuration(1500);
		rootLayout.startAnimation(animation);
	}

	@Override
	protected void onStart() {
		super.onStart();

		new Thread(new Runnable() {
			public void run() {
				UserContext.getInstance().loadCookie();
				if(!TextUtils.isEmpty(UserContext.getInstance().getCookie()) && QjHelper.getInstance().isLoggedIn()) {
					
					DoctorListManager.getInstance().getDoctorList(true, null);

					// ** 免登陆情况 加载所有本地群和会话
					//不是必须的，不加sdk也会自动异步去加载(不会重复加载)；
					//加上的话保证进了主页面会话和群组都已经load完毕
					long start = System.currentTimeMillis();
					EMClient.getInstance().groupManager().loadAllGroups();
					EMClient.getInstance().chatManager().loadAllConversations();
					long costTime = System.currentTimeMillis() - start;
					//等待sleeptime时长
					if (sleepTime - costTime > 0) {
						try {
							Thread.sleep(sleepTime - costTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					//进入主页面
					startActivity(new Intent(SplashActivity.this, MainActivity.class));
					finish();
				}else {
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
					}
//					startActivity(new Intent(SplashActivity.this, QjLoginActivity.class));
					QjLoginActivity.show(SplashActivity.this);
					finish();
				}
			}
		}).start();

	}
	
	/**
	 * 获取当前应用程序的版本号
	 */
	private String getVersion() {
	    return EMClient.getInstance().getChatConfig().getVersion();
	}
}
