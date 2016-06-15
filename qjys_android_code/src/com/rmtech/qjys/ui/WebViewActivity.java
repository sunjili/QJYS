package com.rmtech.qjys.ui;

import com.google.protobuf.micro.c;
import com.rmtech.qjys.R;
import com.rmtech.qjys.ui.qjactivity.MeAboutActivity;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
public class WebViewActivity extends BaseActivity{
	
	public Context context;
	public WebView webView;
	public String from;
	public String url;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.qj_webview);
		setLeftTitle("返回");
		initView();
		context = WebViewActivity.this;
		from = getIntent().getStringExtra("from");
		url = getIntent().getStringExtra("url");
		if(from.equals("jianjie")){
			setTitle("奇迹医生简介");
		}else if(from.equals("gongneng")){
			setTitle("功能简介");
		}else{
			setTitle("用户使用协议");
		}
		loading(url);
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		webView = (WebView) findViewById(R.id.webview);
		webView.setScrollBarStyle(0);//滚动条风格，为0指滚动条不占用空间，直接覆盖在网页上 
		WebSettings setting = webView.getSettings();  
		setting.setJavaScriptEnabled(true);
		setting.setDefaultTextEncodingName("GBK");//设置字符编码，支持中文
	}
	
	public void loading(String url){
		webView.loadUrl(url);
		webView.setWebViewClient(new WebViewClient(){
	           @Override
	        public boolean shouldOverrideUrlLoading(WebView view, String url) {
	            // TODO Auto-generated method stub
	               //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
	            view.loadUrl(url);
	            return true;
	        }
	    });
	}

	@Override
	protected boolean showTitleBar() {
		// TODO Auto-generated method stub
		return true;
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
