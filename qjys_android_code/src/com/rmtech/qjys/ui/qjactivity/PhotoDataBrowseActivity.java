package com.rmtech.qjys.ui.qjactivity;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.R;
import com.rmtech.qjys.callback.BaseModelCallback;
import com.rmtech.qjys.event.PhotoDataEvent;
import com.rmtech.qjys.model.CaseInfo;
import com.rmtech.qjys.model.DoctorInfo;
import com.rmtech.qjys.model.PhotoDataInfo;
import com.rmtech.qjys.model.gson.MBase;
import com.rmtech.qjys.ui.view.HackyViewPager;
import com.rmtech.qjys.utils.DoctorListManager;
import com.rmtech.qjys.utils.DoctorListManager.OnGetDoctorInfoCallback;
import com.sjl.lib.alertview.AlertView;

@SuppressLint("NewApi")
public class PhotoDataBrowseActivity extends CaseWithIdActivity implements OnViewTapListener {

	protected static final String TAG = PhotoDataBrowseActivity.class.getSimpleName();

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
	protected TextView mDeleteImage;

	private SamplePagerAdapter mAdapter;

	private ArrayList<PhotoDataInfo> datalist;
	private PhotoDataInfo currentPhotoData;

	private int current_position;

	private void initViews() {
		mContext = this;
		mTitleLayout = (RelativeLayout) findViewById(R.id.title_layout);
		mTitleLayout.setVisibility(View.GONE);
		mReturnTv = (TextView) findViewById(R.id.return_tv);
		mReturnTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				PhotoDataBrowseActivity.this.finish();
			}
		});
		mTitleTv = (TextView) findViewById(R.id.title_tv);
		mEditTv = (TextView) findViewById(R.id.edit_tv);
		mEditTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PhotoDataEditActivity.show(getActivity(), currentPhotoData, caseId, folderId);
			}
		});
		mBottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
		mBottomLayout.setVisibility(View.GONE);
		mBottomTimeTv = (TextView) findViewById(R.id.bottom_time_tv);
		mBottomAuthTv = (TextView) findViewById(R.id.bottom_auth_tv);
		mBottomTitleTv = (TextView) findViewById(R.id.bottom_title_tv);
		mDeleteImage = (TextView) findViewById(R.id.delete_image);
		mDeleteImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertView("确定删除这张图片？", null, "取消", new String[] { "确定" }, null, getActivity(),
						AlertView.Style.Alert, new com.sjl.lib.alertview.OnItemClickListener() {

							@Override
							public void onItemClick(Object o,final int position) {
								if (position == 0) {

									QjHttp.deleteImages(currentPhotoData.id, new BaseModelCallback() {

										@Override
										public void onError(Call call, Exception e) {
											Toast.makeText(getApplicationContext(), "删除失败！", 1).show();
										}

										@Override
										public void onResponseSucces(MBase response) {
											Toast.makeText(getApplicationContext(), "删除成功！", 1).show();
											datalist.remove(currentPhotoData);
											current_position -= 1;
											if(current_position < 0) {
												current_position  = 0;
											}
											
											initViewPage();
											PhotoDataEvent event = new PhotoDataEvent(PhotoDataEvent.TYPE_DELETE);
											ArrayList<PhotoDataInfo> imagelist = new ArrayList<PhotoDataInfo>();
											imagelist.add(currentPhotoData);
											event.setMovedImageList(caseId, folderId, imagelist);
											EventBus.getDefault().post(event);
										}

									});

								}

							}
						}).setCancelable(true).show();
			}
		});
	}

	@Override
	protected boolean showTitleBar() {
		return false;
	}

	@Subscribe
	public void onEvent(PhotoDataEvent event) {
		if (event != null && event.dataInfo != null && event.type == PhotoDataEvent.TYPE_EDIT && datalist != null) {
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

	private void initViewPage() {
		mAdapter = new SamplePagerAdapter(datalist, this);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setCurrentItem(current_position, false);
		currentPhotoData = datalist.get(current_position);
		mBottomTitleTv.setText(currentPhotoData.name);
		String title = "新病人";
		if(caseInfo != null) {
			title = caseInfo.name;
		}
		mTitleTv.setText(title);
		mBottomTimeTv.setText(currentPhotoData.getCreateTimeStr());
		DoctorListManager.getInstance().getDoctorInfoByHXid(currentPhotoData.doc_id, new OnGetDoctorInfoCallback() {

			@Override
			public void onGet(DoctorInfo info) {
				if (info != null) {
					mBottomAuthTv.setText("由" + info.name + "上传");
				}

			}
		});
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
		setContentView(R.layout.activity_qj_photo_browse);
		mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
		initViews();
		datalist = getIntent().getParcelableArrayListExtra("iamge_list");
		current_position = getIntent().getIntExtra("current_position", 0);
//		caseInfo = getIntent().getParcelableExtra("case_info");
		if (datalist == null || datalist.size() == 0) {
			finish();
			return;
		}
		initViewPage();

		if (savedInstanceState != null) {
			boolean isLocked = savedInstanceState.getBoolean(ISLOCKED_ARG, false);
			((HackyViewPager) mViewPager).setLocked(isLocked);
		}
		mViewPager.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				current_position = arg0;
				currentPhotoData = datalist.get(arg0);
				if(caseInfo != null) {
					mTitleTv.setText(caseInfo.name);
				}
				mBottomTitleTv.setText(currentPhotoData.name);
				mBottomTimeTv.setText(currentPhotoData.getCreateTimeStr());
				DoctorListManager.getInstance().getDoctorInfoByHXid(currentPhotoData.doc_id,
						new OnGetDoctorInfoCallback() {

							@Override
							public void onGet(DoctorInfo info) {
								if (info != null) {
									mBottomAuthTv.setText("由" + info.name + "上传");
								}

							}
						});
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

	public static void show(Activity context, int current_position, ArrayList<PhotoDataInfo> arrayList, String caseId, CaseInfo caseInfo,
			String folderId) {
		Intent intent = new Intent();
		intent.setClass(context, PhotoDataBrowseActivity.class);
		intent.putParcelableArrayListExtra("iamge_list", arrayList);
		setCaseId(intent, caseId);
		setFolderId(intent, folderId);
		setCaseInfo(intent, caseInfo);
		intent.putExtra("current_position", current_position);
		// setCaseInfo(intent, arrayList);

		context.startActivity(intent);
	}

	public class SamplePagerAdapter extends PagerAdapter {

		DisplayImageOptions optionsThumb = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.default_error).showImageOnFail(R.drawable.default_error)
				.resetViewBeforeLoading(true).cacheOnDisk(true).cacheInMemory(true).build();

		DisplayImageOptions optionsOrigin = new DisplayImageOptions.Builder().resetViewBeforeLoading(true)
				.cacheOnDisk(true).cacheInMemory(true).build();

		private List<PhotoDataInfo> dataList;

		public SamplePagerAdapter(List<PhotoDataInfo> dataList, OnViewTapListener listener) {
			this.listener = listener;
			this.dataList = dataList;
		}

		// private static final int[] sDrawables = { R.drawable.ic_launcher,
		// R.drawable.ic_launcher, R.drawable.ic_launcher,
		// R.drawable.ic_launcher, R.drawable.ic_launcher,
		// R.drawable.ic_launcher };
		private OnViewTapListener listener;

		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			View root = View.inflate(container.getContext(), R.layout.item_photodata_browse_image, null);
			// PhotoView photoView = new PhotoView(container.getContext());
			PhotoDataInfo info = dataList.get(position);
			// ImageLoader.getInstance().displayImage(info.thumb_url, photoView,
			// optionsThumb);
			ImageView smallView = (ImageView) root.findViewById(R.id.small_image);
			PhotoView photoView = (PhotoView) root.findViewById(R.id.big_image);
			ImageLoader.getInstance().displayImage(info.thumb_url, smallView, optionsThumb);
			if(!TextUtils.isEmpty(info.localPath)) {
				ImageLoader.getInstance().displayImage("file://"+info.localPath, photoView, optionsOrigin);
			} else {
				ImageLoader.getInstance().displayImage(info.origin_url, photoView, optionsOrigin);
			}
			photoView.setOnViewTapListener(listener);
			container.addView(root, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

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
			outState.putBoolean(ISLOCKED_ARG, ((HackyViewPager) mViewPager).isLocked());
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
