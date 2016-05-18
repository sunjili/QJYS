package com.rmtech.qjys.ui.qjactivity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.greenrobot.eventbus.EventBus;

import okhttp3.Call;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.R;
import com.rmtech.qjys.adapter.PhotoDataGridAdapter;
import com.rmtech.qjys.callback.BaseModelCallback;
import com.rmtech.qjys.event.PhotoDataEvent;
import com.rmtech.qjys.model.PhotoDataInfo;
import com.rmtech.qjys.model.gson.MBase;
import com.rmtech.qjys.model.gson.MImageList.ImageDataList;
import com.sjl.lib.dynamicgrid.DynamicGridView;

@SuppressLint("NewApi")
public class PhotoDataSortActivity extends CaseWithIdActivity implements
		OnClickListener {

	protected static final String TAG = PhotoDataSortActivity.class
			.getSimpleName();
	protected BaseModelCallback callback = new BaseModelCallback() {
		
		@Override
		public void onResponseSucces(MBase response) {
			Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_LONG).show();

			PhotoDataEvent event = new PhotoDataEvent(
					PhotoDataEvent.TYPE_MOVE);
			event.setMovedImageList(caseId, folderId,
					null);
			EventBus.getDefault().post(event);
			
			finish();
		}
		
		@Override
		public void onError(Call call, Exception e) {
			Toast.makeText(getActivity(), "保存失败", Toast.LENGTH_LONG).show();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qj_photo_sort);
		setTitle("文件排序");
		setRightTitle("保存", new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (sortType) {
				case R.id.wjm_sort_view:
					QjHttp.sortImage(folderId, caseId, 1, null, callback );
					break;
				case R.id.scsj_sort_view:
					QjHttp.sortImage(folderId, caseId, 0, null, callback );
					break;
				case R.id.zdy_sort_view:
					QjHttp.sortImage(folderId, caseId, 2, getSortedImageIds(), callback );
					break;
				}
			}
		});
		initViews();
	}

	protected String getSortedImageIds() {
		if (mAdapter != null && mAdapter.getItems() != null
				&& !mAdapter.getItems().isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mAdapter.getItems().size(); i++) {
				sb.append(((PhotoDataInfo) mAdapter.getItems().get(i)).id);
				if (i < mAdapter.getItems().size() - 1) {
					sb.append(",");
				}
			}
			return sb.toString();
		}
		return null;
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Activity context,String caseId, String folderId, ImageDataList imageDataList) {
		Intent intent = new Intent();
		intent.setClass(context, PhotoDataSortActivity.class);
		setCaseId(intent, caseId);
		setFolderId(intent, folderId);
		setImageDataList(intent, imageDataList);
		context.startActivity(intent);
	}

	protected Context mContext;
	protected TextView mWjmSortView;
	protected TextView mScsjSortView;
	protected RelativeLayout mZdySortView;
	protected ImageView mZdySortImageview;
	protected com.sjl.lib.dynamicgrid.DynamicGridView mGridView;
	private List<PhotoDataInfo> mDataList;
	private PhotoDataGridAdapter mAdapter;
	private int sortType = 0;

	private void initViews() {
		mContext = this;
		mWjmSortView = (TextView) findViewById(R.id.wjm_sort_view);
		mWjmSortView.setOnClickListener(this);
		mScsjSortView = (TextView) findViewById(R.id.scsj_sort_view);
		mScsjSortView.setOnClickListener(this);
		mZdySortView = (RelativeLayout) findViewById(R.id.zdy_sort_view);
		mZdySortView.setOnClickListener(this);
		mZdySortImageview = (ImageView) findViewById(R.id.zdy_sort_imageview);
		mGridView = (com.sjl.lib.dynamicgrid.DynamicGridView) findViewById(R.id.dynamic_grid);

		mDataList = imageDataList.images;

		// add callback to stop edit mode if needed
		// gridView.setOnDropListener(new DynamicGridView.OnDropListener()
		// {
		// @Override
		// public void onActionDrop()
		// {
		// gridView.stopEditMode();
		// }
		// });
		mGridView.setOnDragListener(new DynamicGridView.OnDragListener() {
			@Override
			public void onDragStarted(int position) {
				Log.d(TAG, "drag started at position " + position);
			}

			@Override
			public void onDragPositionsChanged(int oldPosition, int newPosition) {
				Log.d(TAG, String.format(
						"drag item position changed from %d to %d",
						oldPosition, newPosition));
			}
		});
		mGridView
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						mGridView.startEditMode(position);
						return true;
					}
				});

		mGridView.setEditModeEnabled(false);
		initAdapter();

	}

	private void initAdapter() {
		mAdapter = new PhotoDataGridAdapter(getActivity(), mDataList);
		mGridView.setAdapter(mAdapter);
	}

	@Override
	public void onBackPressed() {

		if (mGridView.isEditMode()) {
			mGridView.stopEditMode();
			return;
		}
		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		sortType = v.getId();
		switch (sortType) {
		case R.id.wjm_sort_view:
			
			mWjmSortView
					.setCompoundDrawablesWithIntrinsicBounds(getResources()
							.getDrawable(R.drawable.btn_choice_press), null,
							null, null);
			mScsjSortView.setCompoundDrawablesWithIntrinsicBounds(
					getResources().getDrawable(R.drawable.btn_choice_nor),
					null, null, null);
			mZdySortImageview.setImageResource(R.drawable.btn_choice_nor);
			sort(R.id.wjm_sort_view);
			break;
		case R.id.scsj_sort_view:
			mWjmSortView.setCompoundDrawablesWithIntrinsicBounds(getResources()
					.getDrawable(R.drawable.btn_choice_nor), null, null, null);
			mScsjSortView.setCompoundDrawablesWithIntrinsicBounds(
					getResources().getDrawable(R.drawable.btn_choice_press),
					null, null, null);
			mZdySortImageview.setImageResource(R.drawable.btn_choice_nor);
			sort(R.id.scsj_sort_view);
			break;
		case R.id.zdy_sort_view:
			mWjmSortView.setCompoundDrawablesWithIntrinsicBounds(getResources()
					.getDrawable(R.drawable.btn_choice_nor), null, null, null);
			mScsjSortView.setCompoundDrawablesWithIntrinsicBounds(
					getResources().getDrawable(R.drawable.btn_choice_nor),
					null, null, null);
			mZdySortImageview.setImageResource(R.drawable.btn_choice_press);
			mGridView.setEditModeEnabled(true);

			break;
		}
	}

	public void sort(final int sortType) {
		// 排序
		Collections.sort(mDataList, new Comparator<PhotoDataInfo>() {

			@Override
			public int compare(PhotoDataInfo sInfo, PhotoDataInfo bInfo) {
				switch (sortType) {
				case R.id.wjm_sort_view:
					return sInfo.name.compareTo(bInfo.name);
				case R.id.scsj_sort_view:
					return sInfo.create_time - bInfo.create_time;
				}
				return 0;

			}
		});
		initAdapter();
	}
}
