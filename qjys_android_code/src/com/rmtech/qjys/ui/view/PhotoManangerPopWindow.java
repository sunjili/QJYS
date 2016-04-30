package com.rmtech.qjys.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.rmtech.qjys.R;
import com.sjl.lib.utils.ScreenUtil;

@SuppressLint("NewApi")
public class PhotoManangerPopWindow {

	public static ListPopupWindow createPopupList(Context context, View anchorView,ListPopupWindowAdapter mFolderAdapter,
			final AdapterView.OnItemClickListener listener) {
		final ListPopupWindow mFolderPopupWindow = new ListPopupWindow(context);
		mFolderPopupWindow
				.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
		
		mFolderPopupWindow.setAdapter(mFolderAdapter);
		mFolderPopupWindow.setWidth(ScreenUtil.dp2px(143));
		mFolderPopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		mFolderPopupWindow.setAnchorView(anchorView);
		mFolderPopupWindow.setModal(true);
		mFolderPopupWindow.setVerticalOffset(ScreenUtil.dp2px(10));
		mFolderPopupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.popping_box));
		mFolderPopupWindow.setListSelector(context.getResources().getDrawable(R.drawable.bg_transparent));
//	    android:background="@color/c3164aa"
//		Window自适应宽度的方案，设置adapter后，检测每
		mFolderPopupWindow.setOnItemClickListener(listener);
		return mFolderPopupWindow;
	}

	public static class ListPopupWindowAdapter extends BaseAdapter {
		private String[] mStringList = new String[] { "新建文件夹", "文件排序", "文件多选",
				"设置" };
		private int[] mImageList = new int[] {R.drawable.ic_case_newfolder,
				R.drawable.ic_case_filesorting,
				R.drawable.ic_case_filemultiselect, R.drawable.ic_case_setting };

		private Context mContext;
		private boolean isRoot;

		public ListPopupWindowAdapter(Context context, boolean isRoot) {
			super();
			this.mContext = context;
			this.isRoot = isRoot;
		}

		@Override
		public int getCount() {
			if (mStringList == null) {
				return 0;
			} else {
				if(!isRoot) {
					return mStringList.length-1;
				}
				return this.mStringList.length;
			}
		}

		@Override
		public Object getItem(int position) {
			return this.mStringList[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int pos, View convertView,
				ViewGroup parent) {
			int position = pos;
			if(!isRoot) {
				position = pos+1;
			}
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.pop_list_item_photo_manager, null, false);
				holder.display_image = (ImageView) convertView
						.findViewById(R.id.display_image);
				holder.display_tv = (TextView) convertView
						.findViewById(R.id.display_tv);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			final String itemName = (String) this.getItem(position);
			holder.display_tv.setText(itemName);
			holder.display_image.setImageResource(mImageList[position]);
			return convertView;

		}

		private class ViewHolder {
			ImageView display_image;
			TextView display_tv;
		}

	}

}
