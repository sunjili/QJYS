package com.sjl.lib.http.okhttp;

import android.os.Build;

public class PhoneProperty {
	private static final String TAG = PhoneProperty.class.getSimpleName();

	public static interface VERSION_CODES {
		// These value are copied from Build.VERSION_CODES
		public static final int FROYO = 8;
		public static final int GINGERBREAD = 9;
		public static final int GINGERBREAD_MR1 = 10;
		public static final int HONEYCOMB = 11;
		public static final int HONEYCOMB_MR1 = 12;
		public static final int HONEYCOMB_MR2 = 13;
		public static final int ICE_CREAM_SANDWICH = 14;
		public static final int ICE_CREAM_SANDWICH_MR1 = 15;
		public static final int JELLY_BEAN = 16;
		public static final int JELLY_BEAN_MR1 = 17;
	}

	private boolean showLog = false;

	// 版本、rom、机型判断
	private String deviceType = null;
	private boolean isAdaptive = false;
	private boolean partMatch = false;

	public static final String MODEL = Build.MODEL.toLowerCase();
	public static final String DEVICE = Build.DEVICE.toLowerCase();
	public static final String MANUFACTURER = Build.MANUFACTURER.toLowerCase();

	private static PhoneProperty phoneProperty = null;

	public static PhoneProperty instance() {
		if (phoneProperty == null) {
			phoneProperty = new PhoneProperty();
		}
		return phoneProperty;
	}

	private PhoneProperty() {
		// 3.0系统平板电脑走2.3流程z
		if (showLog) {
		}
	}

	public String getPhonePropertyMethod(String itemString) {
		return "set" + itemString.substring(0, 1).toUpperCase() + itemString.substring(1);
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
		// 判断是否完全适配列表中机型
		if (deviceType == null) {
			// 非适配机型，关闭一些模块
			isAdaptive = false;
		} else {
			isAdaptive = true;
		}

	}

	// 部分适配策略调整--适配中无适配项的机型放到部分适配列表
	public boolean isAdaptive() {
		return isAdaptive || partMatch;
	}

	public boolean isPartMatch() {
		return partMatch;
	}

	// 部分适配
	public void setPartMatch(boolean partMatch) {
		this.partMatch = partMatch;
	}

	// ///需要添加的适配项

	// 每增加一项需要在这里添加适配项名字和类型
	static String[] BOOL_METHOD_NAMES = new String[] { "CpuArmV5", "unsupportGLES20", "needSetVideoSize",
			"SamsungCamMode", "HTCCamMode", "NotAutoPlay", "FakeArmV5", "NotSupportVideoRecord",
			"UsePictureContinuousAutoFocus", "OnlySupportVideoH264BaseProfile", "VideoUseCamProfileSize",
			"OnlyOneMediaPlayer" };

	private boolean isCpuArmV5 = false;
	private boolean unsupportGLES20 = false;

	// video recording
	private boolean FakeArmV5 = false;

	// isCpuArmV5
	public boolean isCpuArmV5() {
		return isCpuArmV5;
	}

	public void setCpuArmV5(boolean bl) {
		this.isCpuArmV5 = bl;
	}

	public boolean isFakeArmV5() {
		return this.FakeArmV5;
	}

	public void setFakeArmV5(boolean bl) {
		this.FakeArmV5 = bl;
	}

	// unsupportGLES20
	public boolean isUnsupportGLES20() {
		return this.unsupportGLES20;
	}

	public void setUnsupportGLES20(boolean bl) {
		this.unsupportGLES20 = bl;
	}

}
