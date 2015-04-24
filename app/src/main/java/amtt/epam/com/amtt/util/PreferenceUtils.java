package amtt.epam.com.amtt.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Set;

public class PreferenceUtils {

    private static SharedPreferences mSharedPreferences = null;

    public PreferenceUtils() {
    }

    public static boolean getBoolean(String key) {
        return getPref().getBoolean(key, false);
    }

    public static String getString(String key) {
        return getPref().getString(key, UtilConstants.SharedPreference.EMPTY_STRING);
    }

    public static Set<String> getSet(String key, Set<String> defaultValue) {
        return getPref().getStringSet(key, defaultValue);
    }

    public static void putSet(String key, Set<String> value) {
        getPref().edit().putStringSet(key, value).apply();
    }

    public static void putBoolean(String key, boolean value) {
        getPref().edit().putBoolean(key, value).apply();
    }

    public static void putString(String key, String value) {
        getPref().edit().putString(key, value).apply();
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