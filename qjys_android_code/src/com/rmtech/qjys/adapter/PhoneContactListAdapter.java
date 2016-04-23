package com.rmtech.qjys.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rmtech.qjys.R;
import com.rmtech.qjys.model.PhoneContact;
import com.rmtech.qjys.ui.qjactivity.CaseAddFriendActivity;
import com.rmtech.qjys.ui.qjactivity.PhoneContactsActivity;
import com.rmtech.qjys.ui.view.MySectionIndexer;
import com.rmtech.qjys.ui.view.PinnedHeaderListView;
import com.rmtech.qjys.ui.view.PinnedHeaderListView.PinnedHeaderAdapter;

public class PhoneContactListAdapter extends BaseAdapter implements
		PinnedHeaderAdapter, OnScrollListener {
	private List<PhoneContact> mList;
	private MySectionIndexer mIndexer;
	private Activity mContext;
	private int mLocationPosition = -1;
	private LayoutInflater mInflater;

	public PhoneContactListAdapter(List<PhoneContact> mList, MySectionIndexer mIndexer,
			Activity mContext) {
		this.mList = mList;
		this.mIndexer = mIndexer;
		this.mContext = mContext;
		mInflater = LayoutInflater.from(mContext);
	}
	public PhoneContactListAdapter(List<PhoneContact> mList,Activity mContext) {
		this.mList = mList;
		this.mContext = mContext;
		mInflater = LayoutInflater.from(mContext);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList == null ? 0 : mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("ResourceAsColor") @Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view;
		final ViewHolder holder;
		if (convertView == null) {
			view = mInflater.inflate(R.layout.qj_phone_contact_item, null);
			holder = new ViewHolder();
			holder.group_title = (TextView) view.findViewById(R.id.group_title);
			holder.phoneContact_name = (TextView) view.findViewById(R.id.tv_name);
			holder.btn_add=(Button)view.findViewById(R.id.btn_add);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}

		final PhoneContact phoneContact = mList.get(position);

		try {
			int section = mIndexer.getSectionForPosition(position);
			if (mIndexer.getPositionForSection(section) == position) {
				holder.group_title.setVisibility(View.VISIBLE);
				holder.group_title.setText(phoneContact.getPinyinFirst());
			} else {
				holder.group_title.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		holder.phoneContact_name.setText(phoneContact.getName());
		clickBtnAdd(holder, phoneContact);
		holder.btn_add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				phoneContact.setState(1);
//				clickBtnAdd(holder, phoneContact);
				Toast.makeText(mContext, ""+phoneContact.getName(), Toast.LENGTH_SHORT).show();
				Intent intent=new Intent(mContext,CaseAddFriendActivity.class);
				Bundle bundle=new Bundle();
				bundle.putSerializable("class", phoneContact);
				intent.putExtras(bundle);
				mContext.startActivityForResult(intent, PhoneContactsActivity.REQUEST_ADD_FRIEND);
			}
		});
		return view;
	}

	@SuppressLint("ResourceAsColor")
	private void clickBtnAdd(ViewHolder holder, final PhoneContact phoneContact) {
		if (phoneContact.getState() != 0) {
			holder.btn_add.setBackgroundColor(Color.WHITE);
			holder.btn_add.setTextColor(Color.rgb(178, 178, 178));
			holder.btn_add.setText("已添加");
		} else {
			holder.btn_add
					.setBackgroundResource(R.drawable.qj_me_greenbutton_selector);
			holder.btn_add.setTextColor(Color.rgb(255, 255, 255));
			holder.btn_add.setText("添加");

		}
	}

	public static class ViewHolder {
		public TextView group_title;
		public TextView phoneContact_name;
		public Button btn_add;
	}

	@Override
	public int getPinnedHeaderState(int position) {
		try {
			int realPosition = position;
			if (realPosition < 0
					|| (mLocationPosition != -1 && mLocationPosition == realPosition)) {
				return PINNED_HEADER_GONE;
			}
			mLocationPosition = -1;
			int section = mIndexer.getSectionForPosition(realPosition);
			int nextSectionPosition = mIndexer.getPositionForSection(section + 1);
			if (nextSectionPosition != -1
					&& realPosition == nextSectionPosition - 1) {
				return PINNED_HEADER_PUSHED_UP;
			}
			return PINNED_HEADER_VISIBLE;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return position;
	}

	@Override
	public void configurePinnedHeader(View header, int position, int alpha) {
		// TODO Auto-generated method stub
		try {
			int realPosition = position;
			int section = mIndexer.getSectionForPosition(realPosition);
			String title = (String) mIndexer.getSections()[section];
			((TextView) header.findViewById(R.id.group_title)).setText(title);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		try {
			if (view instanceof PinnedHeaderListView) {
				((PinnedHeaderListView) view).configureHeaderView(firstVisibleItem);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
