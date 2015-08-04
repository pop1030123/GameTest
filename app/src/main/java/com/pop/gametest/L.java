package com.pop.gametest;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by zcao on 4/5/14.
 */
public class L {
    private static final String TAG_PREFIX = "GameTest:";

    public static final String SYS_TAG = "SYSTEM";
    public static final String API_TAG = "API_DATA";
    public static final String UI_TAG = "UI";
    public static final String VIEW_TAG = "VIEW";
    public static final String ACTIVITY_TAG = "ACTIVITY";
    public static final String DBG_TAG = "DEBUG";
    public static final String IAP_TAG = "IAP";
    public static final String AD_TAG = "ADS";

    public static final String V_C_TAG = "VideoCall:";

    private static HashMap<String, Boolean> mDebugMap = new HashMap<String, Boolean>();

    private static void add(String tag, boolean debug) {
        mDebugMap.put(tag, debug);
    }

    private static boolean debug(String tag) {
        Boolean dbg = mDebugMap.get(tag);

        if (dbg == null) return true;

        return dbg;
    }

    static {
        add(SYS_TAG, true);
        add(API_TAG, true);
        add(UI_TAG, true);
        add(ACTIVITY_TAG, true);
        add(DBG_TAG, true);
        add(IAP_TAG, true);
        add(VIEW_TAG, true);
        add(AD_TAG, true);
    }

    public static void d(String tag, String info) {
        if (debug(tag)) {
            Log.d(TAG_PREFIX + tag, info);
        }
    }

    public static void e(String tag, String info) {
        if (debug(tag)) {
            Log.d(TAG_PREFIX + tag, info);
        }
    }

    public static void e(String tag, Exception e) {
        e(tag, "", e);
    }

    public static void e(String tag, String info, Exception e) {
        if (debug(tag)) {
            Log.e(TAG_PREFIX + tag, info + Log.getStackTraceString(e));
        }
    }
}