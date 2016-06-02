package com.rmtech.qjys.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

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
	public int update_time;// : 更新时间
	public int create_time;// : 创建时间
	public int image_count;// : 图片数量

	
	public String getUpdateTimeStr() {
		if (update_time > 0) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String timeStr = format.format(new Date(
					update_time * 1000L));
			return timeStr;
		}
		return "";
	}
	public String getCreateTimeStr() {
		if (create_time > 0) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String timeStr = format.format(new Date(
					create_time * 1000L));
			return timeStr;
		}
		return "";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeString(this.name);
		dest.writeString(this.patient_id);
		dest.writeInt(this.update_time);
		dest.writeInt(this.create_time);
		dest.writeInt(this.image_count);
	}

	protected FolderDataInfo(Parcel in) {
		this.id = in.readString();
		this.name = in.readString();
		this.patient_id = in.readString();
		this.update_time = in.readInt();
		this.create_time = in.readInt();
		this.image_count = in.readInt();
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		FolderDataInfo other = (FolderDataInfo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
}
