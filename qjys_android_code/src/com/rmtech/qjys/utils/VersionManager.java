package com.rmtech.qjys.utils;

import okhttp3.Call;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

import com.google.gson.Gson;
import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.callback.QjHttpCallback;
import com.rmtech.qjys.model.gson.MVersionData;

public class VersionManager {

	/**
	 * 版本号
	 */
	public static int APP_VERSION_CODE = 1;
	public static String APP_VERSION_NAME = "a1.0";
	public static String APP_VERSION = APP_VERSION_NAME + "."
			+ APP_VERSION_CODE;
	
	private volatile static VersionManager mInstance = null;

	private VersionManager() {
	}

	public static VersionManager getInstance() {
		if (mInstance == null) {
			synchronized (VersionManager.class) {
				if (mInstance == null) {
					mInstance = new VersionManager();
				}
			}
		}
		return mInstance;
	}

	public void init(Context context) {
		loadVersion(context);
	}


	public void checkVersionUpdate(final Context context) {
		QjHttp.appUpdate(new QjHttpCallback<MVersionData>() {

			@Override
			public void onError(Call call, Exception e) {
				
			}

			@Override
			public void onResponseSucces(final MVersionData response) {
				
				if(response != null && response.data != null) {
					if(APP_VERSION_CODE < response.data.version) {
						AlertDialog.Builder builder = new AlertDialog.Builder(context);
						builder.setTitle("版本更新");
						builder.setMessage(response.data.desc).setCancelable(false).setNegativeButton("立即更新", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Uri uri = Uri.parse(response.data.url);    
								Intent it = new Intent(Intent.ACTION_VIEW, uri);    
								context.startActivity(it); 
								dialog.dismiss();
							}
						}).setPositiveButton("稍后再说", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						}).create().show();
					}
				}
				
			}

			@Override
			public MVersionData parseNetworkResponse(String str)
					throws Exception {
				return new Gson().fromJson(str, MVersionData.class);
			}

		});

	}

	public void loadVersion(Context context) {
		try {
			if (null != context) {
				if (null != context.getPackageManager()
						&& null != context.getPackageName()) {
					PackageInfo packageInfo = context.getPackageManager()
							.getPackageInfo(context.getPackageName(), 0);
					if (null != packageInfo) {
						APP_VERSION_NAME = packageInfo.versionName;
						APP_VERSION_CODE = packageInfo.versionCode;
						APP_VERSION = APP_VERSION_NAME + "." + APP_VERSION_CODE;
					}
				}
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
