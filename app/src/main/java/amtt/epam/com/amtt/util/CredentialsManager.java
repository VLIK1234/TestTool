package amtt.epam.com.amtt.util;

import android.text.TextUtils;
import android.util.Base64;

import amtt.epam.com.amtt.api.JiraApiConst;

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

    public String getCredentials() {
        //TODO still we save password and encode it every time we need auth string.
        return JiraApiConst.BASIC_AUTH + Base64.encodeToString((CredentialsManager.getInstance().getUserName() +
                UtilConstants.SharedPreference.COLON + CredentialsManager.getInstance().getPassword()).getBytes(), Base64.NO_WRAP);
    }

    public String getCredentials(final String userName, final String password) {
        return JiraApiConst.BASIC_AUTH + Base64.encodeToString((userName + UtilConstants.SharedPreference.COLON + password).getBytes(), Base64.NO_WRAP);
    }

    public String getUserName() {
        return PreferenceUtils.getString(UtilConstants.SharedPreference.USER_NAME);
    }

    private String getPassword() {
        return PreferenceUtils.getString(UtilConstants.SharedPreference.PASSWORD);
    }

    public String getUrl() {
        return PreferenceUtils.getString(UtilConstants.SharedPreference.URL);
    }

    public boolean getAccessState() {
        return !TextUtils.isEmpty(getUserName()) && !TextUtils.isEmpty(getPassword());
    }


    public void setUrl(String url) {
        PreferenceUtils.putString(UtilConstants.SharedPreference.URL, url);
    }

    public void setAccess(Boolean url) {
        PreferenceUtils.putBoolean(UtilConstants.SharedPreference.ACCESS, url);
    }

    public void setCredentials(String userName, String password) {
        setPassword(password);
        setUserName(userName);
    }

    private void setUserName(String userName) {
        PreferenceUtils.putString(UtilConstants.SharedPreference.USER_NAME, userName);
    }

    private void setPassword(String password) {
        PreferenceUtils.putString(UtilConstants.SharedPreference.PASSWORD, password);
    }

}

