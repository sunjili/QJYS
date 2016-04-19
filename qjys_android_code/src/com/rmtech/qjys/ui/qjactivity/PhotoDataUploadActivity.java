package com.rmtech.qjys.ui.qjactivity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.rmtech.qjys.model.FolderDataInfo;
import com.rmtech.qjys.model.PhotoDataInfo;
import com.rmtech.qjys.utils.PhotoUploadManager;
import com.rmtech.qjys.utils.PhotoUploadStateInfo;
import com.sjl.lib.utils.L;

public class PhotoDataUploadActivity extends PhotoDataBaseActivity {


	protected Context mContext;
	protected GridView mGridView;
	protected View nodata_layout;

	private SelectGridAdapter mAdapter;
	
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

	public static void show(Activity context, String patient_id) {
		Intent intent = new Intent();
		setCaseId(intent, patient_id);
		intent.setClass(context, PhotoDataUploadActivity.class);
		context.startActivity(intent);
	}


	private void initViews() {
		initPhotoSelector();
		mContext = this;
		mGridView = (GridView) findViewById(R.id.dynamic_grid);
		nodata_layout = findViewById(R.id.nodata_layout);
		mAdapter = new SelectGridAdapter();
		mGridView.setAdapter(mAdapter);

		mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int i, long l) {
				// selectImageFromGrid(i);
			}

		});

	}

	@Override
	public void onAddNewFolder(String name) {
		FolderDataInfo info = new FolderDataInfo();
		info.name = name;
		mAdapter.add(0, info);
		mAdapter.notifyDataSetChanged();
	}

	protected synchronized void onImagePicked(List<String> paths) {
		Log.d("ssssssssssssssss","onImagePicked");
		super.onImagePicked(paths);
		for (String path : paths) {
			PhotoDataInfo info = new PhotoDataInfo();
			info.localPath = path;
			L.e("localPath = " + path);
			int index = path.lastIndexOf('/');
			info.name = path.substring(index + 1, path.length());
			L.e("info.name = " + info.name);
			info.state = PhotoDataInfo.STATE_UPLOADING;
			mAdapter.add(info);
			PhotoUploadManager.getInstance().addUploadTask(caseId, "", info);
		}
		mAdapter.notifyDataSetChanged();

	}

	public class SelectGridAdapter extends BaseAdapter {

		ArrayList<FolderDataInfo> mList;

		public SelectGridAdapter() {
			mList = new ArrayList<FolderDataInfo>();
		}

		public void add(int position, FolderDataInfo info) {
			mList.add(position, info);
		}

		public void add(PhotoDataInfo info) {
			mList.add(info);
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public void notifyDataSetChanged() {
			if (mList.size() > 0) {
				if (nodata_layout.getVisibility() == View.VISIBLE) {
					nodata_layout.setVisibility(View.GONE);
				}
			} else {
				nodata_layout.setVisibility(View.VISIBLE);
			}
			super.notifyDataSetChanged();
		}

		@Override
		public FolderDataInfo getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public int getItemViewType(int position) {
			if (mList.get(position) instanceof PhotoDataInfo) {
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
			FolderDataInfo info = getItem(position);
			if (convertView == null) {
				if (getItemViewType(position) == 1) {
					convertView = LayoutInflater.from(getActivity()).inflate(
							R.layout.item_grid_photodata_image, null);

				} else {
					convertView = LayoutInflater.from(getActivity()).inflate(
							R.layout.item_grid_photodata_folder, null);

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
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.default_error)
				.showImageOnFail(R.drawable.default_error)
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.cacheInMemory(true).build();

		public ViewHolder(View container) {
			itemImg = (ImageView) container.findViewById(R.id.item_img);
			itemName = (TextView) container.findViewById(R.id.item_name);
		}

		public void build(FolderDataInfo data) {
			if (data instanceof PhotoDataInfo) {
				ImageLoader.getInstance().displayImage(
						"file://" + ((PhotoDataInfo) data).localPath, itemImg,
						options);
			}
			itemName.setText(data.name);
		}
	}

	@Override
	public void onUploadProgress(PhotoUploadStateInfo state) {
		super.onUploadProgress(state);
		L.e("Upload progress" + state.progress);
	}

	@Override
	public void onUploadError(PhotoUploadStateInfo state, Exception e) {
		super.onUploadError(state, e);
		// TODO Auto-generated method stub
		L.e("Upload onUploadError" + e);

	}

	@Override
	public void onUploadComplete(PhotoUploadStateInfo state, PhotoDataInfo info) {
		super.onUploadComplete(state, info);
		L.e("Upload onUploadComplete =" + info.origin_url);

	}

}
