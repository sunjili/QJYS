package com.hyphenate.easeui.ui;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.Call;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMConversationListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.widget.EaseConversationList;
import com.rmtech.qjys.R;
import com.rmtech.qjys.callback.QjHttpCallbackNoParse;
import com.rmtech.qjys.model.gson.MGroupList;
import com.rmtech.qjys.ui.fragment.QjBaseFragment;
import com.rmtech.qjys.ui.fragment.CaseFragment.MyNetworkReceiver;
import com.rmtech.qjys.utils.GroupAndCaseListManager;
import com.sjl.lib.swipemenulistview.SwipeMenu;
import com.sjl.lib.swipemenulistview.SwipeMenuCreator;
import com.sjl.lib.swipemenulistview.SwipeMenuItem;
import com.sjl.lib.swipemenulistview.SwipeMenuListView;
import com.sjl.lib.utils.ScreenUtil;

/**
 * 会话列表fragment
 * 
 */
public class EaseConversationListFragment extends QjBaseFragment {
	private final static int MSG_REFRESH = 2;
	// protected EditText query;
	// protected ImageButton clearSearch;
	protected boolean hidden;
	protected List<EMConversation> conversationList = new ArrayList<EMConversation>();
	protected EaseConversationList conversationListView;
	protected ViewGroup errorItemContainer;
	

	protected boolean isConflict;
	private PtrClassicFrameLayout mPtrFrame;
	public LinearLayout neterrorview;

