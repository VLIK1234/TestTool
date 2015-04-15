package amtt.epam.com.amtt.util;

/**
 * Created by Iryna_Monchanka on 4/8/2015.
 */
public class CredentialsManager {

    private static final CredentialsManager INSTANCE = new CredentialsManager();
    private String password;

    private CredentialsManager() {
    }

    public static CredentialsManager getInstance() {
        return INSTANCE;
    }

    public String getUserName() {
        return PreferenceUtils.getString(Constants.SharedPreferenceKeys.USER_NAME, Constants.SharedPreferenceKeys.VOID);
    }

    public String getUrl() {
        return PreferenceUtils.getString(Constants.SharedPreferenceKeys.URL, Constants.SharedPreferenceKeys.VOID);
    }

    public boolean getAccessState() {
        // todo check for username and password
//        return PreferenceUtils.getBoolean(Constants.SharedPreferenceKeys.ACCESS, false);
        return getUrl() != null && password != null;//todo why get url? should be username. Use TextUtils.isEmpty()
    }

    public void setUserName(String userName) {
        PreferenceUtils.putString(Constants.SharedPreferenceKeys.USER_NAME, userName);
    }

    public void setUrl(String url) {
        PreferenceUtils.putString(Constants.SharedPreferenceKeys.URL, url);
    }

    public void setAccess(Boolean url) {
        PreferenceUtils.putBoolean(Constants.SharedPreferenceKeys.ACCESS, url);
    }

    public void setCredentials(String userName, String password) {
        this.password = password;
        setUserName(userName);
    }
}

