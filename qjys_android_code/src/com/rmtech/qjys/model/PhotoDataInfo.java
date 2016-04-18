package com.rmtech.qjys.model;

import java.io.Serializable;

public class PhotoDataInfo implements Serializable {

	public PhotoDataInfo(int type) {
		this.type = type;
	}

	private static final long serialVersionUID = 1419903223943202259L;

	public static final int STATE_NORMAL = 0;
	public static final int STATE_UPLOADING = 1;
	public static final int STATE_UPLOAD_FAILED = 2;

	public static final int TYPE_IMAGE = 0;
	public static final int TYPE_FOLDER = 1;

	public String id;

	public String name;

	public String url;

	public String localPath;

	public int state = STATE_NORMAL;

	public int type = TYPE_IMAGE;
}
