package com.rmtech.qjys.model.gson;

import android.os.Parcel;

import java.io.Serializable;
import java.util.List;

public class MFlowList extends MBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8355608880185101115L;
	public List<FlowInfo> data;

//	public static class FlowList implements Serializable {
//
//		private static final long serialVersionUID = -2470015029770953699L;
//		public List<FlowInfo> lists;
//	}

	public static class FlowInfo implements Serializable, android.os.Parcelable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -3012551808439160036L;
		public String title;// : 名称
		public String procedure;// : 内容
		public String id;// :id

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(this.title);
			dest.writeString(this.procedure);
			dest.writeString(this.id);
		}

		public FlowInfo() {
		}

		protected FlowInfo(Parcel in) {
			this.title = in.readString();
			this.procedure = in.readString();
			this.id = in.readString();
		}

		public static final Creator<FlowInfo> CREATOR = new Creator<FlowInfo>() {
			@Override
			public FlowInfo createFromParcel(Parcel source) {
				return new FlowInfo(source);
			}

			@Override
			public FlowInfo[] newArray(int size) {
				return new FlowInfo[size];
			}
		};
	}

	public boolean hasData() {
		return data != null && !data.isEmpty();
	}
}
