package amtt.epam.com.amtt.util;

import android.util.Base64;

import amtt.epam.com.amtt.api.JiraApiConst;
import amtt.epam.com.amtt.util.Constants.SharedPreference;

/**
 @author Iryna Monchanka
 @version on 4/8/2015
 */

public class ActiveUser {
    private static final ActiveUser INSTANCE = new ActiveUser();

    private String mUserName;
    private String mUrl;
    private int mId;

    private ActiveUser() {
    }

    public static ActiveUser getInstance() {
        return INSTANCE;
    }

    public String getCredentials() {
        return PreferenceUtils.getString(SharedPreference.CREDENTIALS);
    }

    public String makeTempCredentials(final String userName, final String password) {
        return JiraApiConst.BASIC_AUTH + Base64.encodeToString((userName + Constants.Symbols.COLON + password).getBytes(), Base64.NO_WRAP);
    }

    public String getUserName() {
        if (mUserName == null) {
            mUserName = PreferenceUtils.getString(Constants.SharedPreference.USER_NAME);
        }
        return mUserName;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public void setCredentials(String userName, String password, String url) {
        mUserName = userName;
        mUrl = url;
        String credentialsString = JiraApiConst.BASIC_AUTH + Base64.encodeToString((mUserName + Constants.Symbols.COLON + password).getBytes(), Base64.NO_WRAP);
        PreferenceUtils.putString(Constants.SharedPreference.CREDENTIALS, credentialsString);
    }
    public void setCredentials(String credentials) {
       PreferenceUtils.putString(Constants.SharedPreference.CREDENTIALS, credentials);
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setUserName(String userName) {
        this.mUserName = userName;
    }

    public void clearActiveUser(){
       mUserName = null;
       mUrl = null;
       mId = 0;

    }
}

