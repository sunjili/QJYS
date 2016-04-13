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
package com.rmtech.qjys;

import java.util.concurrent.TimeUnit;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.rmtech.qjys.hx.QjHelper;
import com.sjl.lib.http.okhttp.AndroidDeviceManager;
import com.sjl.lib.http.okhttp.OkHttpUtils;
import com.sjl.lib.multi_image_selector.utils.ScreenUtils;
import com.sjl.lib.utils.ScreenUtil;

public class QjApplication extends Application {

	public static Context applicationContext;
	private static QjApplication instance;
	// login user name
	public final String PREF_USERNAME = "username";
	
	/**
	 * 当前用户nickname,为了苹果推送不是userid而是昵称
	 */
	public static String currentUserNick = "";

	@Override
	public void onCreate() {
		MultiDex.install(this);
		super.onCreate();
        applicationContext = this;
        instance = this;
		AndroidDeviceManager.Instance().initialize(getApplicationContext());
		OkHttpUtils.getInstance().debug("OkHttpUtils").setConnectTimeout(100000, TimeUnit.MILLISECONDS);
        //使用https，但是默认信任全部证书
        OkHttpUtils.getInstance().setCertificates();
        //init demo helper
        QjHelper.getInstance().init(applicationContext);
        initImageLoader(getApplicationContext());
        ScreenUtil.init(applicationContext);
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024)
				// 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				//.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	public static QjApplication getInstance() {
		return instance;
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
}
