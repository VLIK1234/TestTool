package amtt.epam.com.amtt.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Set;

import amtt.epam.com.amtt.R;

public class PreferenceUtils {

    private static SharedPreferences mSharedPreferences = null;

    public PreferenceUtils() {
    }

    public static boolean getBoolean(String key, boolean defaultValue, Context context) {
        return getPref(context).getBoolean(key, defaultValue);
    }

    public static String getString(String key, String defaultValue, Context context) {
        return getPref(context).getString(key, defaultValue);
    }

    public static Set<String> getSet(String key, Set<String> defaultValue, Context context) {
        return getPref(context).getStringSet(key, defaultValue);
    }

    public static void putSet(String key, Set<String> value, Context context) {
        SharedPreferences.Editor editor = getPref(context).edit();
        editor.putStringSet(key, value);
        editor.apply();
    }

    public static void putBoolean(String key, boolean value, Context context) {
        SharedPreferences.Editor editor = getPref(context).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void putString(String key, String value, Context context) {
        SharedPreferences.Editor editor = getPref(context).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static SharedPreferences getPref(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return mSharedPreferences;
    }


    public static void initDefaultPreference(Context context) {
        PreferenceManager.setDefaultValues(context, R.xml.preferences, false);
    }


    public static long getLong(String key, long defaultValue, Context context) {
        return getPref(context).getLong(key, defaultValue);
    }

    public static int getInt(String key, int defaultValue, Context context) {
        return getPref(context).getInt(key, defaultValue);
    }

    public static void putLong(String key, long value, Context context) {
        SharedPreferences.Editor editor = getPref(context).edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static void putInt(String key, int value, Context context) {
        SharedPreferences.Editor editor = getPref(context).edit();
        editor.putInt(key, value);
        editor.apply();
    }
}