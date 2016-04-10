package com.rmtech.qjys.ui.qjactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.rmtech.qjys.R;
import com.rmtech.qjys.ui.BaseActivity;
/**
 * 性别 界面
 * @author Administrator
 *
 */
public class MeSexActivity extends BaseActivity implements OnClickListener {
	private RelativeLayout ll_man;
	private RelativeLayout ll_woman;
	private ImageView iv_man;
	private ImageView iv_woman;
	private Context context;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_me_sex);
		context = MeSexActivity.this;
		setTitle("性別");
		initView();
	}

	private void initView() {
		ll_man = (RelativeLayout) findViewById(R.id.ll_man);
		ll_man.setOnClickListener(this);
		ll_woman = (RelativeLayout) findViewById(R.id.ll_woman);
		ll_woman.setOnClickListener(this);
		iv_man = (ImageView) findViewById(R.id.iv_man);
		iv_woman = (ImageView) findViewById(R.id.iv_woman);

	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, MeSexActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_man:
			iv_man.setVisibility(View.VISIBLE);
			iv_woman.setVisibility(View.INVISIBLE);

			break;
		case R.id.ll_woman:
			iv_woman.setVisibility(View.VISIBLE);
			iv_man.setVisibility(View.INVISIBLE);

			break;
		default:
			break;
		}
	}
}
