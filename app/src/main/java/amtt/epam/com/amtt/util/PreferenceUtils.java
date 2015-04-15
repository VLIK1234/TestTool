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

    public static boolean getBoolean(String key, boolean defaultValue) {
        return getPref().getBoolean(key, defaultValue);
    }

    public static String getString(String key, String defaultValue) {
        return getPref().getString(key, defaultValue);
    }

    public static Set<String> getSet(String key, Set<String> defaultValue) {
        return getPref().getStringSet(key, defaultValue);
    }

    public static void putSet(String key, Set<String> value) {
        SharedPreferences.Editor editor = getPref().edit();
        editor.putStringSet(key, value);
        editor.apply();
    }

    public static void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = getPref().edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void putString(String key, String value) {
        SharedPreferences.Editor editor = getPref().edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static SharedPreferences getPref() {
        if (mSharedPreferences == null) {
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(Kontext.getKontext());
        }
        return mSharedPreferences;
    }

    public static void registerListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        getPref().registerOnSharedPreferenceChangeListener(listener);
    }

    public static void unregisterListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        getPref().unregisterOnSharedPreferenceChangeListener(listener);
    }
}