package amtt.epam.com.amtt.util;

import android.content.Context;
import android.util.Base64;

/**
 * Created by Iryna_Monchanka on 4/8/2015.
 */
public class CredentialsManager {

    private static final CredentialsManager INSTANCE = new CredentialsManager();
    private String credentials;


    private CredentialsManager() {
    }

    public static CredentialsManager getInstance() {
        return INSTANCE;
    }

    public String getUserName(Context context) {
        return PreferenceUtils.getString(Constants.SharedPreferenceKeys.USER_NAME, Constants.SharedPreferenceKeys.VOID, context);
    }

    public String getUrl(Context context) {
        return PreferenceUtils.getString(Constants.SharedPreferenceKeys.URL, Constants.SharedPreferenceKeys.VOID, context);
    }

    public Boolean getAccessState(Context context) {
        return PreferenceUtils.getBoolean(Constants.SharedPreferenceKeys.ACCESS, false, context);
    }

    public void setUserName(String userName, Context context) {
        PreferenceUtils.putString(Constants.SharedPreferenceKeys.USER_NAME, userName, context);
    }

    public void setUrl(String url, Context context) {
        PreferenceUtils.putString(Constants.SharedPreferenceKeys.URL, url, context);
    }

    public void setAccess(Boolean url, Context context) {
        PreferenceUtils.putBoolean(Constants.SharedPreferenceKeys.ACCESS, url, context);
    }

    public void setCredentials(String userName, String password) {
        credentials = Constants.UrlKeys.BASIC_AUTH + Base64.encodeToString((userName + Constants.SharedPreferenceKeys.COLON + password).getBytes(), Base64.NO_WRAP);
    }

    public String getCredentials() {
        return credentials;
    }
}

