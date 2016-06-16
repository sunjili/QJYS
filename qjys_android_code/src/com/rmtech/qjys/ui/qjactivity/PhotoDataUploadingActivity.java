package com.rmtech.qjys.ui.qjactivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rmtech.qjys.R;
import com.rmtech.qjys.callback.QjHttpCallback;
import com.rmtech.qjys.model.PhotoDataInfo;
import com.rmtech.qjys.model.gson.MUploadImageInfo;
import com.rmtech.qjys.utils.BitmapUtils;
import com.rmtech.qjys.utils.DoctorListManager;
import com.rmtech.qjys.utils.ExifUtils;
import com.rmtech.qjys.utils.GroupAndCaseListManager;
import com.rmtech.qjys.utils.PhotoUploadManager;
import com.rmtech.qjys.utils.PhotoUploadManager.OnPhotoUploadListener;
import com.rmtech.qjys.utils.PhotoUploadStateInfo;
import com.rmtech.qjys.utils.PhotoUtil;
import com.rmtech.qjys.widget.ColorfulRingProgressView;
import com.sjl.lib.alertview.AlertView;
import com.sjl.lib.filechooser.FileUtils;
import com.sjl.lib.pinnedheaderlistview.PinnedHeaderListView;
import com.sjl.lib.pinnedheaderlistview.SectionedBaseAdapter;
import com.sjl.lib.swipemenulistview.SwipeMenu;
import com.sjl.lib.swipemenulistview.SwipeMenuCreator;
import com.sjl.lib.swipemenulistview.SwipeMenuItem;
import com.sjl.lib.swipemenulistview.SwipeMenuListView;
import com.sjl.lib.utils.ScreenUtil;
import com.umeng.analytics.MobclickAgent;

public class PhotoDataUploadingActivity extends CaseWithIdActivity implements
		OnPhotoUploadListener {

	private PinnedHeaderListView mListView;
	private ImageUploadingAdapter mAdapter;

	@Override
	public void onUploadProgress(PhotoUploadStateInfo state) {
		// TODO Auto-generated method stub
		// Log.d("ssssss", "onUploadProgress progress=" + state.progress);

		// if (mAdapter != null) {
		// mAdapter.notifyDataSetChanged();
		// }
	}

	@Override
	public void onUploadError(PhotoUploadStateInfo state, Exception e) {
		resetAdapter();
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onUploadComplete(PhotoUploadStateInfo state, PhotoDataInfo info) {
		// TODO Auto-generated method stub
		resetAdapter();
		mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onDestroy() {
		PhotoUploadManager.getInstance().unregisterPhotoUploadListener(this);
		super.onDestroy();
	}

	private void resetAdapter() {
		SparseArray<PhotoUploadStateInfo> map = PhotoUploadManager
				.getInstance().getUploadTaskArray();

		ArrayList<PhotoUploadStateInfo> datalist = new ArrayList<PhotoUploadStateInfo>();
		for (int i = 0; i < map.size(); i++) {
			int key = map.keyAt(i); // get the object by the key.
			PhotoUploadStateInfo state = map.get(key);
			datalist.add(state);
		}
		mAdapter.setData(datalist);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qj_photo_uploading);
		setTitle("传输列表");
		setLeftTitle("");
		setLeftGone();
		setRightTitle("关闭", new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActivity().finish();
			}
		});
		PhotoUploadManager.getInstance().registerPhotoUploadListener(this);

		mListView = (PinnedHeaderListView) findViewById(R.id.pinnedListView);
		mAdapter = new ImageUploadingAdapter();
		resetAdapter();
		mListView.setAdapter(mAdapter);
		mListView
				.setOnItemClickListener(new PinnedHeaderListView.OnItemClickListener() {

					@Override
					public void onSectionClick(AdapterView<?> adapterView,
							View view, int section, long id) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onItemClick(AdapterView<?> adapterView,
							View view, int section, int position, long id) {
						// CaseInfo info = mAdapter.getCaseInfoByPos(section,
						// position);
						// PhotoDataManagerActivity.show(getActivity(), info,
						// null);

					}
				});
		
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				final PhotoUploadStateInfo info = (PhotoUploadStateInfo) mAdapter.getItem(position);
				if(info.imageInfo.state == PhotoDataInfo.STATE_UPLOAD_FAILED){
					new AlertView(null, null, "取消", null, new String[] { "重新上传", "取消上传"}, getActivity(),
							AlertView.Style.ActionSheet, new com.sjl.lib.alertview.OnItemClickListener() {

								@Override
								public void onItemClick(Object o, int position) {
									switch (position) {
									case 0:
										PhotoUploadManager.getInstance().retry(info);
										mAdapter.notifyDataSetChanged();
										break;
									case 1:
										PhotoUploadManager.getInstance().cancel(info);
										resetAdapter();
										mAdapter.notifyDataSetChanged();
										break;
									}

								}
							}).show();
				}else {
					new AlertView(null, null, "取消", null, new String[] {"取消上传"}, getActivity(),
							AlertView.Style.ActionSheet, new com.sjl.lib.alertview.OnItemClickListener() {

								@Override
								public void onItemClick(Object o, int position) {
									switch (position) {
									case 0:
										PhotoUploadManager.getInstance().cancel(info);
										resetAdapter();
										mAdapter.notifyDataSetChanged();
										break;
									}
								}
							}).show();
				}
				return false;
			}
		});
		
		
		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				
				SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity());
				// set item background
				// deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
				// 0x3F, 0x25)));
				deleteItem.setBackground(new ColorDrawable(R.color.cb2));

				// set item width
				deleteItem.setWidth(ScreenUtil.dp2px(90));
				deleteItem.setTitle("重新上传");
				deleteItem.setTitleSize(18);

				// set a icon
				deleteItem.setTitleColor(Color.WHITE);
				// deleteItem.setIcon(R.drawable.ic_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
				
				
				SwipeMenuItem openItem = new SwipeMenuItem(getActivity());
				// set item background
				openItem.setBackground(new ColorDrawable(Color.RED));

				// set item width
				openItem.setWidth(ScreenUtil.dp2px(90));
				// set item title
				openItem.setTitle("取消上传");
				// set item title fontsize
				openItem.setTitleSize(18);
				// set item title font color
				openItem.setTitleColor(Color.WHITE);
				// add to menu
				menu.addMenuItem(openItem);

			}
		};
		// set creator
