package com.rmtech.qjys.model;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class UrlData implements Serializable, Parcelable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2888747968826315025L;
	public String url;// 

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.url);
	}

	public UrlData() {
	}

	protected UrlData(Parcel in) {
		this.url = in.readString();
	}

	public static final Parcelable.Creator<UrlData> CREATOR = new Parcelable.Creator<UrlData>() {
		@Override
		public UrlData createFromParcel(Parcel source) {
			return new UrlData(source);
		}

		@Override
		public UrlData[] newArray(int size) {
			return new UrlData[size];
		}
	};
}
