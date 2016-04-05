package com.sjl.lib.http.okhttp;

import android.content.Context;



public class AppGlobalSetting {
	
	public static final String APP_NAME = "qjys";
	public static final String APP_ID = "wxd930ea5d5a258f4f";

	public static final String welcomeVersion = APP_NAME+"_1.0.0";

	/**
	 * 版本号
	 */
	public static int APP_VERSION_CODE = 0;
	public static String APP_VERSION_NAME = "a1.0";
	public static String APP_VERSION = APP_VERSION_NAME + "." + APP_VERSION_CODE;


	private volatile static AppGlobalSetting mInstance = null;

	private AppGlobalSetting() {
	}

	public static AppGlobalSetting getInstance(Context context) {
		if (mInstance == null) {
			synchronized (AppGlobalSetting.class) {
				if (mInstance == null) {
					mInstance = new AppGlobalSetting();
					mInstance.init(context);
				}
			}
		}
		return mInstance;
	}

	private void init(Context context) {
		// TODO Auto-generated method stub
		
	}

	//支持灰度调整版本；
    public static String getVersion(){
    	return AppGlobalSetting.APP_VERSION;
    }


	
	public static String xg_token = "";
	public static final String XG_TOKEN = "xg_token";
	
	
	// TODO --------------------角标显示设置-------------------------
		public static final String APP_PUSH_HINT_STATE = "APP_PUSH_HINT_STATE";
		private static Boolean mIsHintOpen;

//		public boolean getPushHintOpen() {
//			if (null != mIsHintOpen) {
//				return mIsHintOpen;
//			}
//			mIsHintOpen = mSettings.getBoolean(APP_PUSH_HINT_STATE, true);
//			return mIsHintOpen;
//		}
//
//		public void setPushHintOpen(boolean show) {
//			mIsHintOpen = show;
//			Editor editor = mSettings.edit();
//			editor.putBoolean(APP_PUSH_HINT_STATE, show);
//			editor.commit();
//		}
		
		
}
