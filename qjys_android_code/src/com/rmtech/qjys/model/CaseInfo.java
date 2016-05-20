package com.rmtech.qjys.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.rmtech.qjys.model.gson.MImageList.ImageDataList;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

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
	public String hos_fullname;// ：医院名
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

	public ImageDataList imageDataList; //病例对应的影像资料
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public String getParticipateDoctorIds() {
		if(participate_doctor == null || participate_doctor.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < participate_doctor.size(); i++) {
			DoctorInfo doc = participate_doctor.get(i);
			if(doc == null || TextUtils.equals(doc.id, UserContext.getInstance().getUserId())){
				continue;
			}
			sb.append(doc.id);
			if (i < participate_doctor.size() - 1) {
				sb.append(",");
			}
		}
		return sb.toString();
	}


	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeString(this.group_id);
		dest.writeString(this.name);
		dest.writeString(this.hos_id);
		dest.writeString(this.hos_fullname);
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
		this.hos_fullname = in.readString();
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
		CaseInfo other = (CaseInfo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
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

	public int getGroupCount() {
		if(participate_doctor != null) {
			return participate_doctor.size() + 1;
		}
		return 1;
	}

	public String getShowName() {
		String nameStr = "";
		if (!TextUtils.isEmpty(ward_no)) {
			nameStr = ward_no + "病房 ";
		}
		if (!TextUtils.isEmpty(bed_no)) {
			nameStr += bed_no + "床 ";
		}
		nameStr += name;
		return nameStr;
	}
	
	public boolean hasFlow() {
		return !TextUtils.isEmpty(procedure_title);
	}
}
