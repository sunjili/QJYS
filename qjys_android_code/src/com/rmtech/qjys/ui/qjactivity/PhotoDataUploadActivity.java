package com.rmtech.qjys.ui.qjactivity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rmtech.qjys.R;
import com.rmtech.qjys.model.PhotoDataInfo;
import com.sjl.lib.utils.L;

public class PhotoDataUploadActivity extends PhotoDataBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qj_photo_upload);
		setTitle("上传影像资料");
		setRightTitle(R.drawable.btn_case_newfolder, new OnClickListener() {

			@Override
			public void onClick(View v) {
				showNewFolderDialog();
			}
		});
		initViews();
	}

	public static void show(Activity context) {
		Intent intent = new Intent();
		intent.setClass(context, PhotoDataUploadActivity.class);
		context.startActivity(intent);
	}

	protected Context mContext;
	protected GridView mGridView;
	protected View nodata_layout;
	private ArrayList<PhotoDataInfo> mDataList;

	private SelectGridAdapter mAdapter;

	private void initViews() {
		initPhotoSelector();
		mContext = this;
		mGridView = (GridView) findViewById(R.id.dynamic_grid);
		nodata_layout = findViewById(R.id.nodata_layout);
		mDataList = new ArrayList<>();
		mAdapter = new SelectGridAdapter();
		mGridView.setAdapter(mAdapter);

		mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				// selectImageFromGrid(i);
			}

		});

	}

	@Override
	public void onAddNewFolder(String name) {
		PhotoDataInfo info = new PhotoDataInfo(PhotoDataInfo.TYPE_FOLDER);
		info.name = name;
		mDataList.add(0, info);
		mAdapter.notifyDataSetChanged();
	}

	protected void onImagePicked(List<String> paths) {
		mAdapter.addImages(paths);
	}

	public class SelectGridAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mDataList.size();
		}

		public void addImages(List<String> paths) {
			for (String path : paths) {
				PhotoDataInfo info = new PhotoDataInfo(PhotoDataInfo.TYPE_IMAGE);
				info.localPath = path;
				L.e("localPath = " + path);

				int index = path.lastIndexOf('/');
				info.name = path.substring(index+1, path.length());
				L.e("info.name = " + info.name);
				info.state = PhotoDataInfo.STATE_UPLOADING;
				mDataList.add(info);
			}
			notifyDataSetChanged();

		}

		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
			if (getCount() > 0) {
				if (nodata_layout.getVisibility() == View.VISIBLE) {
					nodata_layout.setVisibility(View.GONE);
				}
			} else {
				nodata_layout.setVisibility(View.VISIBLE);
			}

		}

		@Override
		public PhotoDataInfo getItem(int position) {
			return mDataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public int getItemViewType(int position) {
			return getItem(position).type;
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			PhotoDataInfo info = getItem(position);
			if (convertView == null) {
				if (info.type == PhotoDataInfo.TYPE_IMAGE) {
					convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_grid_photodata_image, null);

				} else {
					convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_grid_photodata_folder, null);

				}
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.build(info);
			return convertView;
		}

	}

	private class ViewHolder {
		public ImageView itemImg;
		public TextView itemName;
		DisplayImageOptions options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.default_error)
				.showImageOnFail(R.drawable.default_error).resetViewBeforeLoading(true).cacheOnDisk(true)
				.cacheInMemory(true).build();

		public ViewHolder(View container) {
			itemImg = (ImageView) container.findViewById(R.id.item_img);
			itemName = (TextView) container.findViewById(R.id.item_name);
		}

		public void build(PhotoDataInfo data) {
			if (data.type == PhotoDataInfo.TYPE_IMAGE) {
				ImageLoader.getInstance().displayImage("file://" + data.localPath, itemImg, options);
			}
			itemName.setText(data.name);
		}
	}

}
