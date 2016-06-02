package com.rmtech.qjys.model;

import android.os.Parcel;

public class PhotoDataInfo extends FolderDataInfo {

	private static final long serialVersionUID = 1419903223943202259L;

	public static final int STATE_NORMAL = 0;
	public static final int STATE_UPLOADING = 1;
	public static final int STATE_UPLOAD_FAILED = 2;

	public String localPath; // 本地路径

	public String thumb_url;// : 图片链接

	public String origin_url;// : 原图

	public String doc_id;// : 上传医生id

	public int state = STATE_NORMAL; // 上传状态

	public void buildFromOther(PhotoDataInfo data) {
		this.id = data.id;
		this.name = data.name;
		this.thumb_url = data.thumb_url;
		this.origin_url = data.origin_url;
		this.doc_id = data.doc_id;
		this.create_time = data.create_time;
		this.update_time = data.update_time;
		this.image_count = data.image_count;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(this.localPath);
		dest.writeString(this.thumb_url);
		dest.writeString(this.origin_url);
		dest.writeString(this.doc_id);
		dest.writeInt(this.state);
	}

	public PhotoDataInfo() {
	}

	protected PhotoDataInfo(Parcel in) {
		super(in);
		this.localPath = in.readString();
		this.thumb_url = in.readString();
		this.origin_url = in.readString();
		this.doc_id = in.readString();
		this.state = in.readInt();
	}

	public static final Creator<PhotoDataInfo> CREATOR = new Creator<PhotoDataInfo>() {
		@Override
		public PhotoDataInfo createFromParcel(Parcel source) {
			return new PhotoDataInfo(source);
		}

		@Override
		public PhotoDataInfo[] newArray(int size) {
			return new PhotoDataInfo[size];
		}
	};
}
