package com.xgd.launcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

public class DeviceStateReceiver extends BroadcastReceiver {

	private DeviceStateListener mCallback;
	boolean isDevice = false;
	public DeviceStateReceiver(DeviceStateListener listener) {
		mCallback = listener;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		
		if(intent.getAction().equals(intent.ACTION_MEDIA_REMOVED) ||
				intent.getAction().equals(intent.ACTION_MEDIA_BAD_REMOVAL)) {
			isDevice = false;
		} else if (intent.getAction().equals(intent.ACTION_MEDIA_MOUNTED)) {
			isDevice = true;
		}
		if (mCallback != null)
			mCallback.connected(isDevice);
	}

	public interface DeviceStateListener {
		public void connected(boolean isConnected);
	}
}
