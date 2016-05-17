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
		 * 05-17 23:13:48.960: E/QjHttpCallback.java(26179):
		 * [parseNetworkResponse:21]response =
		 * {"ret":0,"msg":"\u6210\u529f","data"
		 * :[{"id":"294","doc_id":"101","name"
		 * :"\u674e\u7ecf","state":"0","create_time"
		 * :"1462611480"},{"id":"295","doc_id"
		 * :"101","name":"\u674e\u7ecf\u7406",
		 * "state":"0","create_time":"1462611519"
		 * },{"id":"296","doc_id":"101","name"
		 * :"LOL","state":"0","create_time":"1462614479"
		 * },{"id":"297","doc_id":"101"
		 * ,"name":"LOL\u4e86","state":"0","create_time"
		 * :"1462614516"},{"id":"298"
		 * ,"doc_id":"101","name":"LOL\u4e86\u8bb0\u5f55"
		 * ,"state":"0","create_time"
		 * :"1462614541"},{"id":"371","doc_id":"101","name"
		 * :"\u54e6\u54df","state"
		 * :"0","create_time":"1463497473"},{"id":"372","doc_id"
		 * :"101","name":"\u6211\u5c4b"
		 * ,"state":"0","create_time":"1463497490"}]}
		 */
		public String name;// : 名称
		public String doc_id;
		public int state;
		public int create_time;
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
