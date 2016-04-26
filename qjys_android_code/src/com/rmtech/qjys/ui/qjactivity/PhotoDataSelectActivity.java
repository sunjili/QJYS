package com.rmtech.qjys.ui.qjactivity;

import java.util.HashSet;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rmtech.qjys.R;
import com.rmtech.qjys.adapter.PhotoDataGridAdapter;
import com.rmtech.qjys.model.PhotoDataInfo;
import com.rmtech.qjys.model.gson.MImageList.ImageDataList;
import com.sjl.lib.alertview.AlertView;
import com.sjl.lib.alertview.OnItemClickListener;

public class PhotoDataSelectActivity extends CaseWithIdActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qj_photo_select);
		setTitle("文件多选");
		initViews();
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Activity context, ImageDataList imageDataList) {
		Intent intent = new Intent();
		intent.setClass(context, PhotoDataSelectActivity.class);
		setImageDataList(intent, imageDataList);
		context.startActivity(intent);
	}

	protected Context mContext;
	protected GridView mGridView;
	protected RelativeLayout mBottomLayout;
	protected View mDividerView;
	protected TextView mDeleteTv;
	protected TextView mMoveTv;

	private List<PhotoDataInfo> mDataList;
	private HashSet<PhotoDataInfo> mSelectedImages;

	private PhotoDataGridAdapter mAdapter;

	private void initViews() {
		mContext = this;
		mGridView = (GridView) findViewById(R.id.dynamic_grid);
		mBottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
		mDividerView = (View) findViewById(R.id.divider_view);
		mDeleteTv = (TextView) findViewById(R.id.delete_tv);
		mMoveTv = (TextView) findViewById(R.id.move_tv);
		mSelectedImages = new HashSet<>();

		mDataList = imageDataList.images;
		mAdapter = new PhotoDataGridAdapter(getActivity(), mDataList, mSelectedImages,
				PhotoDataGridAdapter.SHOW_TYPE_SELECT);

		mGridView.setAdapter(mAdapter);

		mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				selectImageFromGrid(i);
			}

		});
		mDeleteTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertView("确定删除？", null, "取消", new String[] { "确定" }, null, getActivity(), AlertView.Style.Alert,
						new com.sjl.lib.alertview.OnItemClickListener() {

							@Override
							public void onItemClick(Object o, int position) {
								if (position == 0) {
									mDataList.removeAll(mSelectedImages);
									mAdapter.notifyDataSetChanged();
								}

							}
						}).setCancelable(true).show();

			}
		});
		mMoveTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

	}

	private void selectImageFromGrid(int i) {
		PhotoDataInfo select = (PhotoDataInfo) mAdapter.getItem(i);
		if (mSelectedImages.contains(select)) {
			mSelectedImages.remove(select);
		} else {
			mSelectedImages.add(select);
		}
		mAdapter.notifyDataSetChanged();

	}
	//
	// public class SelectGridAdapter extends BaseAdapter {
	//
	// @Override
	// public int getCount() {
	// return mDataList.size();
	// }
	//
	// @Override
	// public Object getItem(int position) {
	// return mDataList.get(position);
	// }
	//
	// @Override
	// public long getItemId(int position) {
	// return position;
	// }
	//
	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	// ViewHolder holder;
	// if (convertView == null) {
	// convertView = LayoutInflater.from(getActivity()).inflate(
	// R.layout.photo_grid_item_select, null);
	// holder = new ViewHolder(convertView);
	// convertView.setTag(holder);
	// } else {
	// holder = (ViewHolder) convertView.getTag();
	// }
	// holder.build(getItem(position).toString());
	// return convertView;
	// }
	//
	// }
	//
	// private class ViewHolder {
	// public ImageView itemImg;
	// public ImageView indicator;
	//
	// public ViewHolder(View container) {
	// itemImg = (ImageView) container.findViewById(R.id.image);
	// indicator = (ImageView) container.findViewById(R.id.checkmark);
	// }
	//
	// public void build(String data) {
	// if (mSelectedImages.contains(data)) {
	// indicator.setImageResource(R.drawable.ic_case_filemultiselect);
	// indicator.setVisibility(View.VISIBLE);
	// } else {
	// indicator.setVisibility(View.GONE);
	// }
	// }
	// }

}
