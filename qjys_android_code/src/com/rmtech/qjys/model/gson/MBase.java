package com.rmtech.qjys.model.gson;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class MBase implements Serializable, Parcelable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int ret;//
	public String msg;


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.ret);
		dest.writeString(this.msg);
	}

	public MBase() {
	}

	protected MBase(Parcel in) {
		this.ret = in.readInt();
		this.msg = in.readString();
	}

}
