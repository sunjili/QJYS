/*
 * Copyright (C) 2012 Paul Burke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sjl.lib.filechooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.protobuf.micro.e;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rmtech.qjys.R;

/**
 * List adapter for Files.
 * 
 * @version 2013-12-11
 * @author jilisun
 */
public class FileListAdapter extends BaseAdapter {

	private final static int ICON_FOLDER = R.drawable.ic_folder;
	private final static int ICON_FILE = R.drawable.ic_file;

	private final LayoutInflater mInflater;

	private List<File> mData = new ArrayList<File>();
	private Context mContext;

	public FileListAdapter(Context context) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
	}

	public ArrayList<String> getResultList() {
		return ((FileChooserActivity) mContext).getResultList();
	}

	public void add(File file) {
		mData.add(file);
		notifyDataSetChanged();
	}

	public void remove(File file) {
		mData.remove(file);
		notifyDataSetChanged();
	}

	public void insert(File file, int index) {
		mData.add(index, file);
		notifyDataSetChanged();
	}

	public void clear() {
		mData.clear();
		notifyDataSetChanged();
	}

	@Override
	public File getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	public List<File> getListItems() {
		return mData;
	}

	/**
	 * Set the list items without notifying on the clear. This prevents loss of
	 * scroll position.
	 * 
	 * @param data
	 */
	public void setListItems(List<File> data) {
		mData = data;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		final ViewHolder holder;
		if (row == null) {
			holder = new ViewHolder();
			row = mInflater.inflate(R.layout.file, parent, false);
			holder.view = (TextView) row.findViewById(R.id.textView);
			holder.cbox = (ImageView) row.findViewById(R.id.checkbox);
			holder.icon = (ImageView) row.findViewById(R.id.icon);
			row.setTag(holder);
		}else {
			holder = (ViewHolder) row.getTag();
		}


		// Get the file at the current position
		final File file = getItem(position);
		if (file.isDirectory()) {
			holder.cbox.setVisibility(View.GONE);
		} else {
			if (getResultList().contains(file.getAbsolutePath())) {
				holder.cbox.setImageResource(R.drawable.btn_choice_press);
			} else {
				holder.cbox.setImageResource(R.drawable.btn_choice_nor);
			}
			holder.cbox.setVisibility(View.VISIBLE);
		}

		// Set the TextView as the file name
		holder.view.setText(file.getName());

		// If the item is not a directory, use the file icon
		
		if(file.isDirectory()){
			holder.icon.setImageResource(ICON_FOLDER);
		}else {
			if(FileUtils.isImageFileType(file.getAbsolutePath())){
				// 显示图片
            	DisplayImageOptions options = new DisplayImageOptions.Builder()
        		.showImageForEmptyUri(R.drawable.default_error)
        		.showImageOnFail(R.drawable.default_error)
        		.showImageOnLoading(R.drawable.default_error)
        		//.resetViewBeforeLoading(true)
        		.cacheInMemory(true)
        		.build();
            	
            	ImageLoader.getInstance().displayImage("file://" + file.getAbsolutePath(), holder.icon, options);
			}else {
				holder.icon.setImageResource(ICON_FILE);
			}
		}

		return row;
	}
	
	private static class ViewHolder {
		ImageView icon;
		TextView view;
		ImageView cbox;
	}

}