package com.bignerdranch.android.photogallery;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by lfs-ios on 2017/4/17.
 */

public class QueryPreference {

    private static final String PREF_SEARCH_QUERY = "searchQuery";

    private static final String PREF_LAST_RESULT_ID = "lastResultId";

    private static final String PREF_IS_ALARM_ON = "isAlarmOn";


    public static String getLastReultId(Context context) {


        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_LAST_RESULT_ID, null);

    }

    public static void setLastResultId(Context context, String query) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_LAST_RESULT_ID, query)
                .commit();
    }


    public static String getStoredQuery(Context context) {


        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_SEARCH_QUERY, null);

    }

    public static void setStoredQuery(Context context, String query) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_SEARCH_QUERY, query)
                .commit();
    }


    public static boolean isAlarmOn(Context context) {


        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_IS_ALARM_ON, false);

    }

    public static void setAlarmOn(Context context, boolean isOn) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_IS_ALARM_ON, isOn)
                .commit();
    }
}
