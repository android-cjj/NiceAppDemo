package com.shine.niceapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;

public class NetWorkHelper {
    private static String LOG_TAG = "NetWorkHelper";
    public static Uri uri = Uri.parse("content://telephony/carriers");

    public static boolean isMobileDataEnable(Context paramContext)
            throws Exception {
        return ((ConnectivityManager) paramContext.getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(0).isConnectedOrConnecting();
    }


    public static boolean isWifiDataEnable(Context paramContext) {
        try {
            boolean bool = ((ConnectivityManager) paramContext.getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
            return bool;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}