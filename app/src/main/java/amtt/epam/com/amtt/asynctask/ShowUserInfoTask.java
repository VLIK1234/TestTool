package amtt.epam.com.amtt.asynctask;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;

import amtt.epam.com.amtt.authorization.JiraApi;
import amtt.epam.com.amtt.bo.issue.TypeSearchedData;
import amtt.epam.com.amtt.bo.issue.user.JiraUserInfo;
import amtt.epam.com.amtt.callbacks.ShowUserInfoCallback;
import amtt.epam.com.amtt.processing.UserInfoToJsonProcessor;

/**
 * Created by Irina Monchenko on 06.04.2015.
 */
public class ShowUserInfoTask extends AsyncTask<Void, Void, JiraUserInfo> {

    private final ShowUserInfoCallback mCallback;
    private final String mUserName;
    private final String mCredentials;
    private final String mUrl;
    private final TypeSearchedData mTypeSearchData;

    public ShowUserInfoTask(String username, String credentials, String url, TypeSearchedData typeSearchData, ShowUserInfoCallback callback) {
        mUserName = username;
        mCredentials = credentials;
        mCallback = callback;
        mUrl = url;
        mTypeSearchData = typeSearchData;
    }

    @Override
    protected JiraUserInfo doInBackground(Void... params) {
        HttpEntity i;
        JiraUserInfo jiraUserInfo;
        try {
            i = new JiraApi().searchData(mUserName, mCredentials, mUrl, mTypeSearchData);
            UserInfoToJsonProcessor projects = new UserInfoToJsonProcessor();
            jiraUserInfo = projects.process(i);

        } catch (Exception e) {
            jiraUserInfo = null;
        }
        return jiraUserInfo;
    }

    @Override
    protected void onPostExecute(JiraUserInfo result) {
        mCallback.onShowUserInfoResult(result);
    }
}