//		mListView.setMenuCreator(creator);

		// step 2. listener item click event
		mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(int position,
							SwipeMenu menu, int index) {
						PhotoUploadStateInfo info = (PhotoUploadStateInfo) mAdapter
								.getItem(position);
						switch (index) {
						case 1: 
							PhotoUploadManager.getInstance().cancel(info);
							resetAdapter();
							mAdapter.notifyDataSetChanged();
							break;
						
						case 0: 
							PhotoUploadManager.getInstance().retry(info);
							mAdapter.notifyDataSetChanged();
							break;
						}
						return false;
					}
				});

		// set SwipeListener
		mListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

			@Override
			public void onSwipeStart(int position) {
				// swipe start
			}

			@Override
			public void onSwipeEnd(int position) {
				// swipe end
			}
		});

		// set MenuStateChangeListener
		mListView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
					@Override
					public void onMenuOpen(int position) {
					}

					@Override
					public void onMenuClose(int position) {
					}
				});
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Activity context) {
		Intent intent = new Intent();
		intent.setClass(context, PhotoDataUploadingActivity.class);
		context.startActivity(intent);
		context.overridePendingTransition(R.anim.push_bottom_in, 0);

	}

	public class ImageUploadingAdapter extends SectionedBaseAdapter {

		private List<PhotoUploadStateInfo> imageList;

		// private ArrayList<String> keyPositionList;

		@Override
		public Object getItem(int section, int position) {
			if (imageList != null && position >= 0
					&& position < imageList.size()) {
				return imageList.get(position);
			}
			return null;
		}

		public void setData(List<PhotoUploadStateInfo> patientList) {
			this.imageList = patientList;
			notifyDataSetChanged();
		}

		@Override
		public long getItemId(int section, int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public int getSectionCount() {
			return 1;
		}

		@Override
		public int getCountForSection(int section) {
			if (imageList == null) {
				return 0;
			}
			return imageList.size();
		}

		@Override
		public View getItemView(int section, int position, View convertView,
				ViewGroup parent) {
			View layout = null;
			if (convertView == null) {
				LayoutInflater inflator = (LayoutInflater) parent.getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				layout = inflator.inflate(R.layout.image_uploading_list_item,
						null);
			} else {
				layout = convertView;
			}
			ViewHolder viewHolder = (ViewHolder) layout.getTag();
			if (viewHolder == null) {
				viewHolder = new ViewHolder(layout);
				layout.setTag(viewHolder);
			}
			PhotoUploadStateInfo info = imageList.get(position);
			if (info != null) {
				viewHolder.build(info);
			}
			//
			// ((TextView)
			// layout.findViewById(R.id.textItem)).setText("Section "
			// + section + " Item " + position);
			return layout;
		}

		@Override
		public View getSectionHeaderView(int section, View convertView,
				ViewGroup parent) {
			LinearLayout layout = null;
			if (convertView == null) {
				LayoutInflater inflator = (LayoutInflater) parent.getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				layout = (LinearLayout) inflator.inflate(
						R.layout.case_list_header_item, null);
			} else {
				layout = (LinearLayout) convertView;
			}
			((TextView) layout.findViewById(R.id.textItem)).setText("上传中"
					+ "("
					+ PhotoUploadManager.getInstance().getUploadTaskArray()
							.size() + ")");
			return layout;
		}

	}

	static DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.default_error)
			.showImageOnFail(R.drawable.default_error)
			.resetViewBeforeLoading(true).cacheOnDisk(true).cacheInMemory(true)
			.build();

	private static class ViewHolder {

		private ColorfulRingProgressView crpv;
		private TextView tvPercent;
		private ImageView avatar;
		private TextView name;
		private TextView casename;
		private TextView speed_tv;
		private TextView state_tv;
		private PhotoUploadStateInfo info;

		public ViewHolder(View container) {

			crpv = (ColorfulRingProgressView) container.findViewById(R.id.crpv);

			tvPercent = (TextView) container.findViewById(R.id.tvPercent);
			casename = (TextView) container.findViewById(R.id.casename);
			avatar = (ImageView) container.findViewById(R.id.avatar);
			name = (TextView) container.findViewById(R.id.name);
			state_tv = (TextView) container.findViewById(R.id.state_tv);
			speed_tv = (TextView) container.findViewById(R.id.speed_tv);
		}

		public void setProgress(final int progress) {
			this.info = info;
			// Log.d("ssssss","progress="+info.progress);
			crpv.setPercent(progress);
			tvPercent.setText(progress + "%");
			// if (info.imageInfo.state == PhotoDataInfo.STATE_UPLOADING) {
			// crpv.removeCallbacks(refreshRunnable);
			// crpv.postDelayed(refreshRunnable, 500);
			// }
		}

		// Runnable refreshRunnable = new Runnable() {
		// @Override
		// public void run() {
		// setProgress(info);
		// }
		// };

		@SuppressLint("NewApi")
		public void build(final PhotoUploadStateInfo info) {
			if (info.imageInfo != null) {
				name.setText(info.imageInfo.name);
				String caseName = GroupAndCaseListManager.getInstance().getCaseInfoByCaseId(info.caseId).name;
				if(caseName.length()>4){
					caseName = "(" + caseName.substring(0, 4) + "…" + "病例" + ")";
				}else {
					caseName = "(" + caseName + "病例" + ")";
				}
				casename.setText(caseName);
				ImageLoader.getInstance().displayImage(
						"file://" + info.imageInfo.localPath, avatar, options);

//				String path = "file://" + info.imageInfo.localPath;
//				String path =  info.imageInfo.localPath;
//				BitmapFactory.Options opts = new BitmapFactory.Options();// 获取缩略图显示到屏幕上
//				opts.inSampleSize = 2;
//				Bitmap srcBitmap = BitmapFactory.decodeFile(path, opts);
//
//				int degree = ExifUtils.getExifOrientation(path);
//				if (degree != 0) {
//					Bitmap resBitmap = BitmapUtils.rotateBitmap(srcBitmap,
//							degree, true);
//					avatar.setImageBitmap(resBitmap);
//
//				} else {
//					avatar.setImageBitmap(srcBitmap);
//				}

				if (info.imageInfo.state == PhotoDataInfo.STATE_UPLOAD_FAILED) {
					state_tv.setTextColor(state_tv.getContext().getResources()
							.getColor(R.color.red));
					state_tv.setText("上传失败");
					setProgress(0);
					speed_tv.setText(0 + "kb/s");

				} else {
					state_tv.setTextColor(state_tv.getContext().getResources()
							.getColor(R.color.c7e));
					state_tv.setText("正在上传");
					setProgress(info.progress);
					speed_tv.setText(((int) (Math.random() * 100)) + "kb/s");

				}

				info.setCallbackForList(new QjHttpCallback<MUploadImageInfo>() {

					@Override
					public MUploadImageInfo parseNetworkResponse(String str)
							throws Exception {
						return null;
					}

					@Override
					public void onResponseSucces(MUploadImageInfo response) {
						state_tv.setText("上传成功");

					}

					@Override
					public void onError(Call call, Exception e) {
						state_tv.setText("上传失败");
					}

					@Override
					public void inProgress(float progress) {
						setProgress((int) progress);
						Log.d("ssssssss",
								"PhotoDataUploadingActivity progress = "
										+ progress);
					}
				});
				// if (info.imageInfo.state == PhotoDataInfo.STATE_UPLOADING) {
				// } else if (info.imageInfo.state ==
				// PhotoDataInfo.STATE_UPLOAD_FAILED) {
				// }
			}
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		// 关闭窗体动画显示
		this.overridePendingTransition(R.anim.push_bottom_out,
				R.anim.push_top_in);
	}
}
