package com.rmtech.qjys.ui.qjactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.rmtech.qjys.R;
import com.rmtech.qjys.ui.BaseActivity;
import com.rmtech.qjys.ui.WebViewActivity;
import com.rmtech.qjys.ui.view.MeItemLayout;
import com.umeng.analytics.MobclickAgent;

/***
 * 关于奇迹医生 页面
 * 
 * @author Administrator
 * 
 */
public class MeAboutActivity extends BaseActivity implements
		OnClickListener {
	/***  二维码  */
	private MeItemLayout me_about_erweima;
	/***  评分  */
	private MeItemLayout me_about_pingfen;
	/***  简介  */
	private MeItemLayout me_about_jianjie;
	/***  功能简介  */
	private MeItemLayout me_about_gongneng;
	/***  协议  */
	private MeItemLayout me_about_xieyi;
	/***  反馈建议  */
	private MeItemLayout me_about_fankui;
	
	private TextView versioncode;
	
	
	
	private Context context;
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.me_about_erweima:
			jumpActivity(MeAboutErweimaActivity.class);
			break;
		case R.id.me_about_jianjie:
			jumpToWebAty("jianjie", "http://m.qijiyisheng.com/?t=1", WebViewActivity.class);
			break;
//		case R.id.me_about_gongneng:
//			jumpToWebAty("gongneng", "http://www.163.com", WebViewActivity.class);
//			break;
		case R.id.me_about_xieyi:
			jumpToWebAty("xieyi", "http://m.qijiyisheng.com/user_agreement.html", WebViewActivity.class);
			break;
		case R.id.me_about_fankui:
			jumpActivity(MeAboutFankuiActivity.class);
			break;
			
		default:
			break;
		}
	}
	
	public void jumpToWebAty(String tag, String url, Class<?> cls){
		Intent intent = new Intent(context, cls);
		intent.putExtra("from", tag);
		intent.putExtra("url", url);
		startActivity(intent);
	}
	
	private void jumpActivity(Class<?> cls) {
		Intent intent=new Intent(context,cls);
		startActivity(intent);
	}
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_me_about);
		setTitle("关于奇迹医生");
		setLeftTitle("我");
		context = MeAboutActivity.this;
		initView();
	}

	private void initView() {
		me_about_erweima = (MeItemLayout) findViewById(R.id.me_about_erweima);
		me_about_erweima.setOnClickListener(this);
		me_about_jianjie = (MeItemLayout) findViewById(R.id.me_about_jianjie);
		me_about_jianjie.setOnClickListener(this);
//		me_about_gongneng = (MeItemLayout) findViewById(R.id.me_about_gongneng);
//		me_about_gongneng.setOnClickListener(this);
		me_about_xieyi = (MeItemLayout) findViewById(R.id.me_about_xieyi);
		me_about_xieyi.setOnClickListener(this);
		me_about_fankui = (MeItemLayout) findViewById(R.id.me_about_fankui);
		me_about_fankui.setOnClickListener(this);
		versioncode = (TextView) findViewById(R.id.versioncode);
		versioncode.setText("奇迹医生  V" + getVersionName(getActivity()));
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, MeAboutActivity.class);
		context.startActivity(intent);
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
