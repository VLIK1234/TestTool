package amtt.epam.com.amtt.util;

import android.text.TextUtils;
import android.util.Base64;
import amtt.epam.com.amtt.util.Constants.SharedPreference;

import amtt.epam.com.amtt.api.JiraApiConst;

/**
 * Created by Iryna_Monchanka on 4/8/2015.
 */
public class ActiveUser {

    private static final ActiveUser INSTANCE = new ActiveUser();

    private String mUserName;
    private String mPassword;
    private String mUrl;

    private ActiveUser() {
    }

    public static ActiveUser getInstance() {
        return INSTANCE;
    }

    public String getCredentials() {
        return PreferenceUtils.getString(SharedPreference.CREDENTIALS);
    }

    public String makeTempCredentials(final String userName, final String password) {
        return JiraApiConst.BASIC_AUTH + Base64.encodeToString((userName + Constants.Str.COLON + password).getBytes(), Base64.NO_WRAP);
    }

    public String getUserName() {
        if (mUserName == null) {
            mUserName = PreferenceUtils.getString(Constants.SharedPreference.USER_NAME);
        }
        return mUserName;
    }

    private String getPassword() {
        return mPassword;
    }

    public String getUrl() {
        return mUrl;
    }

    public boolean getAccessState() {
        return !TextUtils.isEmpty(getUserName()) && !TextUtils.isEmpty(getPassword());
    }


    public void setUrl(String url) {
        mUrl = url;
    }

    public void setAccess(Boolean url) {
        PreferenceUtils.putBoolean(Constants.SharedPreference.ACCESS, url);
    }

    public void setCredentials(String userName, String password) {
        String credentialsString = JiraApiConst.BASIC_AUTH + Base64.encodeToString((mUserName + Constants.Str.COLON + mPassword).getBytes(), Base64.NO_WRAP);
        PreferenceUtils.putString(Constants.SharedPreference.CREDENTIALS, credentialsString);
    }

    public void setUserName(String userName) {
        PreferenceUtils.putString(Constants.SharedPreference.USER_NAME, userName);
        mUserName = userName;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

}