	protected EMConversationListener convListener = new EMConversationListener() {

		@Override
		public void onCoversationUpdate() {
			refresh();
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		IntentFilter filter= new IntentFilter();    
	    filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
	    getActivity().registerReceiver(new MyNetworkReceiver(), filter);
		return inflater.inflate(R.layout.ease_fragment_conversation_list, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
			return;
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	protected void initView() {
		inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		// 会话列表控件
		conversationListView = (EaseConversationList) getView().findViewById(R.id.list);
		neterrorview = (LinearLayout) getView().findViewById(R.id.neterrorview);
		// 搜索框
		// query = (EditText) getView().findViewById(R.id.query);
		// 搜索框中清除button
		// clearSearch = (ImageButton)
		// getView().findViewById(R.id.search_clear);
		errorItemContainer = (FrameLayout) getView().findViewById(R.id.fl_error_item);

		mPtrFrame = (PtrClassicFrameLayout) getView().findViewById(R.id.list_view_with_empty_view_fragment_ptr_frame);

		mPtrFrame.setLastUpdateTimeRelateObject(this);
		mPtrFrame.disableWhenHorizontalMove(true);
		mPtrFrame.setPtrHandler(new PtrHandler() {
			@Override
			public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {

				// here check $mListView instead of $content
				return PtrDefaultHandler.checkContentCanBePulledDown(frame, conversationListView, header);
			}

			@Override
			public void onRefreshBegin(PtrFrameLayout frame) {
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
				conversationListView.smoothCloseMenu();
				Log.d("ssssssss", "onUIRefreshPrepare");

			}

			@Override
			public void onUIRefreshComplete(PtrFrameLayout frame) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onUIRefreshBegin(PtrFrameLayout frame) {
				// TODO Auto-generated method stub
				conversationListView.smoothCloseMenu();
				Log.d("ssssssss", "onUIRefreshBegin");
				updateData();
			}

			@Override
			public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status,
					PtrIndicator ptrIndicator) {
				// TODO Auto-generated method stub

			}
		});
		// default is false
		mPtrFrame.setPullToRefresh(false);
		mPtrFrame.setPagingTouchSlop(0);

		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {

				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(ScreenUtil.dp2px(90));
				// set a icon
				deleteItem.setIcon(R.drawable.ic_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		// set creator
		conversationListView.setMenuCreator(creator);

		// step 2. listener item click event
		conversationListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
				switch (index) {
				case 0:
					// delete
					// delete(item);
					// mAppList.remove(position);
					// mAdapter.notifyDataSetChanged();
					break;
				}
				return false;
			}
		});

		// set SwipeListener
		conversationListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

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
		conversationListView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
			@Override
			public void onMenuOpen(int position) {
			}

			@Override
			public void onMenuClose(int position) {
			}
		});
	}

	private void updateData() {
		// TODO Auto-generated method stub
		conversationListView.postDelayed(new Runnable() {

			@Override
			public void run() {
				mPtrFrame.refreshComplete();
				// mAdapter.notifyDataSetChanged();
			}
		}, 1000);

	}

	@Override
	protected void setUpView() {
		loadConversationList(true);
		if (listItemClickListener != null) {
			conversationListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					EMConversation conversation = conversationListView.getItem(position);
					listItemClickListener.onListItemClicked(conversation);
				}
			});
		}

		EMClient.getInstance().addConnectionListener(connectionListener);

		// query.addTextChangedListener(new TextWatcher() {
		// public void onTextChanged(CharSequence s, int start, int before, int
		// count) {
		// conversationListView.filter(s);
		// if (s.length() > 0) {
		// clearSearch.setVisibility(View.VISIBLE);
		// } else {
		// clearSearch.setVisibility(View.INVISIBLE);
		// }
		// }
		//
		// public void beforeTextChanged(CharSequence s, int start, int count,
		// int after) {
		// }
		//
		// public void afterTextChanged(Editable s) {
		// }
		// });
		// clearSearch.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// query.getText().clear();
		// hideSoftKeyboard();
		// }
		// });

		conversationListView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				hideSoftKeyboard();
				return false;
			}
		});
	}

	protected EMConnectionListener connectionListener = new EMConnectionListener() {

		@Override
		public void onDisconnected(int error) {
			if (error == EMError.USER_REMOVED || error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
				isConflict = true;
			} else {
				handler.sendEmptyMessage(0);
			}
		}

		@Override
		public void onConnected() {
			handler.sendEmptyMessage(1);
		}
	};
	private EaseConversationListItemClickListener listItemClickListener;

	protected Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				onConnectionDisconnected();
				break;
			case 1:
				onConnectionConnected();
				break;

			case MSG_REFRESH: {
//				conversationList.clear();
//				conversationList.addAll(loadConversationList());
//				conversationListView.refresh();
				loadConversationList(false);
				break;
			}
			default:
				break;
			}
		}
	};
	
	public class MyNetworkReceiver extends BroadcastReceiver {  
	    @Override  
	    public void onReceive(Context context, Intent intent) {  
	        // TODO Auto-generated method stub  
	    	
	        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);  
	        NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);  
	        NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);  
	        NetworkInfo activeInfo = manager.getActiveNetworkInfo();  
