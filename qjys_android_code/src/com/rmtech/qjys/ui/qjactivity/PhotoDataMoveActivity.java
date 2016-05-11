package com.rmtech.qjys.ui.qjactivity;

import java.util.ArrayList;
import java.util.List;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import okhttp3.Call;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.R;
import com.rmtech.qjys.callback.BaseModelCallback;
import com.rmtech.qjys.event.PhotoDataEvent;
import com.rmtech.qjys.model.FolderDataInfo;
import com.rmtech.qjys.model.PhotoDataInfo;
import com.rmtech.qjys.model.gson.MBase;
import com.sjl.lib.pinnedheaderlistview.PinnedHeaderListView;
import com.sjl.lib.pinnedheaderlistview.SectionedBaseAdapter;

@SuppressLint("NewApi")
public class PhotoDataMoveActivity extends CaseWithIdActivity {
	private PinnedHeaderListView mListView;
	private FolderAdapter mAdapter;
	private ArrayList<FolderDataInfo> datalist;
	private FolderDataInfo selectInfo;
	private ArrayList<PhotoDataInfo> imagelist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qj_photo_uploading);
		setTitle("传输列表");
		setLeftTitle("返回");
		datalist = getIntent().getParcelableArrayListExtra("folder_list");
		imagelist = getIntent().getParcelableArrayListExtra("image_list");

		if (TextUtils.isEmpty(folderId)) {
			if (datalist == null) {
				datalist = new ArrayList<FolderDataInfo>();
			}
			FolderDataInfo rootFolder = new FolderDataInfo();
			rootFolder.id = "0";
			rootFolder.name = "根目录";

			datalist.add(0, rootFolder);
		}

		mListView = (PinnedHeaderListView) findViewById(R.id.pinnedListView);
		mAdapter = new FolderAdapter();
		mAdapter.setData(datalist);
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
						selectInfo = mAdapter.setSelect(position);
					}
				});
		setRightTitle("移动", new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (selectInfo == null) {
					Toast.makeText(getApplicationContext(), "请选择目标文件夹！", 1)
							.show();
					return;
				}

				QjHttp.moveImage(caseId, selectInfo.id, getMoveImageStr(),
						new BaseModelCallback() {

							@Override
							public void onResponseSucces(MBase response) {

								PhotoDataEvent event = new PhotoDataEvent(
										PhotoDataEvent.TYPE_MOVE);
								event.setMovedImageList(caseId, folderId,
										imagelist);
								EventBus.getDefault().post(event);

								finish();
								Toast.makeText(getApplicationContext(),
										"移动成功！", 1).show();
							}

							@Override
							public void onError(Call call, Exception e) {
								Toast.makeText(getApplicationContext(),
										"移动失败！", 1).show();

							}
						});

			}
		});
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private String getMoveImageStr() {
		StringBuilder sb = new StringBuilder();
		if (imagelist != null && imagelist.size() > 0) {
			for (int i = 0; i < imagelist.size(); i++) {
				sb.append(imagelist.get(i).id);
				if (i < imagelist.size() - 1) {
					sb.append(",");
				}
			}
		}
		return sb.toString();
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Activity context,
			ArrayList<FolderDataInfo> arrayList,
			ArrayList<PhotoDataInfo> imageList, String caseId, String folderId) {
		Intent intent = new Intent();
		intent.setClass(context, PhotoDataMoveActivity.class);
		intent.putParcelableArrayListExtra("folder_list", arrayList);
		intent.putParcelableArrayListExtra("image_list", imageList);
		setCaseId(intent, caseId);
		setFolderId(intent, folderId);
		context.startActivity(intent);
	}

	public class FolderAdapter extends SectionedBaseAdapter {

		private List<FolderDataInfo> imageList;
		private int currentPosition = -1;

		@Override
		public Object getItem(int section, int position) {
			return null;
		}

		public void addAll(ArrayList<PhotoDataInfo> imagelist2) {
			if (imagelist2 == null) {
				return;
			}
			if (imageList == null) {
				imageList = new ArrayList<FolderDataInfo>();
			}
			imageList.addAll(imagelist2);
			notifyDataSetChanged();
		}

		public FolderDataInfo setSelect(int position) {
			currentPosition = position;
			notifyDataSetChanged();
			if (imageList != null) {
				if (position >= 0 && imageList.size() > position) {
					return imageList.get(position);
				}
			}
			return null;
		}

		public void setData(List<FolderDataInfo> patientList) {
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

			return imageList.size();
		}

		@Override
		public View getItemView(int section, int position, View convertView,
				ViewGroup parent) {
			View layout = null;
			if (convertView == null) {
				layout = View.inflate(getActivity(),
						R.layout.move_folder_list_item, null);
			} else {
				layout = convertView;
			}
			ViewHolder viewHolder = (ViewHolder) layout.getTag();
			if (viewHolder == null) {
				viewHolder = new ViewHolder(layout);
				layout.setTag(viewHolder);
			}
			FolderDataInfo info = imageList.get(position);
			if (info != null) {
				viewHolder.build(info, currentPosition == position);
			}
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
			((TextView) layout.findViewById(R.id.textItem)).setText("选择目标文件夹");
			return layout;
		}

	}

	private static class ViewHolder {

		private ImageView avatar;
		private TextView name;
		private CheckBox checkbox;

		public ViewHolder(View container) {

			avatar = (ImageView) container.findViewById(R.id.avatar);
			name = (TextView) container.findViewById(R.id.name);
			checkbox = (CheckBox) container.findViewById(R.id.checkbox);
		}

		@SuppressLint("NewApi")
		public void build(final FolderDataInfo info, boolean isChecked) {
			name.setText(info.name);
			if (isChecked) {
				checkbox.setChecked(true);
				checkbox.setVisibility(View.VISIBLE);
			} else {
				checkbox.setVisibility(View.GONE);

			}

		}
	}
}
