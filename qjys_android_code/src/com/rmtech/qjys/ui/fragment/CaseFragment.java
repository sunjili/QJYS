package com.rmtech.qjys.ui.fragment;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rmtech.qjys.R;
import com.rmtech.qjys.ui.qjactivity.AddCaseActivity;
import com.rmtech.qjys.ui.qjactivity.EditCaseActivity;
import com.sjl.lib.pinnedheaderlistview.PinnedHeaderListView;
import com.sjl.lib.pinnedheaderlistview.PinnedHeaderListView.OnItemClickListener;
import com.sjl.lib.pinnedheaderlistview.SectionedBaseAdapter;
import com.sjl.lib.swipemenulistview.SwipeMenu;
import com.sjl.lib.swipemenulistview.SwipeMenuCreator;
import com.sjl.lib.swipemenulistview.SwipeMenuItem;
import com.sjl.lib.swipemenulistview.SwipeMenuListView;

public class CaseFragment extends QjBaseFragment {

	private PtrClassicFrameLayout mPtrFrame;
	private TextView mTextView;
	private PinnedHeaderListView mListView;
	private CaseSectionedAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.qj_fragment_case_list, container,
				false);
	}

	@Override
	protected void initView() {

		mTextView = (TextView) getView().findViewById(
				R.id.list_view_with_empty_view_fragment_empty_view);
		mPtrFrame = (PtrClassicFrameLayout) getView().findViewById(
				R.id.list_view_with_empty_view_fragment_ptr_frame);

		mTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mPtrFrame.setVisibility(View.VISIBLE);
				mPtrFrame.autoRefresh();
			}
		});
		mListView = (PinnedHeaderListView) getView().findViewById(
				R.id.pinnedListView);
		mAdapter = new CaseSectionedAdapter();
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onSectionClick(AdapterView<?> adapterView, View view,
					int section, long id) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int section, int position, long id) {
				// TODO Auto-generated method stub
				EditCaseActivity.show(getActivity());
			}
		});
		 // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(getActivity());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("Open");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        mListView.setMenuCreator(creator);

        // step 2. listener item click event
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
//                        open(null);
                        break;
                    case 1:
                        // delete
//					delete(item);
//                        mAppList.remove(position);
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
		mPtrFrame.setLastUpdateTimeRelateObject(this);
		mPtrFrame.disableWhenHorizontalMove(true);
		mPtrFrame.setPtrHandler(new PtrHandler() {
			@Override
			public boolean checkCanDoRefresh(PtrFrameLayout frame,
					View content, View header) {

				// here check $mListView instead of $content
				return PtrDefaultHandler.checkContentCanBePulledDown(frame,
						mListView, header);
			}

			@Override
			public void onRefreshBegin(PtrFrameLayout frame) {
				updateData();
			}

		});
		mPtrFrame.addPtrUIHandler(new PtrUIHandler() {
			
			@Override
			public void onUIReset(PtrFrameLayout frame) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onUIRefreshPrepare(PtrFrameLayout frame) {
				// TODO Auto-generated method stub
				mListView.smoothCloseMenu();
				Log.d("ssssssss","onUIRefreshPrepare");

			}
			
			@Override
			public void onUIRefreshComplete(PtrFrameLayout frame) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onUIRefreshBegin(PtrFrameLayout frame) {
				// TODO Auto-generated method stub
				mListView.smoothCloseMenu();
				Log.d("ssssssss","onUIRefreshBegin");
			}
			
			@Override
			public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
				// TODO Auto-generated method stub
				
			}
		});
		// default is false
		mPtrFrame.setPullToRefresh(true);
		mPtrFrame.setPagingTouchSlop(0);
		titleBar.setRightLayoutClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AddCaseActivity.show(getActivity());
			}
		});

	}
	
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

	private void updateData() {
		// TODO Auto-generated method stub
		mListView.postDelayed(new Runnable() {

			@Override
			public void run() {
				displayData();
			}
		}, 500);

	}

	private void displayData() {

		mTextView.setVisibility(View.GONE);

		// mAdapter.getDataList().addAll(data.optJson("data").optJson("list").toArrayList());
		mPtrFrame.refreshComplete();
		mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void setUpView() {
		titleBar.setTitle("我的病例");

	}

	public class CaseSectionedAdapter extends SectionedBaseAdapter {

		@Override
		public Object getItem(int section, int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int section, int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getSectionCount() {
			return 7;
		}

		@Override
		public int getCountForSection(int section) {
			return 15;
		}

		@Override
		public View getItemView(int section, int position, View convertView,
				ViewGroup parent) {
			LinearLayout layout = null;
			if (convertView == null) {
				LayoutInflater inflator = (LayoutInflater) parent.getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				layout = (LinearLayout) inflator.inflate(
						R.layout.case_list_item, null);
			} else {
				layout = (LinearLayout) convertView;
			}
			ViewHolder viewHolder = (ViewHolder) layout.getTag();
			if (viewHolder == null) {
				viewHolder = new ViewHolder(layout);
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
			((TextView) layout.findViewById(R.id.textItem))
					.setText("Header for section " + section);
			return layout;
		}

	}

	private static class ViewHolder {
		public TextView nameTv;
		public TextView genderTv;
		public TextView timeTv;
		public TextView doctorsTv;
		public TextView statusTv;
		public TextView contentTv;

		public ViewHolder(View container) {
			nameTv = (TextView) container.findViewById(R.id.name_tv);
			genderTv = (TextView) container.findViewById(R.id.gender_tv);
			timeTv = (TextView) container.findViewById(R.id.time_tv);
			doctorsTv = (TextView) container.findViewById(R.id.doctors_tv);
			statusTv = (TextView) container.findViewById(R.id.status_tv);
			contentTv = (TextView) container.findViewById(R.id.content_tv);
		}
	}

}
