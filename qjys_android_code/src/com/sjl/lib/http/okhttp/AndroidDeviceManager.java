package com.sjl.lib.http.okhttp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;


public class AndroidDeviceManager {
	private static final String TAG = AndroidDeviceManager.class.getName();

	private static Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
	private static AndroidDeviceManager sDevice = new AndroidDeviceManager();
	private static volatile boolean mInited = false;

	private TelephonyManager mTelephonyMgr = null;
	private ConnectivityManager mConnectivityMgr = null;
	private Context mContext = null;
	private NetworkChangeReceiver mNetworkReveiver = null;
	private WifiManager.WifiLock mWifiLock = null;
	private volatile boolean mMobileConnected = false;
	private volatile boolean mWifiConnected = false;
	private String mCurrentAPN = null;
	private String mDeviceInfo = null;
	private String IMEI = "0000000";
	private Map<String, Integer> mApnMap = new HashMap();
	private int JELLY_BEAN_NEW = 17;
	private WifiNetChangeReceiver mWifiNetChangeReceiver = null;

	private int currentASU = -2147483648;
	private boolean asuChanged = true;

	private PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
		public void onSignalStrengthsChanged(SignalStrength signalStrength) {
			int asu = signalStrength.getGsmSignalStrength();

			AndroidDeviceManager.this.currentASU = asu;
			AndroidDeviceManager.this.asuChanged = true;

			super.onSignalStrengthsChanged(signalStrength);
		}
	};

	private String storageInfo = "";

	public static AndroidDeviceManager Instance() {
		return sDevice;
	}
	
	public boolean getMobileConnectState() {
		return mMobileConnected;
	}
	
	public String getIMEI() {
		return IMEI;
	}
	
	public boolean getWifiConnectState() {
		return mWifiConnected;
	}
	
	public void setWifiNetChangeReceiver(WifiNetChangeReceiver receiver) {
		mWifiNetChangeReceiver = receiver;
	}

	private AndroidDeviceManager() {
		this.mApnMap.put("unknown", Integer.valueOf(0));
		this.mApnMap.put("cmnet", Integer.valueOf(1));
		this.mApnMap.put("cmwap", Integer.valueOf(2));
		this.mApnMap.put("3gnet", Integer.valueOf(3));
		this.mApnMap.put("3gwap", Integer.valueOf(4));
		this.mApnMap.put("uninet", Integer.valueOf(5));
		this.mApnMap.put("uniwap", Integer.valueOf(6));
		this.mApnMap.put("wifi", Integer.valueOf(7));
		this.mApnMap.put("ctwap", Integer.valueOf(8));
		this.mApnMap.put("ctnet", Integer.valueOf(9));
		this.mApnMap.put("cmcc", Integer.valueOf(10));
		this.mApnMap.put("unicom", Integer.valueOf(11));
		this.mApnMap.put("cmct", Integer.valueOf(12));
	}

	public synchronized void initialize(Context context) {
		if (context != null)
			if (!(mInited)) {
				this.mContext = context;
				this.mTelephonyMgr = ((TelephonyManager) context.getSystemService("phone"));
				this.mConnectivityMgr = ((ConnectivityManager) context.getSystemService("connectivity"));
				this.mNetworkReveiver = new NetworkChangeReceiver();

				try {
					IntentFilter upIntentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
					this.mContext.registerReceiver(this.mNetworkReveiver, upIntentFilter);
				} catch (Exception e) {
					Log.e(TAG, e.toString());
				}

				this.mTelephonyMgr.listen(this.mPhoneStateListener, 256);

				mInited = true;
				
				try {
					IMEI = this.mTelephonyMgr.getDeviceId();
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				Log.e(TAG, "initialize error : null context.");
			}
	}

	public synchronized void release() {
		if (this.mContext != null && null != mNetworkReveiver) {
			try {
				this.mContext.unregisterReceiver(this.mNetworkReveiver);
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
		}
	}

	public int getApnByName(String apnName) {
		return ((Integer) this.mApnMap.get(apnName)).intValue();
	}

	public boolean isNetworkAvailable() {
		boolean available = false;

		NetworkInfo info = null;
		try {
			info = this.mConnectivityMgr.getActiveNetworkInfo();
		} catch (Exception e) {
			return false;
		}
		if (info == null) {
			return false;
		}

		available = info.isConnected();
		if (available) {
			this.mCurrentAPN = info.getExtraInfo();
		} else {
			Log.i(TAG, "isNetworkEnable() : FALSE with TYPE = " + info.getType());
		}

		return available;
	}

	public String getDNSInfo() {
		DNSInfo info = DNSInfo.getDNSInfo(null);
		if (info != null) {
			return info.toString();
		}

		return "";
	}

	public String getAPN() {
		if (isViaWIFI()) {
			return "wifi";
		}

		if (this.mCurrentAPN == null) {
			ApnNode node = getDefaultAPN();
			if (node != null)
				this.mCurrentAPN = node.getApn();
			else {
				this.mCurrentAPN = "unknown";
			}
		}

		return this.mCurrentAPN;
	}
	
	// for http
	public String getNetAPN() {
		if (isViaWIFI()) {
			return "wifi";
		}
		
	    int networkType = mTelephonyMgr.getNetworkType();
	    switch (networkType) {
	        case TelephonyManager.NETWORK_TYPE_GPRS:
	        case TelephonyManager.NETWORK_TYPE_EDGE:
	        case TelephonyManager.NETWORK_TYPE_CDMA:
	        case TelephonyManager.NETWORK_TYPE_1xRTT:
	        case TelephonyManager.NETWORK_TYPE_IDEN:
	            return "2G";
	        case TelephonyManager.NETWORK_TYPE_UMTS:
	        case TelephonyManager.NETWORK_TYPE_EVDO_0:
	        case TelephonyManager.NETWORK_TYPE_EVDO_A:
	        case TelephonyManager.NETWORK_TYPE_HSDPA:
	        case TelephonyManager.NETWORK_TYPE_HSUPA:
	        case TelephonyManager.NETWORK_TYPE_HSPA:
	        case TelephonyManager.NETWORK_TYPE_EVDO_B:
	        case TelephonyManager.NETWORK_TYPE_EHRPD:
	        case TelephonyManager.NETWORK_TYPE_HSPAP:
	            return "3G";
	        case TelephonyManager.NETWORK_TYPE_LTE:
	            return "4G";
	        default:
	            return "wwan";
	    }
	}

	public int getOperatorByAPN(int apn) {
		int operator = 0;

		switch (apn) {
		case 1:
		case 2:
		case 10:
			operator = 1;
			break;
		case 3:
		case 4:
		case 5:
		case 6:
		case 11:
			operator = 2;
			break;
		case 8:
		case 9:
		case 12:
			operator = 3;
			break;
		case 7:
			operator = 4;
			break;
		default:
			operator = 0;
		}

		return operator;
	}

	public int getAPNValue() {
		Integer apn = null;
		String apnName = getAPN();
		if (apnName != null) {
			apn = (Integer) this.mApnMap.get(apnName);
		}

		return ((apn != null) ? apn.intValue() : 0);
	}

	public boolean isViaWIFI() {
		NetworkInfo activeNetInfo = null;
		try {
			activeNetInfo = this.mConnectivityMgr.getActiveNetworkInfo();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ((activeNetInfo != null) && (activeNetInfo.getType() == 1));
	}

	public ApnNode getDefaultAPN() {
		String apn = "";
		String name = "";

		ApnNode apnNode = new ApnNode();

		if (Build.VERSION.SDK_INT < this.JELLY_BEAN_NEW) {
			Cursor mCursor = null;
			try{
				mCursor = this.mContext.getContentResolver().query(
						PREFERRED_APN_URI, null, null, null, null);
				if (mCursor == null) {
					return null;
				}
	
				mCursor.moveToFirst();
				while (!mCursor.isAfterLast()) {
					name = mCursor.getString(mCursor.getColumnIndex("name"));
					apn = mCursor.getString(mCursor.getColumnIndex("apn"));
					if(apn != null){
						apn = apn.toLowerCase();
					}
					mCursor.moveToNext();
				}
			}catch(Throwable ex){
				ex.printStackTrace();
				return null;
			}finally{
				if(mCursor != null){
					mCursor.close();
				}
			}
		} else {
			NetworkInfo activeNetInfo = this.mConnectivityMgr.getActiveNetworkInfo();
			if (activeNetInfo != null) {
				if (activeNetInfo.getType() == 0) {
					if (activeNetInfo.getExtraInfo() != null) {
						apn = activeNetInfo.getExtraInfo().toLowerCase();
					}
				} else if (activeNetInfo.getType() == 1) {
					apn = "wifi";
				}
			}

		}

		apnNode.setName(name);
		apnNode.setApn(apn);

		return apnNode;
	}

	public long getExternalStorageSpace() {
		boolean hasExternalStorage = "mounted".equals(Environment.getExternalStorageState());

		if (!(hasExternalStorage)) {
			return -1L;
		}

		try {
			StatFs fileSystem = new StatFs(Environment.getExternalStorageDirectory().getPath());

			long blockSize = fileSystem.getBlockSize();
			long avalibleBlocks = fileSystem.getAvailableBlocks();

			long avalibleSpace = blockSize * avalibleBlocks;

			Log.i(TAG, "外部存储(SDCard)剩余空间: " + avalibleSpace + "b");

			return avalibleSpace;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

	public String getDeviceName() {
		return Build.MODEL + "(" + Build.VERSION.RELEASE + ")";
	}

	public String getDeviceInfo(boolean refresh) {
		if ((this.mDeviceInfo != null) && (this.mDeviceInfo.length() > 0) && (!(refresh))) {
			return this.mDeviceInfo;
		}

		DisplayMetrics displayMetrics = new DisplayMetrics();
		WindowManager manager = (WindowManager) this.mContext.getSystemService("window");
		if (manager != null) {
			manager.getDefaultDisplay().getMetrics(displayMetrics);
		}
		
		StringBuilder builder = new StringBuilder();

		try {
			builder.append("imei=").append(this.mTelephonyMgr.getDeviceId()).append('&');
		} catch (Exception e) {
			e.printStackTrace();
		}
		builder.append("model=").append(Build.MODEL).append('&');
		builder.append("os=").append(Build.VERSION.RELEASE).append('&');
		builder.append("apilevel=").append(Build.VERSION.SDK_INT).append('&');

		String apn = getAPN();
		apn = (apn == null) ? "[-]" : apn;

		builder.append("network=").append(apn).append('&');
		builder.append("sdcard=").append((Environment.getExternalStorageState().equals("mounted")) ? 1 : 0).append('&');
		builder.append("sddouble=").append("0").append('&');
		builder.append("display=").append(displayMetrics.widthPixels).append('*').append(displayMetrics.heightPixels).append('&');
		builder.append("manu=").append(Build.MANUFACTURER).append("&");
		builder.append("wifi=").append(getWifiInfo()).append("&");
		builder.append("dns=").append(getDNSInfo()).append("&");
		builder.append("storage=").append(getStorageInfo(true));
        builder.append("cpu=").append(getCpuName());

		this.mDeviceInfo = builder.toString();

		Log.i(TAG, this.mDeviceInfo);

		return this.mDeviceInfo;
	}

	static public String getCpuString(){
		if(Build.CPU_ABI.equalsIgnoreCase("x86")){
			return "Intel";
		}
		
		String strInfo = "";
		RandomAccessFile reader = null;
		try
		{
			byte[] bs = new byte[1024];
			reader = new RandomAccessFile("/proc/cpuinfo", "r");
			reader.read(bs);
			String ret = new String(bs);
			int index = ret.indexOf(0);
			if(index != -1) {
				strInfo = ret.substring(0, index);
			} else {
				strInfo = ret;
			}
			
			reader.close();
		}
		catch (IOException ex){
			ex.printStackTrace();
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return strInfo;
	}
	
	static public String getCpuType(){
		String strInfo = getCpuString();
		String strType = null;
		
		if (PhoneProperty.instance().isFakeArmV5()) {
			strType = "armv5";
		} else if (strInfo.contains("ARMv5") || strInfo.contains("v5l")) {
			strType = "armv5";
		} else if (strInfo.contains("ARMv6") || strInfo.contains("v6l")) {
			strType = "armv6";
		} else if (strInfo.contains("ARMv7")) {
			strType = "armv7";
		} else if (strInfo.contains("Intel")) {
			strType = "x86";
		} else {
			strType = "unknown";
		}
		return strType;
	}
	
	
	public static String getCpuName() {
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader("/proc/cpuinfo");
			br = new BufferedReader(fr);

			String text = br.readLine();
			if (text == null) {
				fr.close();
				br.close();
				
				return "unknown";
			}

			String[] array = text.split(":\\s+", 2);
			fr.close();
			br.close();
			if (array.length >= 2) {
				return array[1];
			}
			
			return "unknown";
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			if(fr != null){
				try {
					fr.close();
				} catch (IOException e1) {
					e.printStackTrace();
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e1) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			if (fr != null) {
				try {
					fr.close();
				} catch (IOException e1) {
					e.printStackTrace();
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e1) {
					e.printStackTrace();
				}
			}
		}

		return "unknown";
	}

	public void lockWifi() {
		Log.i(TAG, "Taking wifi lock");
		
		unlockWifi();
		if (this.mWifiLock == null) {
			WifiManager manager = (WifiManager) this.mContext.getSystemService("wifi");
			this.mWifiLock = manager.createWifiLock("com.tencent.weibo.omas");
			this.mWifiLock.setReferenceCounted(false);
		}
		
		this.mWifiLock.acquire();
	}

	public void unlockWifi() {
		Log.i(TAG, "Releasing wifi lock");
		if (this.mWifiLock != null) {
			this.mWifiLock.release();
			this.mWifiLock = null;
		}
	}

	public boolean is2GRadio() {
		NetworkInfo info = this.mConnectivityMgr.getActiveNetworkInfo();

		if ((info != null) && (info.getType() == 0)) {
			int subtype = info.getSubtype();
			if ((subtype == 2) ||
			(subtype == 1) ||
			(subtype == 11))
				return true;
			if ((subtype == 7) ||
			(subtype == 4) ||
			(subtype == 5) ||
			(subtype == 6) ||
			(subtype == 8) ||
			(subtype == 10) ||
			(subtype == 9) ||
			(subtype == 3)) {
				return false;
			}
		}

		return false;
	}

	public String getStorageInfo(boolean force) {
		if ((!(force)) && (this.storageInfo != null)) {
			return this.storageInfo;
		}

		String innerInfo = getInnerStorageInfo();
		String extInfo = getExternalStorageInfo();

		String resu = String.format("[IN : %s |EXT: %s]",
				new Object[] { innerInfo, extInfo });

		Log.i(TAG, "Storage Info = " + resu);

		return resu;
	}

	public String getInnerStorageInfo() {
		if (this.mContext == null) {
			return "NONE";
		}
		
		StatFs fileSystem;
		try {
			fileSystem = new StatFs(this.mContext.getFilesDir().getAbsolutePath());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "NONE";
		}

		long blockSize = fileSystem.getBlockSize();
		long avalibleBlocks = fileSystem.getAvailableBlocks();
		long allBlocks = fileSystem.getBlockCount();

		long avalibleSpace = blockSize * avalibleBlocks;

		long allSpace = blockSize * allBlocks;

		long MB = 1048576L;

		return String.format(
				"%d/%dMB (%d%%)",
				new Object[] {
						Long.valueOf(avalibleSpace / 1048576L),
						Long.valueOf(allSpace /
						1048576L),
						Long.valueOf(Math.round(avalibleSpace * 100.0D
								/ allSpace * 1.0D)) });
	}

	public String getExternalStorageInfo() {
		boolean hasExternalStorage = "mounted"
		.equals(Environment.getExternalStorageState());

		if (!(hasExternalStorage)) {
			return "NONE";
		}
		StatFs fileSystem;
		try {
			fileSystem = new StatFs(
			Environment.getExternalStorageDirectory().getPath());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "NONE";
		}

		long blockSize = fileSystem.getBlockSize();
		long avalibleBlocks = fileSystem.getAvailableBlocks();
		long allBlocks = fileSystem.getBlockCount();

		long avalibleSpace = blockSize * avalibleBlocks;

		long allSpace = blockSize * allBlocks;

		long MB = 1048576L;

		return String.format(
				"%d/%dMB (%d%%)",
				new Object[] {
						Long.valueOf(avalibleSpace / 1048576L),
						Long.valueOf(allSpace /
						1048576L),
						Long.valueOf(Math.round(avalibleSpace * 100.0D
								/ allSpace * 1.0D)) });
	}

	public String getWifiInfo() {
		if (this.mContext == null) {
			return "";
		}

		WifiManager wifiManager = (WifiManager) this.mContext.getSystemService("wifi");
		if (wifiManager == null) {
			return "[-]";
		}

        WifiInfo wifiInfo = null;
        try {
            wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo == null) {
                return "[-]";
            }
        }catch(Throwable e){
            // 此处会发生 ava.lang.NullPointerException: name == null 错误
            return "[-]";
        }


		String ssid = wifiInfo.getSSID();
		String signal = String.valueOf(WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 5));

		String speed = String.valueOf(wifiInfo.getLinkSpeed()) + " " + "Mbps";
		StringBuffer buffer = new StringBuffer();
		buffer.append('[').append(signal).append(", ").append(ssid).append(", ").append(speed).append(']');
		String resu = buffer.toString();

		Log.i(TAG, "Wifi Info = " + resu);

		return resu;
	}

	public String getGSMSignal() {
		String resu = "[-]";

		if (this.currentASU != -2147483648)
			resu = String.valueOf(this.currentASU);
		else {
			resu = "[-]";
		}

		Log.i(TAG, "GSM Signal = " + resu);

		return resu;
	}

	public static class DNSInfo {
		protected String currentPreferredDNS = null;
		protected String currentAlternativeDNS = null;
		protected String wifiPreferredDNS = null;
		protected String wifiAlternativeDNS = null;

		protected static DNSInfo DNS = null;

		public static DNSInfo getDNSInfo(Context context) {
			if (DNS != null) {
				return DNS;
			}
			return refreshDNSInfo(context);
		}

		public static DNSInfo refreshDNSInfo(Context context) {
			if (DNS == null)
				DNS = new DNSInfo();
			else {
				DNS.reset();
			}

//			Console.ConsoleResult systemDNS1 = Console.executeCommand("getprop net.dns1", 2000L);
//			Console.ConsoleResult systemDNS2 = Console.executeCommand("getprop net.dns2", 2000L);
//
//			DNS.setCurrentPreferredDNS(systemDNS1.toString());
//			DNS.setCurrentAlternativeDNS(systemDNS2.toString());

			WifiManager wifiManager = null;

			if (context == null) {
				context = AndroidDeviceManager.Instance().mContext;
			}

			if (context != null) {
				wifiManager = (WifiManager) context.getSystemService("wifi");
			}

			if (wifiManager != null) {
				DhcpInfo info = wifiManager.getDhcpInfo();

				if (info != null) {
					DNS.setWifiPreferredDNS(int32ToIPStr(info.dns1));
					DNS.setWifiAlternativeDNS(int32ToIPStr(info.dns2));
				}
			}

			if (DNS != null)
				Log.i(AndroidDeviceManager.TAG, "DNS Info = " + DNS.toString());
			else {
				Log.i(AndroidDeviceManager.TAG, "DNS Info = [-]");
			}

			return DNS;
		}

		public static String int32ToIPStr(int ip) {
			StringBuffer buffer = new StringBuffer();

			buffer.append(ip & 0xFF).append(".");
			buffer.append(ip >> 8 & 0xFF).append(".");
			buffer.append(ip >> 16 & 0xFF).append(".");
			buffer.append(ip >> 24 & 0xFF);

			return buffer.toString();
		}

		public void reset() {
			setCurrentPreferredDNS(null);
			setCurrentAlternativeDNS(null);
			setWifiPreferredDNS(null);
			setWifiAlternativeDNS(null);
		}

		public String toString() {
			StringBuffer buffer = new StringBuffer();

			buffer.append(getCurrentPreferredDNS()).append(",")
			.append(getCurrentAlternativeDNS()).append(";")
			.append(getWifiPreferredDNS()).append(",")
			.append(getWifiAlternativeDNS()).append(";");

			return buffer.toString();
		}

		public String getCurrentPreferredDNS() {
			return this.currentPreferredDNS;
		}

		public void setCurrentPreferredDNS(String currentPreferredDNS) {
			this.currentPreferredDNS = currentPreferredDNS;
		}

		public String getCurrentAlternativeDNS() {
			return this.currentAlternativeDNS;
		}

		public void setCurrentAlternativeDNS(String currentAlternativeDNS) {
			this.currentAlternativeDNS = currentAlternativeDNS;
		}

		public String getWifiPreferredDNS() {
			return this.wifiPreferredDNS;
		}

		public void setWifiPreferredDNS(String wifiPreferredDNS) {
			this.wifiPreferredDNS = wifiPreferredDNS;
		}

		public String getWifiAlternativeDNS() {
			return this.wifiAlternativeDNS;
		}

		public void setWifiAlternativeDNS(String wifiAlternativeDNS) {
			this.wifiAlternativeDNS = wifiAlternativeDNS;
		}
	}

	public static interface WifiNetChangeReceiver {
		void onWifiNetChanged(Boolean connected, int networkType);
	}
	
	public class NetworkChangeReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			//L.e(AndroidDeviceManager.TAG, "NetworkChangeReceiver onReceive()");

			if (false == "android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
				return;
			}
			
			NetworkInfo ni = (NetworkInfo) intent.getParcelableExtra("networkInfo");
			if (ni == null) {
				Log.e(AndroidDeviceManager.TAG, "onReceive NetworkInfo ni == null");
				return;
			}
			
			//TODO 没用到？
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					getDeviceInfo(true);
				}
			}).start();
			

			Log.i(TAG, ni.toString());
			if (ni.getState() == NetworkInfo.State.CONNECTED) {
				if (ni.getType() == 1) {
					AndroidDeviceManager.this.mCurrentAPN = ni.getExtraInfo();
					Log.i(AndroidDeviceManager.TAG, "wifi connected,mCurrentAPN = "
					         + AndroidDeviceManager.this.mCurrentAPN);
					
					
					if (mWifiConnected == false) {
						AndroidDeviceManager.this.lockWifi();
					}
					
					mWifiConnected = true;
				} else if (ni.getType() == 0) {
					AndroidDeviceManager.this.mCurrentAPN = ni.getExtraInfo();
					Log.i(AndroidDeviceManager.TAG, "mobile connected,mCurrentAPN = "
									+ AndroidDeviceManager.this.mCurrentAPN);
					
					mMobileConnected = true;
					AndroidDeviceManager.this.unlockWifi();
				}
				// 网络恢复连接
				if (null != mWifiNetChangeReceiver) {
					mWifiNetChangeReceiver.onWifiNetChanged(true, ni.getType());
				}
			} else if (ni.getState() == NetworkInfo.State.DISCONNECTED) {
				// 网络断开链接
				if (null != mWifiNetChangeReceiver) {
					mWifiNetChangeReceiver.onWifiNetChanged(false, ni.getType());
				}
				
				if (ni.getType() == 1) {
					mWifiConnected = false;
				} else if (ni.getType() == 0) {
					mMobileConnected = false;
				}
				AndroidDeviceManager.this.unlockWifi();
			}
		}
	}
}

