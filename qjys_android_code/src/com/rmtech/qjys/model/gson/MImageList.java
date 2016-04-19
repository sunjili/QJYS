package com.rmtech.qjys.model.gson;

import java.io.Serializable;
import java.util.List;

import com.rmtech.qjys.model.FolderDataInfo;
import com.rmtech.qjys.model.PhotoDataInfo;

public class MImageList extends MBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6264339021382834212L;
	public ImageDataList data;

	public static class ImageDataList implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 7383864699831028727L;
		public int order;// : 0时间 1名称 2自定义
		public List<FolderDataInfo> folders;// :
		public List<PhotoDataInfo> images;// :

	}
}
