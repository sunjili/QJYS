package com.rmtech.qjys.zbarlib.decode;

import java.util.concurrent.CountDownLatch;

import android.os.Handler;
import android.os.Looper;

import com.rmtech.qjys.ui.qjactivity.ScanActivity;

/**
 *
 * 描述: 解码线程
 */
final class DecodeThread extends Thread {

	ScanActivity activity;
	private Handler handler;
	private final CountDownLatch handlerInitLatch;

	DecodeThread(ScanActivity activity) {
		this.activity = activity;
		handlerInitLatch = new CountDownLatch(1);
	}

	Handler getHandler() {
		try {
			handlerInitLatch.await();
		} catch (InterruptedException ie) {
			// continue?
		}
		return handler;
	}

	@Override
	public void run() {
		Looper.prepare();
		handler = new DecodeHandler(activity);
		handlerInitLatch.countDown();
		Looper.loop();
	}

}
