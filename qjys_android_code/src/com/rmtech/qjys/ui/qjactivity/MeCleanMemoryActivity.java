package com.rmtech.qjys.ui.qjactivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.rmtech.qjys.R;
import com.rmtech.qjys.ui.BaseActivity;

/***
 * 清理內存 页面
 * 
 * @author Administrator
 * 
 */
public class MeCleanMemoryActivity extends BaseActivity implements
		OnClickListener {
	private Button btn_clean;
	private Context context;
	private TextView tv_memory_value;
	private TextView tv_memory;
	private PackageManager pm;
	private TextView tv_top;
	private ProgressDialog mProgressDialog;
	

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_me_clean_memory);
		setTitle("清理存储空间");
		setLeftTitle("我");
		context = MeCleanMemoryActivity.this;
		initView();
		if(isFromSetting()){
			setTitle("本地控件清理");
			setLeftTitle("返回");
			tv_memory.setText("缓存");
			tv_top.setText("");
			btn_clean.setText("清空缓存");
		}
		showBytes();
	}
	
	

	private void initView() {
		pm = this.getPackageManager();
		btn_clean = (Button) findViewById(R.id.btn_clean);
		btn_clean.setOnClickListener(this);
		tv_memory_value = (TextView) this.findViewById(R.id.tv_memory_value);
		tv_memory = (TextView) this.findViewById(R.id.tv_memory);
		tv_top = (TextView) this.findViewById(R.id.tv_top);
//		tv_memory_value.setText("");
	}
	
	public void showBytes(){
		if(isFromSetting()){
			//TODO 获取病例缓存
			tv_memory_value.setText("(" + "已使用" + "123.4" + "M" + ")");
		}else{
			//TODO 获取程序总存储
			
			tv_memory_value.setText("(" + "已使用" + "123.4" + "M" + ")");
		}
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, MeCleanMemoryActivity.class);
		context.startActivity(intent);
	}

	private boolean isFromSetting() {
		return "PhotoDataSettingActivity".equals(getIntent().getStringExtra("from"));
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_clean:
			if(mProgressDialog != null && mProgressDialog.isShowing()) {
				return;
			}
			mProgressDialog = ProgressDialog.show(getActivity(), null, "正在清理");
			new AsyncTask<Void, Void, Void>() {

				@Override
				protected Void doInBackground(Void... params) {
					if(isFromSetting()){
						ImageLoader.getInstance().clearMemoryCache();
						ImageLoader.getInstance().clearDiskCache();
					}else{
						ImageLoader.getInstance().clearMemoryCache();
						ImageLoader.getInstance().clearDiskCache();
					}
					return null;
				}

				@Override
				protected void onPreExecute() {
					super.onPreExecute();
				}

				@Override
				protected void onPostExecute(Void result) {
					// TODO Auto-generated method stub
					try {
						tv_memory_value.setText("(" + "已使用" + "0" + "M" + ")");
						mProgressDialog.dismiss();
					} catch (Exception e) {
						e.printStackTrace();
					}
					super.onPostExecute(result);
				}
				
				
			}.execute();
			
			break;

		default:
			break;
		}
	}
}
