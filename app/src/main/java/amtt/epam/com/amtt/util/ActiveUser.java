package amtt.epam.com.amtt.util;

import android.util.Base64;

import java.util.ArrayList;
import java.util.List;

import amtt.epam.com.amtt.api.JiraApiConst;

/**
 @author Iryna Monchanka
 @version on 4/8/2015
 */

public class ActiveUser {
    private static final ActiveUser INSTANCE = new ActiveUser();

    private String mUserName;
    private String mUrl;
    private int mId;
    private String mLastProjectKey;
    private String mCredentialsString;
    private String mLastProjectName;
    private String mLastAssigneeName;
    private List<String> mLastComponentsIds;

    private ActiveUser() {
    }

    public static ActiveUser getInstance() {
        return INSTANCE;
    }

    public String makeTempCredentials(final String userName, final String password) {
        return JiraApiConst.BASIC_AUTH + Base64.encodeToString((userName + Constants.Symbols.COLON + password).getBytes(), Base64.NO_WRAP);
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        this.mUserName = userName;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public void setCredentials(String userName, String password, String url) {
        setUserName(userName);
        setUrl(url);
        setCredentials(JiraApiConst.BASIC_AUTH + Base64.encodeToString((getUserName() + Constants.Symbols.COLON + password).getBytes(), Base64.NO_WRAP));
    }

    public void setCredentials(String credentials) {
       this.mCredentialsString = credentials;
    }

    public String getCredentials() {
        return mCredentialsString;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getLastProjectKey() {
        return mLastProjectKey;
    }

    public void setLastProjectKey(String lastProjectKey) {
        this.mLastProjectKey = lastProjectKey;
    }

    public List<String> getLastComponentsIds() {
        return mLastComponentsIds;
    }

    public void setLastComponentsIds(List<String> lastComponentsIds) {
        this.mLastComponentsIds = lastComponentsIds;
    }

    public String getLastAssignee() {
        return mLastAssigneeName;
    }

    public void setLastAssigneeName(String lastAssigneeName) {
        this.mLastAssigneeName = lastAssigneeName;
    }

    public void setLastProjectName(String lastProjectName) {
        this.mLastProjectName = lastProjectName;
    }

    public void clearActiveUser() {
        setUserName(null);
        setUrl(null);
        setId(0);
        setLastProjectKey(null);
        setCredentials(null);
        setLastProjectName(null);
        setLastAssigneeName(null);
        setLastComponentsIds(null);
    }
}

