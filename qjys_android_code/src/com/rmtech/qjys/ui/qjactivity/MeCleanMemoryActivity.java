package com.rmtech.qjys.ui.qjactivity;

import java.io.File;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.hyphenate.util.PathUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rmtech.qjys.R;
import com.rmtech.qjys.hx.QjHelper;
import com.rmtech.qjys.ui.BaseActivity;
import com.sjl.lib.alertview.AlertView;
import com.sjl.lib.filechooser.FileUtils;
import com.sjl.lib.utils.StorageUtils;
import com.umeng.analytics.MobclickAgent;

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
		new LoadDiscTotalUsedSizeTask().execute();
	}
	
	

	private void initView() {
		pm = this.getPackageManager();
		btn_clean = (Button) findViewById(R.id.btn_clean);
		btn_clean.setOnClickListener(this);
		tv_memory_value = (TextView) this.findViewById(R.id.tv_memory_value);
		tv_memory = (TextView) this.findViewById(R.id.tv_memory);
		tv_top = (TextView) this.findViewById(R.id.tv_top);
		tv_memory_value.setText("加载中...");
	}
	
	public void showBytes(long result){
		 
		String resultStr = String.format("%.1fM", result / 1024.0 / 1024.0);

		if(isFromSetting()){
			//TODO 获取病例缓存
			tv_memory_value.setText("(" + "已使用" + resultStr  + ")");
		}else{
			//TODO 获取程序总存储
			tv_memory_value.setText("(" + "已使用" + resultStr  + ")");
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
			new AlertView("确定要清除缓存吗？", null, "取消", null, new String[] { "清除缓存"}, getActivity(),
					AlertView.Style.ActionSheet, new com.sjl.lib.alertview.OnItemClickListener() {

						@Override
						public void onItemClick(Object o, int position) {
							switch (position) {
							case 0:
								if(mProgressDialog != null && mProgressDialog.isShowing()) {
									return;
								}
								mProgressDialog = ProgressDialog.show(getActivity(), null, "正在清理");
								new AsyncTask<Void, Void, Void>() {

									@Override
									protected Void doInBackground(Void... params) {
										
										//PathUtil.getInstance()
//										File file = new File(Environment.getExternalStorageDirectory(), "a.jpg");
//										String filePath = Environment.getExternalStorageDirectory() + "/qjys" + File.separator + "qrQJYSPic" + ".jpg";
//										filePath = getFileRoot() + File.separator  
//								                + "qrImage_" + QjHelper.getInstance().getCurrentUsernName() + ".jpg";
//										PhotoUtil
										
//										final String cacheDir = "/Android/data/" + context.getPackageName()
//												+ "/cache/";
//										return new File(Environment.getExternalStorageDirectory().getPath()
//												+ cacheDir);
//										String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Qrcode/";
//										FileUtils
//									    StorageUtils {

										
										if(isFromSetting()){
											ImageLoader.getInstance().clearMemoryCache();
											ImageLoader.getInstance().clearDiskCache();
											StorageUtils.clearCache();
											StorageUtils.deleteFile(PathUtil.getInstance().getHistoryPath());
									
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
											tv_memory_value.setText("(" + "已使用" + "0.0" + "M" + ")");
											mProgressDialog.dismiss();
										} catch (Exception e) {
											e.printStackTrace();
										}
										super.onPostExecute(result);
									}
									
								}.execute();
								break;
							}

						}
					}).show();
			
			
			break;

		default:
			break;
		}
	}
	

	private class LoadDiscTotalUsedSizeTask extends AsyncTask<Void, Void, Long> {
		protected Long doInBackground(Void... params) {
			try {
				return StorageUtils.getTotalDiscCacheSize();
			} catch (Exception e) {
				
				return 1000000000000L;
			}
		}

		protected void onPostExecute(Long result) {
			showBytes(result);
		}
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
