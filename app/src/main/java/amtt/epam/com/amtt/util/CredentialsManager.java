package amtt.epam.com.amtt.util;

import android.content.Context;

/**
 * Created by Iryna_Monchanka on 4/8/2015.
 */
public class CredentialsManager {

    private static final CredentialsManager INSTANCE = new CredentialsManager();

    private String mUserName;
    private String mPassword;
    private String mUrl;
    private Boolean mAccess;


    private CredentialsManager() {
    }

    public static CredentialsManager getInstance() {
        return INSTANCE;
    }

    public String getUserName(Context context) {
        mUserName = PreferenceUtils.getString(Constants.USER_NAME, Constants.VOID, context);
        return mUserName;
    }

    public String getPassword(Context context) {
        mPassword = PreferenceUtils.getString(Constants.PASSWORD, Constants.VOID, context);
        return mPassword;
    }

    public String getUrl(Context context) {
        mUrl = PreferenceUtils.getString(Constants.URL, Constants.VOID, context);
        return mUrl;
    }

    public Boolean getAccess(Context context) {
        mAccess = PreferenceUtils.getBoolean(Constants.ACCESS, false, context);
        return mAccess;
    }

    public void setUserName(String userName, Context context) {
        PreferenceUtils.putString(Constants.USER_NAME, userName, context);
    }

    public void setPassword(String password, Context context) {
        PreferenceUtils.putString(Constants.PASSWORD, password, context);
    }

    public void setUrl(String url, Context context) {
        PreferenceUtils.putString(Constants.URL, url, context);
    }

    public void setAccess(Boolean url, Context context) {
        PreferenceUtils.putBoolean(Constants.ACCESS, url, context);
    }
}

