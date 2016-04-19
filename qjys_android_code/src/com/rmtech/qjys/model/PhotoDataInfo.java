package com.rmtech.qjys.model;

public class PhotoDataInfo extends FolderDataInfo {

	private static final long serialVersionUID = 1419903223943202259L;

	public static final int STATE_NORMAL = 0;
	public static final int STATE_UPLOADING = 1;
	public static final int STATE_UPLOAD_FAILED = 2;

	public String localPath; // 本地路径

	public String thumb_url;// : 图片链接

	public String origin_url;// : 原图

	public String doc_id;// : 上传医生id

	public int create_time;// : 上传时间戳

	public int update_time;// : 更新时间戳

	public int state = STATE_NORMAL; // 上传状态

	public void buildFromOther(PhotoDataInfo data) {
		this.id = data.id;
		this.name = data.name;
		this.thumb_url = data.thumb_url;
		this.origin_url = data.origin_url;
		this.doc_id = data.doc_id;
		this.create_time = data.create_time;
		this.update_time = data.update_time;
	}

}
