package com.rmtech.qjys.model.gson;

import java.io.Serializable;
import java.util.List;

import android.os.Parcel;

public class MStateList extends MBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1010390745705838476L;
	public List<StateInfo> data;

	public static class StateInfo implements Serializable, android.os.Parcelable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 2401634159194828551L;
		/**
		 * id : id name : 名称
		 */
		public String name;// : 名称
		public String id;// :id

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(this.name);
			dest.writeString(this.id);
		}

		public StateInfo() {
		}

		protected StateInfo(Parcel in) {
			this.name = in.readString();
			this.id = in.readString();
		}

		public static final Creator<StateInfo> CREATOR = new Creator<StateInfo>() {
			@Override
			public StateInfo createFromParcel(Parcel source) {
				return new StateInfo(source);
			}

			@Override
			public StateInfo[] newArray(int size) {
				return new StateInfo[size];
			}
		};

	}

	public boolean hasData() {
		return data != null && !data.isEmpty();
	}
}
