package com.rmtech.qjys.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

public class CaseInfo implements Serializable, Parcelable {

	/**
	 * name: 姓名 hos_id：医院id sex ：性别 0未知 1男 2女 age : 年龄 hos_id : 医院id department：
	 * 科室 ward_no : 病房号 bed_no : 床位号 diagnose : 诊断结果，如果是多个诊断结果，用&&分割，给客户端显示自行分割
	 * treat_state ： 就诊状态 procedure_title ： 诊疗规范标题 procedure_text ： 诊疗规范内容
	 * abstract ： 摘要 state : 0正常 1临时 返回结构 { ret: 0 msg: 成功 data: { id ： 病例id } }
	 */
	private static final long serialVersionUID = -7846017874522166789L;

	public String id;// : 病例id
	public String group_id;// : 群组id
	public String name;// : 姓名
	public String hos_id;// ：医院id
	public String hos_name;// ：医院名
	public int sex;// ：性别 0未知 1男 2女
	public String age;// : 年龄
	public String department;// ： 科室
	public String ward_no;// : 病房号
	public String bed_no;// : 床位号
	public String diagnose;// : 诊断结果，如果是多个诊断结果，用&&分割，给客户端显示自行分割
	public String treat_state;// ： 就诊状态
	public String procedure_title;// ： 诊疗规范标题
	public String procedure_text;// ： 诊疗规范内容
	public String abs;// ： 摘要
	public int state;// : 0正常 1临时
	public int create_time;// : 病例时间
	public DoctorInfo admin_doctor;// : 管理员
	public List<DoctorInfo> participate_doctor;// : 管理员
	

	public static class DoctorInfo implements Serializable, Parcelable {
		/**
	 * 
	 */
		private static final long serialVersionUID = 5674389516689554457L;
		public String id;// : id
		public String name;// : 姓名

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(this.id);
			dest.writeString(this.name);
		}

		public DoctorInfo() {
		}

		protected DoctorInfo(Parcel in) {
			this.id = in.readString();
			this.name = in.readString();
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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeString(this.group_id);
		dest.writeString(this.name);
		dest.writeString(this.hos_id);
		dest.writeString(this.hos_name);
		dest.writeInt(this.sex);
		dest.writeString(this.age);
		dest.writeString(this.department);
		dest.writeString(this.ward_no);
		dest.writeString(this.bed_no);
		dest.writeString(this.diagnose);
		dest.writeString(this.treat_state);
		dest.writeString(this.procedure_title);
		dest.writeString(this.procedure_text);
		dest.writeString(this.abs);
		dest.writeInt(this.state);
		dest.writeInt(this.create_time);
		dest.writeParcelable(this.admin_doctor, flags);
		dest.writeTypedList(participate_doctor);
	}

	public CaseInfo() {
	}

	protected CaseInfo(Parcel in) {
		this.id = in.readString();
		this.group_id = in.readString();
		this.name = in.readString();
		this.hos_id = in.readString();
		this.hos_name = in.readString();
		this.sex = in.readInt();
		this.age = in.readString();
		this.department = in.readString();
		this.ward_no = in.readString();
		this.bed_no = in.readString();
		this.diagnose = in.readString();
		this.treat_state = in.readString();
		this.procedure_title = in.readString();
		this.procedure_text = in.readString();
		this.abs = in.readString();
		this.state = in.readInt();
		this.create_time = in.readInt();
		this.admin_doctor = in.readParcelable(DoctorInfo.class.getClassLoader());
		this.participate_doctor = in.createTypedArrayList(DoctorInfo.CREATOR);
	}

	public static final Parcelable.Creator<CaseInfo> CREATOR = new Parcelable.Creator<CaseInfo>() {
		@Override
		public CaseInfo createFromParcel(Parcel source) {
			return new CaseInfo(source);
		}

		@Override
		public CaseInfo[] newArray(int size) {
			return new CaseInfo[size];
		}
	};
}
