package com.rmtech.qjys.model.gson;

import java.io.Serializable;

import com.rmtech.qjys.model.IdData;

public class MGroupData extends MBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7717436116919112723L;

	public GroupData data;
	
	public static class GroupData implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 6915995185730778000L;
		
		public String group_id;// : 群组id
		public int is_new;// : 上面group_id是否是新建的，0否，1是
		    
	}

}
