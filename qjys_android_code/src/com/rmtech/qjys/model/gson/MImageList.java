package com.rmtech.qjys.model.gson;

import java.io.Serializable;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.rmtech.qjys.model.FolderDataInfo;
import com.rmtech.qjys.model.PhotoDataInfo;

public class MImageList extends MBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6264339021382834212L;
	public ImageDataList data;

	public static class ImageDataList implements Serializable, Parcelable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 7383864699831028727L;
		public int order;// : 0时间 1名称 2自定义
		public List<FolderDataInfo> folders;// :
		public List<PhotoDataInfo> images;// :

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeInt(this.order);
			dest.writeTypedList(folders);
			dest.writeTypedList(images);
		}

		public ImageDataList() {
		}

		protected ImageDataList(Parcel in) {
			this.order = in.readInt();
			this.folders = in.createTypedArrayList(FolderDataInfo.CREATOR);
			this.images = in.createTypedArrayList(PhotoDataInfo.CREATOR);
		}

		public static final Parcelable.Creator<ImageDataList> CREATOR = new Parcelable.Creator<ImageDataList>() {
			@Override
			public ImageDataList createFromParcel(Parcel source) {
				return new ImageDataList(source);
			}

			@Override
			public ImageDataList[] newArray(int size) {
				return new ImageDataList[size];
			}
		};
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeParcelable(this.data, flags);
	}

	public MImageList() {
	}

	protected MImageList(Parcel in) {
		super(in);
		this.data = in.readParcelable(ImageDataList.class.getClassLoader());
	}

	public static final Creator<MImageList> CREATOR = new Creator<MImageList>() {
		@Override
		public MImageList createFromParcel(Parcel source) {
			return new MImageList(source);
		}

		@Override
		public MImageList[] newArray(int size) {
			return new MImageList[size];
		}
	};
}
