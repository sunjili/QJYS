package com.rmtech.qjys.ui.qjactivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import okhttp3.Call;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.R;
import com.rmtech.qjys.adapter.PhotoDataGridAdapter;
import com.rmtech.qjys.callback.BaseModelCallback;
import com.rmtech.qjys.event.PhotoDataEvent;
import com.rmtech.qjys.model.FolderDataInfo;
import com.rmtech.qjys.model.PhotoDataInfo;
import com.rmtech.qjys.model.gson.MBase;
import com.rmtech.qjys.model.gson.MImageList.ImageDataList;
import com.sjl.lib.alertview.AlertView;

public class PhotoDataSelectActivity extends CaseWithIdActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qj_photo_select);
		setTitle("文件多选");
		setLeftTitle("取消");
		setBackImageGone();
		setRightTitle("全选", new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 全选功能
				selectAll();
			}
		});
		initViews();
		EventBus.getDefault().register(this);

	}

	@Subscribe
	public void onEvent(PhotoDataEvent event) {
		if (event != null && event.type == PhotoDataEvent.TYPE_MOVE) {
			if (!TextUtils.equals(caseId, event.caseId)) {
				return;
			}
			if (!TextUtils.equals(folderId, event.folderId)) {
				return;
			}
			if (mSelectedImages != null) {
				mSelectedImages.clear();
			}
			if (mAdapter != null && event.imagelist != null) {
				mAdapter.removeAll(event.imagelist);
			}

		}
	}

	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Activity context, String caseId, String folderId, ImageDataList imageDataList) {
		Intent intent = new Intent();
		intent.setClass(context, PhotoDataSelectActivity.class);
		setCaseId(intent, caseId);
		setFolderId(intent, folderId);
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
		if (imageDataList == null) {
			return;
		}
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
				if (mSelectedImages == null || mSelectedImages.isEmpty()) {
					Toast.makeText(getApplicationContext(), "请选择要删除的图片！", 1).show();
					return;
				}
				new AlertView("确定删除这" + mSelectedImages.size() + "张图片？", null, "取消", new String[] { "确定" }, null,
						getActivity(), AlertView.Style.Alert, new com.sjl.lib.alertview.OnItemClickListener() {

							@Override
							public void onItemClick(Object o, int position) {
								if (position == 0) {

									QjHttp.deleteImages(getSelectImageStr(), new BaseModelCallback() {

										@Override
										public void onError(Call call, Exception e) {
											Toast.makeText(getApplicationContext(), "删除失败！", 1).show();
										}

										@Override
										public void onResponseSucces(MBase response) {
											Toast.makeText(getApplicationContext(), "删除成功！", 1).show();
											ArrayList<PhotoDataInfo> imagelist = new ArrayList<PhotoDataInfo>(mSelectedImages);
											mAdapter.removeAll(imagelist);
											PhotoDataEvent event = new PhotoDataEvent(PhotoDataEvent.TYPE_DELETE);
											event.setMovedImageList(caseId, folderId, imagelist);
											EventBus.getDefault().post(event);
										}

									});
									
								}

							}
						}).setCancelable(true).show();

			}
		});
		mMoveTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mSelectedImages == null || mSelectedImages.isEmpty()) {
					Toast.makeText(getApplicationContext(), "请选择要移动的图片！", 1).show();
					return;
				}
				ArrayList<FolderDataInfo> folderList = new ArrayList<FolderDataInfo>();
				ArrayList<PhotoDataInfo> imageList = new ArrayList<PhotoDataInfo>(mSelectedImages);
				if (imageDataList.folders != null) {
					folderList.addAll(imageDataList.folders);
				}

				PhotoDataMoveActivity.show(getActivity(), folderList, imageList, caseId, folderId);
			}
		});

	}

	private String getSelectImageStr() {
		StringBuilder sb = new StringBuilder();
		if (mSelectedImages != null && mSelectedImages.size() > 0) {
			for (PhotoDataInfo info : mSelectedImages) {
				sb.append(info.id);
				sb.append(",");
			}
			return sb.substring(0, sb.length() - 1);
		}
		return sb.toString();
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
	
	private void selectAll(){
		mSelectedImages.addAll(mDataList);
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
