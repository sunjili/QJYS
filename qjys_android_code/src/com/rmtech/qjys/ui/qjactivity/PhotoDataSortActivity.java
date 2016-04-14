package com.rmtech.qjys.ui.qjactivity;

import java.util.ArrayList;
import java.util.Arrays;

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

import com.rmtech.qjys.R;
import com.rmtech.qjys.ui.BaseActivity;
import com.sjl.lib.dynamicgrid.DynamicGridView;
import com.sjl.lib.dynamicgrid.example.CheeseDynamicAdapter;
import com.sjl.lib.dynamicgrid.example.Cheeses;

@SuppressLint("NewApi")
public class PhotoDataSortActivity extends BaseActivity implements
		OnClickListener {

	protected static final String TAG = PhotoDataSortActivity.class
			.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qj_photo_sort);
		setTitle("文件排序");
		initViews();
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Activity context) {
		Intent intent = new Intent();
		intent.setClass(context, PhotoDataSortActivity.class);
		context.startActivity(intent);
	}

	protected Context mContext;
	protected TextView mWjmSortView;
	protected TextView mScsjSortView;
	protected RelativeLayout mZdySortView;
	protected ImageView mZdySortImageview;
	protected com.sjl.lib.dynamicgrid.DynamicGridView mGridView;
	private ArrayList<String> mDataList;
	private CheeseDynamicAdapter mAdapter;

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

		mDataList = new ArrayList<String>(Arrays.asList(Cheeses.sCheeseStrings));
		mAdapter = new CheeseDynamicAdapter(getActivity(), mDataList,
				getResources().getInteger(R.integer.column_count));
		mGridView.setAdapter(mAdapter);

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
		switch (v.getId()) {
		case R.id.wjm_sort_view:
			mWjmSortView.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.btn_choice_press), null, null, null);
			mScsjSortView.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.btn_choice_nor), null, null, null);
			mZdySortImageview.setImageResource(R.drawable.btn_choice_nor);
			break;
		case R.id.scsj_sort_view:
			mWjmSortView.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.btn_choice_nor), null, null, null);
			mScsjSortView.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.btn_choice_press), null, null, null);
			mZdySortImageview.setImageResource(R.drawable.btn_choice_nor);
			break;
		case R.id.zdy_sort_view:
			mWjmSortView.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.btn_choice_nor), null, null, null);
			mScsjSortView.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.btn_choice_nor), null, null, null);
			mZdySortImageview.setImageResource(R.drawable.btn_choice_press);
			break;
		}
	}
}
