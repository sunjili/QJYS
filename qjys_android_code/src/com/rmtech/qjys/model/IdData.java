package com.rmtech.qjys.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class IdData implements Serializable, Parcelable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4448406878537125175L;
	public String id;// : 用户id

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
	}

	public IdData() {
	}

	protected IdData(Parcel in) {
		this.id = in.readString();
	}

	public static final Parcelable.Creator<IdData> CREATOR = new Parcelable.Creator<IdData>() {
		@Override
		public IdData createFromParcel(Parcel source) {
			return new IdData(source);
		}

		@Override
		public IdData[] newArray(int size) {
			return new IdData[size];
		}
	};
}
