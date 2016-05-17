
package com.rmtech.qjys.model;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class DoctorInfo implements Serializable, Parcelable {

	/**
	 * 'id' : 医生id 'name' : 姓名 'head' : 头像 'phone' : 手机号
	 */
	private static final long serialVersionUID = 7865107684435794820L;

	public String id;
	public String name;
	public String head;
	public String phone;
	public int sex;// : 性别
	public String hos_id;// : 医院id
	public String hos_fullname;// : 医院名称
	public String department;// : 科室
	public String remark;// : 备注
	public int mostUser;// 常用 1  不常用 0


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DoctorInfo other = (DoctorInfo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeString(this.name);
		dest.writeString(this.head);
		dest.writeString(this.phone);
		dest.writeInt(this.sex);
		dest.writeString(this.hos_id);
		dest.writeString(this.hos_fullname);
		dest.writeString(this.department);
		dest.writeString(this.remark);
		dest.writeInt(this.mostUser);
	}

	public DoctorInfo() {
	}

	protected DoctorInfo(Parcel in) {
		this.id = in.readString();
		this.name = in.readString();
		this.head = in.readString();
		this.phone = in.readString();
		this.sex = in.readInt();
		this.hos_id = in.readString();
		this.hos_fullname = in.readString();
		this.department = in.readString();
		this.remark = in.readString();
		this.mostUser = in.readInt();
	}
	public DoctorInfo(UserInfo user) {
		this.id = user.id;
		this.name = user.name;
		this.head = user.head;
		this.phone = user.phone;
		this.sex = user.sex;
		this.hos_id = user.hos_id;
		this.hos_fullname = user.hos_fullname;
		this.department = user.department;
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

	public boolean isMyself() {
		// TODO Auto-generated method stub
		return TextUtils.equals(id, UserContext.getInstance().getUserId());
	}
}
