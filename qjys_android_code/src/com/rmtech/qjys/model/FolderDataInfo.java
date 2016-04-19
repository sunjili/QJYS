package com.rmtech.qjys.model;

import java.io.Serializable;

public class FolderDataInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -889201101804352824L;

	public FolderDataInfo() {
	}

	public String id;

	public String name; // 名称

	public String patient_id;// : 病例
	public String update_time;// : 更新时间
	public String create_time;// : 创建时间

}
