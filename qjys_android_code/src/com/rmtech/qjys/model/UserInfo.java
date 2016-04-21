package com.rmtech.qjys.model;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class UserInfo implements Serializable, Parcelable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8789584433095188767L;
	public String id;// : 用户id
	public String phone;// : 手机号
	public String head;// : 头像
	public String name;// : 姓名
	public int sex;// : 性别
	public String hxpasswd;// : 环信密码
	public String hos_id;// : 医院id
	public String hos_name;// : 医院名称
	public String department;// : 科室
	public boolean passwordFlag;//密码
	public AuthInfo authinfo;

	public static class AuthInfo implements Parcelable {
		public String token;// ：加密串
		public int expire;// : 过期时间 秒
		public String uid;// : 用户id

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(this.token);
			dest.writeInt(this.expire);
			dest.writeString(this.uid);
		}

		public AuthInfo() {
		}

		protected AuthInfo(Parcel in) {
			this.token = in.readString();
			this.expire = in.readInt();
			this.uid = in.readString();
		}

		public static final Parcelable.Creator<AuthInfo> CREATOR = new Parcelable.Creator<AuthInfo>() {
			@Override
			public AuthInfo createFromParcel(Parcel source) {
				return new AuthInfo(source);
			}

			@Override
			public AuthInfo[] newArray(int size) {
				return new AuthInfo[size];
			}
		};
	}

	public String getCookie() {
		if (authinfo != null) {
			StringBuilder cookie = new StringBuilder();
			cookie.append("token=" + authinfo.token + ";");
			cookie.append("expire=" + authinfo.expire + ";");
			cookie.append("uid=" + authinfo.uid);
			return cookie.toString();
		}
		return null;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeString(this.phone);
		dest.writeString(this.head);
		dest.writeString(this.name);
		dest.writeInt(this.sex);
		dest.writeString(this.hxpasswd);
		dest.writeString(this.hos_id);
		dest.writeString(this.hos_name);
		dest.writeString(this.department);
		dest.writeByte(passwordFlag ? (byte) 1 : (byte) 0);
		dest.writeParcelable(this.authinfo, flags);
	}

	public UserInfo() {
	}

	protected UserInfo(Parcel in) {
		this.id = in.readString();
		this.phone = in.readString();
		this.head = in.readString();
		this.name = in.readString();
		this.sex = in.readInt();
		this.hxpasswd = in.readString();
		this.hos_id = in.readString();
		this.hos_name = in.readString();
		this.department = in.readString();
		this.passwordFlag = in.readByte() != 0;
		this.authinfo = in.readParcelable(AuthInfo.class.getClassLoader());
	}

	public static final Parcelable.Creator<UserInfo> CREATOR = new Parcelable.Creator<UserInfo>() {
		@Override
		public UserInfo createFromParcel(Parcel source) {
			return new UserInfo(source);
		}

		@Override
		public UserInfo[] newArray(int size) {
			return new UserInfo[size];
		}
	};
}