//	        Toast.makeText(context, "mobile:"+mobileInfo.isConnected()+"\n"+"wifi:"+wifiInfo.isConnected()  
//	                        +"\n"+"active:"+activeInfo.getTypeName(), 1).show();
	        if (conversationList == null || conversationList.size() == 0) {
				neterrorview.setVisibility(View.GONE);
			} else {
				if(activeInfo!=null&&activeInfo.isConnectedOrConnecting()){
					neterrorview.setVisibility(View.GONE);
		        }else{
		        	neterrorview.setVisibility(View.VISIBLE);
		        }
			}
	        
	    } 
	  
	}  

	/**
	 * 连接到服务器
	 */
	protected void onConnectionConnected() {
		 errorItemContainer.setVisibility(View.GONE);
	}

	/**
	 * 连接断开
	 */
	protected void onConnectionDisconnected() {
		errorItemContainer.setVisibility(View.VISIBLE);
	}

	/**
	 * 刷新页面
	 */
	public void refresh() {
		if (!handler.hasMessages(MSG_REFRESH)) {
			handler.sendEmptyMessage(MSG_REFRESH);
		}
	}

	/**
	 * 获取会话列表
	 * 
	 * @param context
	 * @return +
	 */
	protected void loadConversationList(boolean needCache) {
		final List<EMConversation> sortList = new ArrayList<EMConversation>();
		GroupAndCaseListManager.initGroupList(needCache,sortList, new QjHttpCallbackNoParse<MGroupList>() {

			@Override
			public void onError(Call call, Exception e) {
				// TODO Auto-generated method stub
				initConversationList(sortList);

			}

			@Override
			public void onResponseSucces(boolean iscache, MGroupList response) {
				// TODO Auto-generated method stub
				initConversationList(sortList);

			}
		});
		// // 获取所有会话，包括陌生人
		// Map<String, EMConversation> conversations =
		// EMClient.getInstance().chatManager().getAllConversations();
		// // 过滤掉messages size为0的conversation
		// /**
		// * 如果在排序过程中有新消息收到，lastMsgTime会发生变化 影响排序过程，Collection.sort会产生异常
		// * 保证Conversation在Sort过程中最后一条消息的时间不变 避免并发问题
		// */
		// List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long,
		// EMConversation>>();
		// StringBuilder sb = new StringBuilder();
		// synchronized (conversations) {
		// for (EMConversation conversation : conversations.values()) {
		// if (conversation.getAllMessages().size() != 0) {
		// sortList.add(new Pair<Long,
		// EMConversation>(conversation.getLastMessage().getMsgTime(),
		// conversation));
		// if(conversation.isGroup()) {
		// sb.append(conversation.getUserName()).append(",");
		// }
		// }
		// }
		// }
		// String groupIds = sb.toString();
		// if(TextUtils.isEmpty(groupIds)) {
		// QjHttp.getGroupinfo(groupIds, new QjHttpCallback<MGroupList>() {
		//
		// @Override
		// public MGroupList parseNetworkResponse(String str) throws Exception {
		// return new Gson().fromJson(str, MGroupList.class);
		// }
		//
		// @Override
		// public void onResponseSucces(MGroupList response) {
		//
		// }
		//
		// @Override
		// public void onError(Call call, Exception e) {
		//
		// }
		// });
		// }
		// try {
		// // Internal is TimSort algorithm, has bug
		// sortConversationByLastChatTime(sortList);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// List<EMConversation> list = new ArrayList<EMConversation>();
		// for (Pair<Long, EMConversation> sortItem : sortList) {
		// list.add(sortItem.second);
		// }
		// return list;
	}

	protected void initConversationList(List<EMConversation> sortList) {
		// TODO Auto-generated method stub
		conversationList.clear();
		conversationList.addAll(sortList);
		conversationListView.init(conversationList);

	}

	/**
	 * 根据最后一条消息的时间排序
	 * 
	 * @param usernames
	 */
	private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
		Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
			@Override
			public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

				if (con1.first == con2.first) {
					return 0;
				} else if (con2.first > con1.first) {
					return 1;
				} else {
					return -1;
				}
			}

		});
	}

	protected void hideSoftKeyboard() {
		if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getActivity().getCurrentFocus() != null)
				inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		this.hidden = hidden;
		if (!hidden && !isConflict) {
			refresh();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!hidden) {
			refresh();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EMClient.getInstance().removeConnectionListener(connectionListener);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (isConflict) {
			outState.putBoolean("isConflict", true);
		}
	}

	public interface EaseConversationListItemClickListener {
		/**
		 * 会话listview item点击事件
		 * 
		 * @param conversation
		 *            被点击item所对应的会话
		 */
		void onListItemClicked(EMConversation conversation);
	}

	/**
	 * 设置listview item点击事件
	 * 
	 * @param listItemClickListener
	 */
	public void setConversationListItemClickListener(EaseConversationListItemClickListener listItemClickListener) {
		this.listItemClickListener = listItemClickListener;
	}

}
