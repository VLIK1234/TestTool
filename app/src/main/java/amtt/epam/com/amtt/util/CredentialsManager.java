package amtt.epam.com.amtt.util;

import android.content.Context;
import android.util.Base64;

/**
 * Created by Iryna_Monchanka on 4/8/2015.
 */
public class CredentialsManager {

    private static final CredentialsManager INSTANCE = new CredentialsManager();


    private CredentialsManager() {
    }

    public static CredentialsManager getInstance() {
        return INSTANCE;
    }

    public String getUserName(Context context) {
        return PreferenceUtils.getString(Constants.USER_NAME, Constants.VOID, context);
    }

    public String getUrl(Context context) {
        return PreferenceUtils.getString(Constants.URL, Constants.VOID, context);
    }

    public Boolean getAccessState(Context context) {
        return PreferenceUtils.getBoolean(Constants.ACCESS, false, context);
    }

    public void setUserName(String userName, Context context) {
        PreferenceUtils.putString(Constants.USER_NAME, userName, context);
    }

    public void setUrl(String url, Context context) {
        PreferenceUtils.putString(Constants.URL, url, context);
    }

    public void setAccess(Boolean url, Context context) {
        PreferenceUtils.putBoolean(Constants.ACCESS, url, context);
    }

    public void setCredentials(String userName, String password, Context context) {
        String credentials = Constants.BASIC_AUTH + Base64.encodeToString((userName + Constants.COLON + password).getBytes(), Base64.NO_WRAP);
        PreferenceUtils.putString(Constants.CREDENTIALS, credentials, context);
    }

    public String getCredentials(Context context) {
        return PreferenceUtils.getString(Constants.CREDENTIALS, Constants.VOID, context);
    }
}

