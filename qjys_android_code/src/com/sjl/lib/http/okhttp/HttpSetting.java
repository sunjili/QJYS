package com.sjl.lib.http.okhttp;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.rmtech.qjys.model.UserContext;

public class HttpSetting {

	public static int TIME_OUT_SPAN = 25000;
	public static final String REFER_URL = "http://qijiyisheng.com";

	// 正式环境
	public static final String BASE_URL = "http://101.200.130.188";

	// 测试环境
	public static final String TEST_BASE_URL = "http://101.200.130.188";

	public static String AGENT = "qijiyisheng/" + AppGlobalSetting.APP_VERSION_NAME + "(" + Build.MANUFACTURER + ";"
			+ Build.MODEL + ";" + "os" + Build.VERSION.RELEASE + ";" + AndroidDeviceManager.getCpuType() + ";"
			+ AppGlobalSetting.APP_VERSION_CODE + ")".replaceAll(" ", "");

	public enum RequestType {
		GET, POST
	}

	public static void refreshAgent() {
		AGENT = "qijiyisheng/" + AppGlobalSetting.APP_VERSION_NAME + "(" + Build.MANUFACTURER + ";" + Build.MODEL + ";"
				+ "os" + Build.VERSION.RELEASE + ";" + AndroidDeviceManager.getCpuType() + ";"
				+ AppGlobalSetting.APP_VERSION_CODE + ")".replaceAll(" ", "");
	}

	public static void addHttpParams(HashMap<String, String> params, String url) {
		if (params == null) {
			return;
		}
		Long time = System.currentTimeMillis() / 1000;
		String sTime = String.valueOf(time);

		params.put("g_tk", getTK(url, sTime));
		params.put("t", sTime);
		params.put("g_net", AndroidDeviceManager.Instance().getNetAPN());
		params.put("v", AppGlobalSetting.getVersion());
		params.put("w_tk", getWTK()); // 写操作
		params.put("did", AndroidDeviceManager.Instance().getIMEI());

	}

	public static void addHttpHeader(HashMap<String, String> headers) {
		headers.put("Referer", HttpSetting.REFER_URL);
		headers.put("User-Agent", HttpSetting.AGENT);
		if (!TextUtils.isEmpty(UserContext.getInstance().getCookie())) {
			headers.put("Cookie", UserContext.getInstance().getCookie());
		}
	}

	private static String getWTK() {
		return "";
	}

	private static String getTK(String url, String time) {
		// md5('sanyou__asdfghty__/station/detail__12344444444__zhongchuang')
		StringBuilder sb = new StringBuilder();
		sb.append("qiji__").append(AndroidDeviceManager.Instance().getIMEI());
		sb.append("__");
		sb.append(url);
		sb.append("__");
		sb.append(time);
		sb.append("__yisheng");
		String md5_value = md5(sb.toString().toLowerCase());
		Log.d("sssssssssssssss", "getTK=" + sb);
		return md5_value;
	}

	public static String md5(String msg) {
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(msg.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Huh, MD5 should be supported?", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Huh, UTF-8 should be supported?", e);
		}

		try {
			StringBuilder hex = new StringBuilder(hash.length * 2);
			for (byte b : hash) {
				if ((b & 0xFF) < 0x10)
					hex.append("0");
				hex.append(Integer.toHexString(b & 0xFF));
			}
			return hex.toString();
		} catch (Error e) {
			return "";
		}
	}

}
