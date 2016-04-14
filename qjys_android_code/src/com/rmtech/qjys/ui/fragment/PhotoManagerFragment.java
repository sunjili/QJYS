package com.rmtech.qjys.ui.fragment;

import java.util.ArrayList;
import java.util.Arrays;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.rmtech.qjys.R;
import com.rmtech.qjys.ui.qjactivity.PhotoDataBrowseActivity;
import com.rmtech.qjys.ui.qjactivity.PhotoDataManagerActivity;
import com.rmtech.qjys.ui.view.CaseTopBarView;
import com.sjl.lib.alertview.AlertView;
import com.sjl.lib.alertview.OnDismissListener;
import com.sjl.lib.alertview.OnItemClickListener;
import com.sjl.lib.dynamicgrid.DynamicGridView;
import com.sjl.lib.dynamicgrid.example.CheeseDynamicAdapter;
import com.sjl.lib.dynamicgrid.example.Cheeses;

public class PhotoManagerFragment extends QjBaseFragment {

	protected static final String TAG = PhotoManagerFragment.class
			.getSimpleName();
	private DynamicGridView mGridView;

	private CaseTopBarView mQuickReturnView;
	private int mCachedVerticalScrollRange;
	private int mQuickReturnHeight;

	private static final int STATE_ONSCREEN = 0;
	private static final int STATE_OFFSCREEN = 1;
	private static final int STATE_RETURNING = 2;
	private int mState = STATE_ONSCREEN;
	private int mScrollY;
	private int mMinRawY = 0;
	private TranslateAnimation anim;
	private AlertView mAlertViewExt;
	private EditText etName;
	private ArrayList<String> mDataList;
	private CheeseDynamicAdapter mAdapter;

	public PhotoManagerFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.bfragment_photo_manager,
				container, false);
		return rootView;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mGridView = (DynamicGridView) getView().findViewById(R.id.dynamic_grid);

	}

	public boolean onBackPressed() {

		if (mAlertViewExt != null && mAlertViewExt.isShowing()) {
			mAlertViewExt.dismiss();
			return true;
		}

		return false;
	}

	@Override
	protected void setUpView() {
		mDataList = new ArrayList<String>(Arrays.asList(Cheeses.sCheeseStrings));
		mAdapter = new CheeseDynamicAdapter(getActivity(), mDataList,
				getResources().getInteger(R.integer.column_count));
		// TODO Auto-generated method stub
		mGridView.setAdapter(mAdapter);
		mGridView.setEditModeEnabled(false);
		mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				PhotoDataBrowseActivity.show(getActivity());
			}
		});
		mGridView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(getActivity(), " onItemLongClick",
						Toast.LENGTH_SHORT).show();
				new AlertView(null, null, "取消", null, new String[] { "删除",
						"重命名" }, getActivity(), AlertView.Style.ActionSheet,
						new com.sjl.lib.alertview.OnItemClickListener() {

							@Override
							public void onItemClick(Object o, int position) {
								switch (position) {
								case 0:
									// showCameraAction();
									break;
								case 1:
									// startImageSelector();
									// ImageSelectorMainActivity.show(PhotoDataManagerActivity.this);
									break;
								}

							}
						}).show();
				return true;
			}
		});

		mGridView.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						mQuickReturnHeight = mQuickReturnView.getHeight();
						mGridView.computeScrollY();
						mCachedVerticalScrollRange = mGridView.getListHeight();
					}
				});

		mGridView.setOnScrollListener(new OnScrollListener() {
			@SuppressLint("NewApi")
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				mScrollY = 0;
				int translationY = 0;

				if (mGridView.scrollYIsComputed()) {
					mScrollY = mGridView.getComputedScrollY();
				}

				int rawY = mGridView.getTop()
						- Math.min(
								mCachedVerticalScrollRange
										- mGridView.getHeight(), mScrollY);

				switch (mState) {
				case STATE_OFFSCREEN:
					if (rawY <= mMinRawY) {
						mMinRawY = rawY;
					} else {
						mState = STATE_RETURNING;
					}
					translationY = rawY;
					break;

				case STATE_ONSCREEN:
					if (rawY < -mQuickReturnHeight) {
						mState = STATE_OFFSCREEN;
						mMinRawY = rawY;
					}
					translationY = rawY;
					break;

				case STATE_RETURNING:
					translationY = (rawY - mMinRawY) - mQuickReturnHeight;
					if (translationY > 0) {
						translationY = 0;
						mMinRawY = rawY - mQuickReturnHeight;
					}

					if (rawY > 0) {
						mState = STATE_ONSCREEN;
						translationY = rawY;
					}

					if (translationY < -mQuickReturnHeight) {
						mState = STATE_OFFSCREEN;
						mMinRawY = rawY;
					}
					break;
				}

				/** this can be used if the build is below honeycomb **/
				if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB) {
					anim = new TranslateAnimation(0, 0, translationY,
							translationY);
					anim.setFillAfter(true);
					anim.setDuration(0);
					mQuickReturnView.startAnimation(anim);
				} else {
					mQuickReturnView.setTranslationY(translationY);
				}

			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
		});
	}

	public void setQuickReturnView(CaseTopBarView mCaseTopBarView) {
		// TODO Auto-generated method stub
		mQuickReturnView = mCaseTopBarView;

	}

	private void closeKeyboard() {
		// 关闭软键盘
		if (imm != null) {
			imm.hideSoftInputFromWindow(etName.getWindowToken(), 0);
		}
		// 恢复位置
		mAlertViewExt.setMarginBottom(0);
	}

	private InputMethodManager imm;

	public void showNewFolderDialog() {
		if (imm == null) {
			imm = (InputMethodManager) getActivity().getSystemService(
					Context.INPUT_METHOD_SERVICE);
		}
		if (mAlertViewExt == null) {
			mAlertViewExt = new AlertView("新建文件夹", null, "取消", null,
					new String[] { "完成" }, getActivity(),
					AlertView.Style.Alert, new OnItemClickListener() {

						@Override
						public void onItemClick(Object o, int position) {
							closeKeyboard();
							if (o == mAlertViewExt
									&& position != AlertView.CANCELPOSITION) {
								String name = etName.getText().toString();
								if (name.isEmpty()) {
									Toast.makeText(getActivity(), "请输入文件名",
											Toast.LENGTH_SHORT).show();
								} else {
									addFolderToGrid();
								}
								return;
							}
						}
					});
			ViewGroup extView = (ViewGroup) LayoutInflater.from(getActivity())
					.inflate(R.layout.alertext_form, null);
			etName = (EditText) extView.findViewById(R.id.etName);
			etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View view, boolean focus) {
					// 输入框出来则往上移动
					boolean isOpen = imm.isActive();
					mAlertViewExt.setMarginBottom(isOpen && focus ? 120 : 0);
				}
			});
			mAlertViewExt.addExtView(extView);
			mAlertViewExt.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(Object o) {
					closeKeyboard();
				}
			});
		}
		mAlertViewExt.show();

	}

	protected void addFolderToGrid() {
		mAdapter.add(0, "");
	}
}