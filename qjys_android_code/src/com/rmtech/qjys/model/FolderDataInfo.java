package com.rmtech.qjys.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class FolderDataInfo implements Serializable, Parcelable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -889201101804352824L;

	public FolderDataInfo() {
	}

	public String id;

	public String name; // 名称

	public String patient_id;// : 病例
	public String update_time;// : 更新时间
	public String create_time;// : 创建时间

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeString(this.name);
		dest.writeString(this.patient_id);
		dest.writeString(this.update_time);
		dest.writeString(this.create_time);
	}

	protected FolderDataInfo(Parcel in) {
		this.id = in.readString();
		this.name = in.readString();
		this.patient_id = in.readString();
		this.update_time = in.readString();
		this.create_time = in.readString();
	}

	public static final Creator<FolderDataInfo> CREATOR = new Creator<FolderDataInfo>() {
		@Override
		public FolderDataInfo createFromParcel(Parcel source) {
			return new FolderDataInfo(source);
		}

		@Override
		public FolderDataInfo[] newArray(int size) {
			return new FolderDataInfo[size];
		}
	};
}
