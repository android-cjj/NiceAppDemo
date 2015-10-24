package com.shine.niceapp.utils;

import android.util.Log;

public class LogUtil {
    public static int DEBUG;
    public static int ERROR;
    public static int INFO;
    public static int VERBOSE = 1;
    public static int WARN;
    private static boolean isLog;
    private static int level;

    static {
        DEBUG = 2;
        INFO = 3;
        WARN = 4;
        ERROR = 5;
        level = VERBOSE;
        isLog = true;
    }

    public static void d(String paramString) {
        if ((isPrintLog()) && (level <= DEBUG))
            Log.d("LogUtil", paramString + "");
    }

    public static void d(String paramString1, String paramString2) {
        if ((isPrintLog()) && (level <= DEBUG))
            Log.d(paramString1, paramString2 + "");
    }

    public static void e(String paramString) {
        if ((isPrintLog()) && (level <= ERROR))
            Log.e("LogUtil", paramString + "");
    }

    public static void e(String paramString1, String paramString2) {
        if ((isPrintLog()) && (level <= ERROR))
            Log.e(paramString1, paramString2 + "");
    }

    public static void e(String paramString, Throwable paramThrowable) {
        if ((isPrintLog()) && (level <= ERROR))
            Log.e("LogUtil", paramString + "", paramThrowable);
    }

    public static void i(String paramString) {
        if ((isPrintLog()) && (level <= INFO))
            Log.i("LogUtil", paramString + "");
    }

    public static void i(String paramString1, String paramString2) {
        if ((isPrintLog()) && (level <= INFO))
            Log.i(paramString1, paramString2 + "");
    }

    public static boolean isPrintLog() {
        return isLog;
    }

    public static void v(String paramString) {
        if ((isPrintLog()) && (level <= VERBOSE))
            Log.v("LogUtil", paramString + "");
    }

    public static void v(String paramString1, String paramString2) {
        if ((isPrintLog()) && (level <= VERBOSE))
            Log.v(paramString1, paramString2 + "");
    }

    public static void w(String paramString) {
        if ((isPrintLog()) && (level <= WARN))
            Log.w("LogUtil", paramString + "");
    }

    public static void w(String paramString1, String paramString2) {
        if ((isPrintLog()) && (level <= WARN))
            Log.w(paramString1, paramString2 + "");
    }
}