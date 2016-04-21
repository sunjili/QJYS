package com.rmtech.qjys.ui.qjactivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.rmtech.qjys.R;
import com.rmtech.qjys.ui.BaseActivity;

/***
 * 二维码 页面
 * 
 * @author Administrator
 * 
 */
public class MeAboutErweimaActivity extends BaseActivity {
	private EditText et_name;
	private String name;
	private Context context;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_me_about_erweima);
		setTitle("奇迹医生二维码");
		context = MeAboutErweimaActivity.this;
		setRightTitle("分享", new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// TODO 分享
				final Dialog dialog = new Dialog(context, R.style.dialog);
				dialog.setContentView(R.layout.qj_me_dialog);
				dialog.findViewById(R.id.tv_paizhao).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								dialog.cancel();
							}
						});
				dialog.findViewById(R.id.tv_xiangce).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								dialog.cancel();
							}
						});
				dialog.findViewById(R.id.tv_guanliqi).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								dialog.cancel();
							}
						});

				dialog.findViewById(R.id.tv_cancle).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								dialog.cancel();
							}
						});
				dialog.setCancelable(true);
				dialog.create();
				dialog.show();
			}
		});
		initView();
	}

	private void initView() {

	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, MeAboutErweimaActivity.class);
		context.startActivity(intent);
	}
}
