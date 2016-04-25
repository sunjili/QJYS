package com.rmtech.qjys.model;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class HospitalInfo implements Serializable, Parcelable {

	/**
	 * id: 用户id fullname: 全称 shortname: 简称
	 */
	private static final long serialVersionUID = 7865107684435794820L;

	public String id;
	public String fullname;
	public String shortname;
//	"type":"0","state":"0","create_time"
	public int type;
	public int state;
	public int create_time;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeString(this.fullname);
		dest.writeString(this.shortname);
		dest.writeInt(this.type);
		dest.writeInt(this.state);
		dest.writeInt(this.create_time);
	}

	public HospitalInfo() {
	}

	protected HospitalInfo(Parcel in) {
		this.id = in.readString();
		this.fullname = in.readString();
		this.shortname = in.readString();
		this.type = in.readInt();
		this.state = in.readInt();
		this.create_time = in.readInt();
	}

	public static final Parcelable.Creator<HospitalInfo> CREATOR = new Parcelable.Creator<HospitalInfo>() {
		@Override
		public HospitalInfo createFromParcel(Parcel source) {
			return new HospitalInfo(source);
		}

		@Override
		public HospitalInfo[] newArray(int size) {
			return new HospitalInfo[size];
		}
	};
}
