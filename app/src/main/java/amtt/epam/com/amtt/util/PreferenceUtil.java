package amtt.epam.com.amtt.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import amtt.epam.com.amtt.CoreApplication;

public class PreferenceUtil {

    private static SharedPreferences mSharedPreferences = null;

    public PreferenceUtil() {
    }

    public static String getString(String key) {
        return getPref().getString(key, Constants.Symbols.EMPTY);
    }

    public static void putString(String key, String value) {
        getPref().edit().putString(key, value).apply();
    }

    public static void putInt(String key, int value) {
        getPref().edit().putInt(key, value).apply();
    }

    public static int getInt(String key, int value) {
        return getPref().getInt(key, value);
    }

    public static boolean getBoolean(String key) {
        return getPref().getBoolean(key, false);
    }

    public static void putBoolean(String key, boolean value) {
        getPref().edit().putBoolean(key, value).apply();
    }

    public static SharedPreferences getPref() {
        if (mSharedPreferences == null) {
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(CoreApplication.getContext());
        }
        return mSharedPreferences;
    }
}