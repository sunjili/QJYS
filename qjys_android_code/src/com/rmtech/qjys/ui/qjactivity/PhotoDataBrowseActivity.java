package com.rmtech.qjys.ui.qjactivity;

import java.util.ArrayList;
import java.util.List;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rmtech.qjys.R;
import com.rmtech.qjys.event.CaseEvent;
import com.rmtech.qjys.event.PhotoDataEvent;
import com.rmtech.qjys.model.PhotoDataInfo;
import com.rmtech.qjys.ui.view.HackyViewPager;
import com.rmtech.qjys.utils.GroupAndCaseListManager;
import com.sjl.lib.utils.L;

@SuppressLint("NewApi")
public class PhotoDataBrowseActivity extends CaseWithIdActivity implements
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

	private ArrayList<PhotoDataInfo> datalist;
	private PhotoDataInfo currentPhotoData;

	private void initViews() {
		mContext = this;
		mTitleLayout = (RelativeLayout) findViewById(R.id.title_layout);
		mReturnTv = (TextView) findViewById(R.id.return_tv);
		mTitleTv = (TextView) findViewById(R.id.title_tv);
		mEditTv = (TextView) findViewById(R.id.edit_tv);
		mEditTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PhotoDataEditActivity.show(getActivity(), currentPhotoData,
						caseId, folderId);
			}
		});
		mBottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
		mBottomTimeTv = (TextView) findViewById(R.id.bottom_time_tv);
		mBottomAuthTv = (TextView) findViewById(R.id.bottom_auth_tv);
		mBottomTitleTv = (TextView) findViewById(R.id.bottom_title_tv);
		mSelectImage = (TextView) findViewById(R.id.select_image);
	}

	@Override
	protected boolean showTitleBar() {
		return false;
	}

	@Subscribe
	public void onEvent(PhotoDataEvent event) {
		if (event != null && event.dataInfo != null
				&& event.type == PhotoDataEvent.TYPE_EDIT && datalist != null) {
			for (PhotoDataInfo info : datalist) {
				if (TextUtils.equals(info.id, event.dataInfo.id)) {
					info.origin_url = event.dataInfo.origin_url;
					info.thumb_url = event.dataInfo.thumb_url;
					if (mAdapter != null) {
						mAdapter.notifyDataSetChanged();
					}
					break;
				}
			}
		}
	}

	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qj_photo_browse);
		mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
		initViews();
		datalist = getIntent().getParcelableArrayListExtra("iamge_list");
		int current_position = getIntent().getIntExtra("current_position", 0);
		if (datalist == null || datalist.size() == 0) {
			finish();
			return;
		}
		mAdapter = new SamplePagerAdapter(datalist, this);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setCurrentItem(current_position, false);
		currentPhotoData = datalist.get(current_position);
		mTitleTv.setText((current_position + 1) + "/" + datalist.size());

		if (savedInstanceState != null) {
			boolean isLocked = savedInstanceState.getBoolean(ISLOCKED_ARG,
					false);
			((HackyViewPager) mViewPager).setLocked(isLocked);
		}
		mViewPager.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				currentPhotoData = datalist.get(arg0);
				mTitleTv.setText((arg0 + 1) + "/" + mAdapter.getCount());
				mBottomTitleTv.setText(currentPhotoData.name);
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
		EventBus.getDefault().register(this);

	}

	public static void show(Activity context, int current_position,
			ArrayList<PhotoDataInfo> arrayList, String caseId, String folderId) {
		Intent intent = new Intent();
		intent.setClass(context, PhotoDataBrowseActivity.class);
		intent.putParcelableArrayListExtra("iamge_list", arrayList);
		setCaseId(intent, caseId);
		setFolderId(intent, folderId);

		intent.putExtra("current_position", current_position);
		// setCaseInfo(intent, arrayList);

		context.startActivity(intent);
	}

	static class SamplePagerAdapter extends PagerAdapter {

		DisplayImageOptions optionsThumb = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.default_error)
				.showImageOnFail(R.drawable.default_error)
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.cacheInMemory(true).build();

		DisplayImageOptions optionsOrigin = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.showImageOnLoading(R.drawable.image_loading)
				.cacheInMemory(true).build();

		private List<PhotoDataInfo> dataList;

		public SamplePagerAdapter(List<PhotoDataInfo> dataList,
				OnViewTapListener listener) {
			this.listener = listener;
			this.dataList = dataList;
		}

		// private static final int[] sDrawables = { R.drawable.ic_launcher,
		// R.drawable.ic_launcher, R.drawable.ic_launcher,
		// R.drawable.ic_launcher, R.drawable.ic_launcher,
		// R.drawable.ic_launcher };
		private OnViewTapListener listener;

		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			View root = View.inflate(container.getContext(),
					R.layout.item_photodata_browse_image, null);
			// PhotoView photoView = new PhotoView(container.getContext());
			PhotoDataInfo info = dataList.get(position);
			// ImageLoader.getInstance().displayImage(info.thumb_url, photoView,
			// optionsThumb);
			ImageView smallView = (ImageView) root
					.findViewById(R.id.small_image);
			PhotoView photoView = (PhotoView) root.findViewById(R.id.big_image);
			ImageLoader.getInstance().displayImage(info.thumb_url, smallView,
					optionsThumb);
			ImageLoader.getInstance().displayImage(info.origin_url, photoView,
					optionsOrigin);
			photoView.setOnViewTapListener(listener);
			container.addView(root, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);

			return root;
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
