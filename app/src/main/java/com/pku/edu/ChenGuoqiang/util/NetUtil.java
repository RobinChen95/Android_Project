package com.pku.edu.ChenGuoqiang.util;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtil {
    public static final int NETWORK_NONE = 0;
    public static final int NETWORK_WIFI = 1;
    public static final int NETWORK_MOBILE = 2;

    public static int getNetWorkState(Context context){
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo==null)return NETWORK_NONE;
        int nType = networkInfo.getType();
        if (nType==ConnectivityManager.TYPE_MOBILE)return NETWORK_MOBILE;
        else if (nType==ConnectivityManager.TYPE_WIFI)return NETWORK_WIFI;
        return NETWORK_NONE;
    }
}
