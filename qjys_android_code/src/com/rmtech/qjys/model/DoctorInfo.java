package com.rmtech.qjys.model;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class DoctorInfo implements Serializable, Parcelable {

	/**
	 * 'id' : 医生id 'name' : 姓名 'head' : 头像 'phone' : 手机号
	 */
	private static final long serialVersionUID = 7865107684435794820L;

	public String id;
	public String name;
	public String head;
	public String idphone;
	public int sex;// : 性别
	public String hos_id;// : 医院id
	public String hos_fullname;// : 医院名称
	public String department;// : 科室

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeString(this.name);
		dest.writeString(this.head);
		dest.writeString(this.idphone);
		dest.writeInt(this.sex);
		dest.writeString(this.hos_id);
		dest.writeString(this.hos_fullname);
		dest.writeString(this.department);
	}

	public DoctorInfo() {
	}

	protected DoctorInfo(Parcel in) {
		this.id = in.readString();
		this.name = in.readString();
		this.head = in.readString();
		this.idphone = in.readString();
		this.sex = in.readInt();
		this.hos_id = in.readString();
		this.hos_fullname = in.readString();
		this.department = in.readString();
	}

	public static final Parcelable.Creator<DoctorInfo> CREATOR = new Parcelable.Creator<DoctorInfo>() {
		@Override
		public DoctorInfo createFromParcel(Parcel source) {
			return new DoctorInfo(source);
		}

		@Override
		public DoctorInfo[] newArray(int size) {
			return new DoctorInfo[size];
		}
	};
}
