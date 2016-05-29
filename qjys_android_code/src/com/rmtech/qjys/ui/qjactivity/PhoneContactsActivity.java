package com.rmtech.qjys.ui.qjactivity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.util.HanziToPinyin;
import com.hyphenate.util.HanziToPinyin.Token;
import com.rmtech.qjys.R;
import com.rmtech.qjys.adapter.PhoneContactListAdapter;
import com.rmtech.qjys.db.DbOpenHelper;
import com.rmtech.qjys.model.PhoneContact;
import com.rmtech.qjys.ui.BaseActivity;
import com.rmtech.qjys.ui.view.BladeView;
import com.rmtech.qjys.ui.view.BladeView.OnItemClickListener;
import com.rmtech.qjys.ui.view.MySectionIndexer;
import com.rmtech.qjys.ui.view.PinnedHeaderListView;
import com.rmtech.qjys.utils.DoctorListManager;

public class PhoneContactsActivity extends BaseActivity {
	/** 用于保存已设置的手机号 */
	private ArrayList<String> tempPhones = new ArrayList<String>();
	public static final int REQUEST_ADD_FRIEND = 0x2302;
	private Activity context;
	private EditText et_search_view;
	private ProgressDialog progressDialog = null;
	// 数据加载完成的消息
	private final int MESSAGE_SUCC_LOAD = 0;
	// 数据查询完成的消息
	private final int MESSAGE_SUCC_QUERY = 1;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MESSAGE_SUCC_LOAD:
				setViewLoad();
				progressDialog.dismiss();
				break;
			case MESSAGE_SUCC_QUERY:
				setViewQuery();
				progressDialog.dismiss();
				break;
			}
		}
	};
	private static final String ALL_CHARACTER = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private String[] sections = { "#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
			"Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
	private PinnedHeaderListView listView;
	private MySectionIndexer mIndexer;
	private BladeView mLetterListView;
	private int[] counts;
	private List<PhoneContact> cityList = new ArrayList<PhoneContact>();
	private List<PhoneContact> cityListTemp = new ArrayList<PhoneContact>();

	private PhoneContactListAdapter mAdapterAll;
	private PhoneContactListAdapter mAdapterQuery;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qj_phone_contacts);
		setTitle("手机通讯录朋友");
		setLeftTitle("返回");
		context = PhoneContactsActivity.this;
		counts = new int[sections.length];
		initView();
		tempPhones.addAll(DoctorListManager.getInstance().getDoctorPhonNumberList());
		loadAndSaveContacts();

	}

	protected boolean showTitleBar() {
		return true;
	}

	protected void setViewQuery() {
		mAdapterQuery = new PhoneContactListAdapter(cityListTemp, context);
		listView.setAdapter(mAdapterQuery);
		listView.setOnScrollListener(mAdapterQuery);

	}

	protected void setViewLoad() {
		if (mIndexer == null) {
			mIndexer = new MySectionIndexer(sections, counts);
		}
		mAdapterAll = new PhoneContactListAdapter(cityList, mIndexer, context);
		listView.setAdapter(mAdapterAll);

		listView.setOnScrollListener(mAdapterAll);

		// 設置頂部固定頭部
		listView.setPinnedHeaderView(LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.qj_phonecontact_item_top, listView, false));
	}

	private void initView() {
		et_search_view = (EditText) findViewById(R.id.search_view);
		et_search_view.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (TextUtils.isEmpty(s.toString())) {
					handler.sendEmptyMessage(MESSAGE_SUCC_LOAD);
				} else {
					queryContacts(s.toString());
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		// 使用listView显示联系人
		listView = (PinnedHeaderListView) findViewById(R.id.contact_list);
		mLetterListView = (BladeView) findViewById(R.id.mLetterListView);
		mLetterListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(String s) {
				if (s != null) {
					int section = ALL_CHARACTER.indexOf(s);
					int position = mIndexer.getPositionForSection(section);
					if (position != -1) {
						listView.setSelection(position);
					} else {
					}
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_ADD_FRIEND:
			if (resultCode == Activity.RESULT_OK) {
				PhoneContact phoneContact = (PhoneContact) data.getSerializableExtra("class");
				if (phoneContact == null) {
					return;
				}
				String phone = phoneContact.getPhone();
				// TODO 保存这个phoneContact的值
				// tempPhones.add(phone);

				if (null != cityList && cityList.size() > 0) {
					for (int i = 0; i < cityList.size(); i++) {
						String id2 = cityList.get(i).getPhone();
						if (id2.equals(phone) || id2 == phone) {
							cityList.get(i).setState(2);
							mAdapterAll.notifyDataSetChanged();
						}
					}
				}
				if (null != cityListTemp && cityListTemp.size() > 0) {
					for (int i = 0; i < cityListTemp.size(); i++) {
						String id2 = cityListTemp.get(i).getPhone();
						if (id2.equals(phone) || id2 == phone) {
							cityListTemp.get(i).setState(2);
							mAdapterQuery.notifyDataSetChanged();
						}
					}
				}
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 加载并存储联系人数据
	 */
	private void loadAndSaveContacts() {
		progressDialog = ProgressDialog.show(context, null, "正在加载联系人数据...");
		new Thread() {
			@Override
			public void run() {
				// 获取联系人数据
				cityList = findContacts();
				// 临时存储联系人数据
				saveContacts(context, cityList);
				// 发送消息通知更新UI
				handler.sendEmptyMessage(MESSAGE_SUCC_LOAD);
			}
		}.start();
	}

	/**
	 * 根据条件从本地临时库中获取联系人
	 * 
	 * @param keyWord
	 *            查询关键字
	 */
	private void queryContacts(final String keyWord) {
		new Thread() {
			@Override
			public void run() {
				// 获取联系人数据
				cityListTemp.clear();
				cityListTemp = findContactsByCond(context, keyWord);
				// 发送消息通知更新UI
				handler.sendEmptyMessage(MESSAGE_SUCC_QUERY);
			}
		}.start();
	}

	/**
	 * 获取手机联系人信息
	 * 
	 * @return List<HashMap<String, String>>
	 */
	@SuppressLint("DefaultLocale")
	public List<PhoneContact> findContacts() {
		List<PhoneContact> list = new ArrayList<PhoneContact>();
		// 查询联系人
		Cursor contactsCursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null,
				null, PhoneLookup.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
		// 姓名的索引
		int nameIndex = 0;
		// 联系人姓名
		String name = null;
		// 联系人头像ID
		String photoId = null;
		// 联系人的ID索引值
		String contactsId = null;
		// 查询联系人的电话号码
		Cursor phoneCursor = null;
		while (contactsCursor.moveToNext()) {
			nameIndex = contactsCursor.getColumnIndex(PhoneLookup.DISPLAY_NAME);
			name = contactsCursor.getString(nameIndex);
			photoId = contactsCursor.getString(contactsCursor.getColumnIndex(PhoneLookup.PHOTO_ID));
			contactsId = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts._ID));
			phoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactsId, null, null);
			while (phoneCursor.moveToNext()) {
				PhoneContact phoneMap = new PhoneContact();
				phoneMap.setId(list.size() + "");
				// 添加联系人姓名
				phoneMap.setName(name);
				// 添加联系人头像
				phoneMap.setPhoto(photoId);
				// 添加电话号码
				String phone = phoneCursor.getString(phoneCursor
						.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				phoneMap.setPhone(phone);
				// 添加号码类型（住宅电话、手机号码、单位电话等）
				phoneMap.setType(getString(ContactsContract.CommonDataKinds.Phone.getTypeLabelResource(phoneCursor
						.getInt(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)))));
				// 添加拼音
				String upperCase = getPinYinFirstAlphabet(name);
				phoneMap.setPinyin(upperCase);
				String substring = null;
				try {
					substring = upperCase.substring(0, 1);
					if (!ALL_CHARACTER.contains(substring) || substring.equals("#")) {
						counts[0]++;
						;
					} else {
						int index = ALL_CHARACTER.indexOf(substring);
						counts[index]++;
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// TODO 设置状态
//				for (int i = 0; i < tempPhones.size(); i++) {
//					String string = tempPhones.get(i);
					if (tempPhones.contains(phone)) {
						phoneMap.setState(1);
					} else {
						phoneMap.setState(0);
					}

//				}
				list.add(phoneMap);
			}
			phoneCursor.close();
		}
		contactsCursor.close();
		return list;
	}

	/**
	 * 获取联系人头像
	 * 
	 * @param context
	 *            上下文环境
	 * @param photoId
	 *            头像ID
	 * @return Bitmap
	 */
	public static Bitmap getPhoto(Context context, String photoId) {
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_defaultavatarbig);
		if (photoId != null && "".equals(photoId)) {
			String[] projection = new String[] { ContactsContract.Data.DATA15 };
			String selection = "ContactsContract.Data._ID = " + photoId;
			Cursor cur = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, projection, selection,
					null, null);
			if (cur != null) {
				cur.moveToFirst();
				byte[] contactIcon = null;
				contactIcon = cur.getBlob(cur.getColumnIndex(ContactsContract.Data.DATA15));
				if (contactIcon != null) {
					bitmap = BitmapFactory.decodeByteArray(contactIcon, 0, contactIcon.length);
				}
			}
		}
		return bitmap;
	}

	/**
	 * 自定义联系人Adapter
	 */

	/**
	 * 根据条件查询联系人数据
	 * 
	 * @param context
	 *            上下文环境
	 * @param keyWord
	 *            查询关键字
	 * @return List<HashMap<String, String>>
	 */
	public List<PhoneContact> findContactsByCond(Context context, String keyWord) {
		String filter = "";
		for (int i = 0; i < keyWord.length(); i++) {
			filter = filter + "%" + keyWord.charAt(i);
		}

		List<PhoneContact> list = new ArrayList<PhoneContact>();
		try {
			SQLiteDatabase db = DbOpenHelper.getInstance(context).getReadableDatabase();// .getSQLiteDb(context);
			String sql = "select * from contacts where name like '" + filter + "%' or name_alias like '" + filter
					+ "%' or phone like '" + filter + "%' order by _id";
			// 查询数据
			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.moveToNext()) {

				PhoneContact map = new PhoneContact();
				map.setId(list.size() + "");
				String name = cursor.getString(cursor.getColumnIndex("name"));
				map.setName(name);
				String phone = cursor.getString(cursor.getColumnIndex("phone"));
				map.setPhone(phone);
				String type = cursor.getString(cursor.getColumnIndex("type"));
				map.setType(type);
				String photo = cursor.getString(cursor.getColumnIndex("photo"));
				map.setPhoto(photo);
				// TODO 设置状态
//				for (int i = 0; i < tempPhones.size(); i++) {
//					String string = tempPhones.get(i);
//					if (string.equals(phone) || string == phone) {
//						map.setState(1);
//					} else {
//						map.setState(0);
//					}
//
//				}
				if (tempPhones.contains(phone)) {
					map.setState(1);
				} else {
					map.setState(0);
				}
				map.setPinyin(getPinYinFirstAlphabet(name));
				list.add(map);
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 判断客户手机号码是否符合规则
	 * 
	 * @param userPhone
	 *            客户手机号码
	 * @return true | false
	 */
	public static boolean isValidPhoneNumber(String userPhone) {
		if (null == userPhone || "".equals(userPhone))
			return false;
		Pattern p = Pattern.compile("^0?1[0-9]{10}");
		Matcher m = p.matcher(userPhone);
		return m.matches();
	}

	/**
	 * 获取中文的拼音首字母
	 * 
	 * @param chinese
	 *            中文
	 * @return 拼音首字母
	 */
	public static String getPinYinFirstAlphabet(String chinese) {
		String convert = "";
		ArrayList<Token> str = HanziToPinyin.getInstance().get(chinese);
		if (str == null) {
			return chinese;
		}
		for (int j = 0; j < str.size(); j++) {
			char word = chinese.charAt(j);
			// .get(0).target.substring(0, 1).toUpperCase()
			// String[] pinyinArray =
			// PinyinHelper.toHanyuPinyinStringArray(word);
			String target = str.get(j).target;
			if (target != null) {
				convert += target.substring(0, 1);
			} else {
				convert += word;
			}
		}
		return convert;
	}

	/**
	 * 存储联系人信息
	 * 
	 * @param context
	 *            上下文环境
	 * @param contactsList
	 *            联系人列表
	 */
	public static void saveContacts(Context context, List<PhoneContact> contactsList) {
		SQLiteDatabase db = null;
		try {
			db = DbOpenHelper.getInstance(context).getWritableDatabase();
			// 开启事务控制
			db.beginTransaction();
			// 先将联系人数据清空
			db.execSQL("drop table if exists contacts");
			db.execSQL("create table contacts(_id integer not null primary key autoincrement, name varchar(50), name_alias varchar(10), phone varchar(30), type varchar(50), photo varchar(50))");
			String sql = null;
			for (PhoneContact contactsMap : contactsList) {
				sql = String.format(
						"insert into contacts(name,name_alias,phone,type,photo) values('%s','%s','%s','%s','%s')",
						contactsMap.getName(), getPinYinFirstAlphabet(contactsMap.getName()), contactsMap.getPhone(),
						contactsMap.getType(), contactsMap.getPhone());
				db.execSQL(sql);
			}
			// 设置事务标志为成功
			db.setTransactionSuccessful();
		} finally {
			// 结束事务
			if (db != null) {
				db.endTransaction();
				db.close();
			}
		}
	}

}