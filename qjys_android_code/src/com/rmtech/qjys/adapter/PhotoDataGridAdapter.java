package com.rmtech.qjys.adapter;

import java.util.HashSet;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rmtech.qjys.R;
import com.rmtech.qjys.model.FolderDataInfo;
import com.rmtech.qjys.model.PhotoDataInfo;
import com.sjl.lib.dynamicgrid.BaseDynamicGridAdapter;

public class PhotoDataGridAdapter extends BaseDynamicGridAdapter {

	public static final int SHOW_TYPE_NORMAL = 0;
	public static final int SHOW_TYPE_SELECT = 1;

	private boolean debug = false;

	private int showType = SHOW_TYPE_NORMAL;
	private HashSet<PhotoDataInfo> mSelectedImages;

	public PhotoDataGridAdapter(Context context, List<?> items,  HashSet<PhotoDataInfo> mSelectedImages, int type) {
		super(context, items, context.getResources().getInteger(R.integer.column_count));
		showType = type;
		this.mSelectedImages = mSelectedImages;
	}

	public PhotoDataGridAdapter(Context context, List<?> items) {
		super(context, items, context.getResources().getInteger(R.integer.column_count));
	}

	// public void add(int position, FolderDataInfo info) {
	// mList.add(position, info);
	// }
	//
	// public void add(PhotoDataInfo info) {
	// mList.add(info);
	// }

	@Override
	public int getCount() {
		if (debug) {
			return 20;
		}
		return super.getCount();
	}

	@Override
	public void notifyDataSetChanged() {

		super.notifyDataSetChanged();
	}

	@Override
	public int getItemViewType(int position) {
		if (debug) {
			return 0;
		}
		if (getItem(position) instanceof PhotoDataInfo) {
			return 1;
		}
		return 0;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		FolderDataInfo info = (FolderDataInfo) getItem(position);
		if (convertView == null) {
			if (getItemViewType(position) == 1) {
				convertView = LayoutInflater.from(parent.getContext())
						.inflate(R.layout.item_grid_photodata_image, null);

			} else {
				convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_photodata_folder,
						null);

			}
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.build(info);
		return convertView;
	}

	DisplayImageOptions options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.default_error)
			.showImageOnFail(R.drawable.default_error).showImageOnLoading(R.drawable.default_error).resetViewBeforeLoading(true).cacheOnDisk(true)
			.cacheInMemory(true).build();
	
	private class ViewHolder {
		public ImageView itemImg;
		public ImageView checkmark;
		public TextView itemName;
		

		public ViewHolder(View container) {
			itemImg = (ImageView) container.findViewById(R.id.item_img);
			checkmark = (ImageView) container.findViewById(R.id.checkmark);
			itemName = (TextView) container.findViewById(R.id.item_name);
		}

		public void build(FolderDataInfo data) {
			if (debug) {
				itemImg.setImageResource(R.drawable.ic_launcher);
				itemName.setText("ssssssss");
				return;
			}
			if (data instanceof PhotoDataInfo) {
				String localPath = ((PhotoDataInfo) data).localPath;
				if (TextUtils.isEmpty(localPath)) {
					ImageLoader.getInstance().displayImage(((PhotoDataInfo) data).thumb_url, itemImg, options);
				} else {
					ImageLoader.getInstance().displayImage("file://" + ((PhotoDataInfo) data).localPath, itemImg,
							options);
				}
				
				if(showType == SHOW_TYPE_SELECT) {
					checkmark.setVisibility(View.VISIBLE);
					if(mSelectedImages != null) {
						if(mSelectedImages.contains(data)) {
							checkmark.setImageResource(R.drawable.btn_choice_press);
						} else {
							checkmark.setImageResource(R.drawable.btn_choice_nor);
						}
					}
					
				}
			}
			itemName.setText(data.name);
			
			
		}
	}

	// public void addAll(int position, List<FolderDataInfo> folders) {
	// mList.addAll(position, folders);
	//
	// }
	//
	// public void addAll(List<PhotoDataInfo> images) {
	// mList.addAll(images);
	// }

}
