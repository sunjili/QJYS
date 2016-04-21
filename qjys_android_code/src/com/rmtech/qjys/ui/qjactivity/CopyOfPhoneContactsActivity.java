package com.rmtech.qjys.ui.qjactivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.util.HanziToPinyin;
import com.hyphenate.util.HanziToPinyin.Token;
import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.R;
import com.rmtech.qjys.callback.BaseModelCallback;
import com.rmtech.qjys.db.DbOpenHelper;
import com.rmtech.qjys.model.gson.MBase;
import com.rmtech.qjys.ui.BaseActivity;

public class CopyOfPhoneContactsActivity extends BaseActivity {
	private Context ctx;

	private TextView topTitleTextView;
	private ListView listView = null;
	List<HashMap<String, String>> contactsList = null;
	private EditText contactsSearchView;
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
				listView.setAdapter(new ContactsAdapter(ctx));
				progressDialog.dismiss();
				break;
			case MESSAGE_SUCC_QUERY:
				listView.setAdapter(new ContactsAdapter(ctx));
				break;
			}
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		ctx = CopyOfPhoneContactsActivity.this;
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setTitle("手机通讯录朋友");

		this.setContentView(R.layout.activity_qj_phone_contacts);
		// 使用listView显示联系人
		listView = (ListView) findViewById(R.id.contact_list);
		loadAndSaveContacts();
		// 绑定listView item的单击事件
		listView.setOnItemClickListener(new OnItemClickListener() {
			@SuppressWarnings("unchecked")
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long _id) {
				HashMap<String, String> map = (HashMap<String, String>) adapterView.getItemAtPosition(position);
				String phone = map.get("phone");
				// 对手机号码进行预处理（去掉号码前的+86、首尾空格、“-”号等）
				phone = phone.replaceAll("^(\\+86)", "");
				phone = phone.replaceAll("^(86)", "");
				phone = phone.replaceAll("-", "");
				phone = phone.trim();
				// 如果当前号码并不是手机号码
				if (!isValidPhoneNumber(phone)) {
					Toast.makeText(ctx, "获取验证码失败", Toast.LENGTH_LONG).show();
				} else {
					
					QjHttp.addFriend(phone, new BaseModelCallback() {
						
						@Override
						public void onResponseSucces(MBase response) {
							if(response.ret == 0) {
								progressDialog.dismiss();
								String s1 = getResources().getString(R.string.send_successful);
								Toast.makeText(getApplicationContext(), s1, 1).show();
							} else {
								if(response.ret == 206) {
									//跳到短信
								}
								onError(null, null);
							}
						}
						
						@Override
						public void onError(Call call, Exception e) {
							// TODO Auto-generated method stub
							progressDialog.dismiss();
							String s2 = getResources().getString(R.string.Request_add_buddy_failure);
							Toast.makeText(getApplicationContext(), s2 + e.getMessage(), 1).show();
						}
					});
//					Intent intent = new Intent();
//					intent.putExtra("phone", phone);
//					setResult(RESULT_OK, intent);
//					// 关闭当前窗口
//					finish();
				}
			}
		});
		contactsSearchView = (EditText) findViewById(R.id.search_view);
		contactsSearchView.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				queryContacts(s.toString());
			}
		});
	}

	/**
	 * 加载并存储联系人数据
	 */
	private void loadAndSaveContacts() {
		progressDialog = ProgressDialog.show(ctx, null, "正在加载联系人数据...");
		new Thread() {
			@Override
			public void run() {
				// 获取联系人数据
				contactsList = findContacts();
				// 临时存储联系人数据
				saveContacts(ctx, contactsList);
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
				contactsList = findContactsByCond(ctx, keyWord);
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
	public List<HashMap<String, String>> findContacts() {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		// 查询联系人
		Cursor contactsCursor = ctx.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null,
				PhoneLookup.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
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
			// 遍历联系人号码（一个人对应于多个电话号码）
			while (phoneCursor.moveToNext()) {
				HashMap<String, String> phoneMap = new HashMap<String, String>();
				// 添加联系人姓名
				phoneMap.put("name", name);
				// 添加联系人头像
				phoneMap.put("photo", photoId);
				// 添加电话号码
				phoneMap.put("phone", phoneCursor.getString(phoneCursor
						.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
				// 添加号码类型（住宅电话、手机号码、单位电话等）
				phoneMap.put("type", getString(ContactsContract.CommonDataKinds.Phone.getTypeLabelResource(phoneCursor
						.getInt(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)))));
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
	class ContactsAdapter extends BaseAdapter {
		private LayoutInflater inflater = null;

		public ContactsAdapter(Context ctx) {
			inflater = LayoutInflater.from(ctx);
		}

		public int getCount() {
			return contactsList.size();
		}

		public Object getItem(int position) {
			return contactsList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.list_item_phone_contacts, null);
				holder.text1 = (TextView) convertView.findViewById(R.id.text1);
				holder.text2 = (TextView) convertView.findViewById(R.id.text2);
				holder.text3 = (TextView) convertView.findViewById(R.id.text3);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.text1.setText(contactsList.get(position).get("name"));
			holder.text2.setText(contactsList.get(position).get("type"));
			holder.text3.setText(contactsList.get(position).get("phone"));
			return convertView;
		}

		public final class ViewHolder {
			private TextView text1;
			private TextView text2;
			private TextView text3;
		}
	}

	/**
	 * 根据条件查询联系人数据
	 * 
	 * @param context
	 *            上下文环境
	 * @param keyWord
	 *            查询关键字
	 * @return List<HashMap<String, String>>
	 */
	public static List<HashMap<String, String>> findContactsByCond(Context context, String keyWord) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			SQLiteDatabase db = DbOpenHelper.getInstance(context).getReadableDatabase();// .getSQLiteDb(context);
			String sql = "select * from contacts where name like '" + keyWord + "%' or name_alias like '" + keyWord
					+ "%' order by _id";
			// 查询数据
			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("name", cursor.getString(cursor.getColumnIndex("name")));
				map.put("phone", cursor.getString(cursor.getColumnIndex("phone")));
				map.put("type", cursor.getString(cursor.getColumnIndex("type")));
				map.put("photo", cursor.getString(cursor.getColumnIndex("photo")));
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
	public static void saveContacts(Context context, List<HashMap<String, String>> contactsList) {
		SQLiteDatabase db = null;
		try {
			db = DbOpenHelper.getInstance(context).getWritableDatabase();
			// 开启事务控制
			db.beginTransaction();
			// 先将联系人数据清空
			db.execSQL("drop table if exists contacts");
			db.execSQL("create table contacts(_id integer not null primary key autoincrement, name varchar(50), name_alias varchar(10), phone varchar(30), type varchar(50), photo varchar(50))");
			String sql = null;
			for (HashMap<String, String> contactsMap : contactsList) {
				sql = String.format(
						"insert into contacts(name,name_alias,phone,type,photo) values('%s','%s','%s','%s','%s')",
						contactsMap.get("name"), getPinYinFirstAlphabet(contactsMap.get("name")),
						contactsMap.get("phone"), contactsMap.get("type"), contactsMap.get("photo"));
				db.execSQL(sql);
			}
			// 设置事务标志为成功
			db.setTransactionSuccessful();
		} finally {
			// 结束事务
			if(db != null) {
				db.endTransaction();
				db.close();
			}
		}
	}
}
