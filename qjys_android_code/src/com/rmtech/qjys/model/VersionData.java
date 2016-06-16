package com.rmtech.qjys.model;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class VersionData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1366956017782104562L;
	public int version;// version : 版本号
	public String desc;// ： 新版本描述
	public String url;// ： 新版本下载地址

}
