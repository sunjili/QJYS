package com.rmtech.qjys.ui.qjactivity;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rmtech.qjys.R;
import com.rmtech.qjys.ui.BaseActivity;
import com.rmtech.qjys.ui.view.HackyViewPager;

@SuppressLint("NewApi")
public class ImageSelectBrowseActivity extends BaseActivity {

	protected static final String TAG = ImageSelectBrowseActivity.class.getSimpleName();

	private static final String ISLOCKED_ARG = "isLocked";

	private ViewPager mViewPager;

	private SamplePagerAdapter mAdapter;

	private ArrayList<String> datalist;

	private void initViewPage() {
		mAdapter = new SamplePagerAdapter(datalist);
		mViewPager.setAdapter(mAdapter);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
		setContentView(R.layout.activity_qj_photo_browse);
		mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
		datalist = getIntent().getStringArrayListExtra("datalist");
		if (datalist == null || datalist.size() == 0) {
			finish();
			return;
		}
		findViewById(R.id.title_layout).setVisibility(View.GONE);
		findViewById(R.id.bottom_layout).setVisibility(View.GONE);
		initViewPage();

		if (savedInstanceState != null) {
			boolean isLocked = savedInstanceState.getBoolean(ISLOCKED_ARG, false);
			((HackyViewPager) mViewPager).setLocked(isLocked);
		}

	}

	public static void show(Activity context, ArrayList<String> arrayList) {
		Intent intent = new Intent();
		intent.setClass(context, ImageSelectBrowseActivity.class);
		intent.putStringArrayListExtra("datalist", arrayList);
		context.startActivity(intent);
	}

	public class SamplePagerAdapter extends PagerAdapter {

		DisplayImageOptions optionsOrigin = new DisplayImageOptions.Builder().resetViewBeforeLoading(true)
				.cacheOnDisk(true).cacheInMemory(true).build();

		private List<String> dataList;

		public SamplePagerAdapter(List<String> dataList) {
			this.dataList = dataList;
		}

		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			View root = View.inflate(container.getContext(), R.layout.item_photodata_browse_image, null);
			String localPath = dataList.get(position);
			ImageView smallView = (ImageView) root.findViewById(R.id.small_image);
			PhotoView photoView = (PhotoView) root.findViewById(R.id.big_image);
			if (!TextUtils.isEmpty(localPath)) {
				ImageLoader.getInstance().displayImage("file://" + localPath, photoView, optionsOrigin);
			}
			container.addView(root, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			root.findViewById(R.id.progressBar).setVisibility(View.GONE);
			return root;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

	private boolean isViewPagerActive() {
		return (mViewPager != null && mViewPager instanceof HackyViewPager);
	}

	@Override
	protected void onSaveInstanceState(@NonNull Bundle outState) {
		if (isViewPagerActive()) {
			outState.putBoolean(ISLOCKED_ARG, ((HackyViewPager) mViewPager).isLocked());
		}
		super.onSaveInstanceState(outState);
	}

}
