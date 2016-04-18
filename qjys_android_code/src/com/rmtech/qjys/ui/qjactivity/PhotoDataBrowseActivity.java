package com.rmtech.qjys.ui.qjactivity;

import android.content.Context;
import com.rmtech.qjys.ui.view.HackyViewPager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.rmtech.qjys.R;
import com.rmtech.qjys.ui.BaseActivity;
import com.rmtech.qjys.ui.view.HackyViewPager;

@SuppressLint("NewApi")
public class PhotoDataBrowseActivity extends BaseActivity implements
		OnViewTapListener {

	protected static final String TAG = PhotoDataBrowseActivity.class
			.getSimpleName();

	private static final String ISLOCKED_ARG = "isLocked";

	private ViewPager mViewPager;
	private MenuItem menuLockItem;

	protected Context mContext;
	protected RelativeLayout mTitleLayout;
	protected TextView mReturnTv;
	protected TextView mTitleTv;
	protected TextView mEditTv;
	protected RelativeLayout mBottomLayout;
	protected TextView mBottomTimeTv;
	protected TextView mBottomAuthTv;
	protected TextView mBottomTitleTv;
	protected TextView mSelectImage;

	private SamplePagerAdapter mAdapter;

	private void initViews() {
		mContext = this;
		mTitleLayout = (RelativeLayout) findViewById(R.id.title_layout);
		mReturnTv = (TextView) findViewById(R.id.return_tv);
		mTitleTv = (TextView) findViewById(R.id.title_tv);
		mEditTv = (TextView) findViewById(R.id.edit_tv);
		mEditTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PhotoDataEditActivity.show(getActivity());
				
			}
		});
		mBottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
		mBottomTimeTv = (TextView) findViewById(R.id.bottom_time_tv);
		mBottomAuthTv = (TextView) findViewById(R.id.bottom_auth_tv);
		mBottomTitleTv = (TextView) findViewById(R.id.bottom_title_tv);
		mSelectImage = (TextView) findViewById(R.id.select_image);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qj_photo_browse);
		mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
		initViews();
		mAdapter = new SamplePagerAdapter(this);
		mViewPager.setAdapter(mAdapter);

		if (savedInstanceState != null) {
			boolean isLocked = savedInstanceState.getBoolean(ISLOCKED_ARG,
					false);
			((HackyViewPager) mViewPager).setLocked(isLocked);
		}
		mViewPager.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				mTitleTv.setText((arg0 + 1) + "/" + mAdapter.getCount());
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	public static void show(Activity context) {
		Intent intent = new Intent();
		intent.setClass(context, PhotoDataBrowseActivity.class);
		context.startActivity(intent);
	}

	static class SamplePagerAdapter extends PagerAdapter {
		public SamplePagerAdapter(OnViewTapListener listener) {
			this.listener = listener;
		}

		private static final int[] sDrawables = { R.drawable.ic_launcher,
				R.drawable.ic_launcher, R.drawable.ic_launcher,
				R.drawable.ic_launcher, R.drawable.ic_launcher,
				R.drawable.ic_launcher };
		private OnViewTapListener listener;

		@Override
		public int getCount() {
			return sDrawables.length;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			PhotoView photoView = new PhotoView(container.getContext());
			photoView.setImageResource(sDrawables[position]);
			photoView.setOnViewTapListener(listener);

			// Now just add PhotoView to ViewPager and return it
			container.addView(photoView, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);

			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

	private void toggleViewPagerScrolling() {
		if (isViewPagerActive()) {
			((HackyViewPager) mViewPager).toggleLock();
		}
	}

	private boolean isViewPagerActive() {
		return (mViewPager != null && mViewPager instanceof HackyViewPager);
	}

	@Override
	protected void onSaveInstanceState(@NonNull Bundle outState) {
		if (isViewPagerActive()) {
			outState.putBoolean(ISLOCKED_ARG,
					((HackyViewPager) mViewPager).isLocked());
		}
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onViewTap(View view, float x, float y) {
		if (mTitleLayout.getVisibility() == View.VISIBLE) {
			mTitleLayout.setVisibility(View.GONE);
			mBottomLayout.setVisibility(View.GONE);
		} else {
			mTitleLayout.setVisibility(View.VISIBLE);
			mBottomLayout.setVisibility(View.VISIBLE);

		}
	}

}
