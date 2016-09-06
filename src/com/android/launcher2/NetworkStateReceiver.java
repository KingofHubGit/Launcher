package com.android.launcher2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkStateReceiver extends BroadcastReceiver{

    private NetworkStateListener mCallback;
    public NetworkStateReceiver(NetworkStateListener listener){
        mCallback = listener;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
        NetworkInfo.State state = (networkInfo == null ? NetworkInfo.State.UNKNOWN : networkInfo.getState());
        boolean isConnected = (state == NetworkInfo.State.CONNECTED);
        if(mCallback != null)
            mCallback.connected(isConnected);
    }
    public interface NetworkStateListener{
        public void connected(boolean isConnected);
    }
}
