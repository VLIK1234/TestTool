package amtt.epam.com.amtt.asynctask;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;

import amtt.epam.com.amtt.api.JiraApi;
import amtt.epam.com.amtt.bo.issue.TypeSearchedData;
import amtt.epam.com.amtt.bo.issue.user.JiraUserInfo;
import amtt.epam.com.amtt.callbacks.ShowUserInfoCallback;
import amtt.epam.com.amtt.processing.UserInfoToJsonProcessor;
import amtt.epam.com.amtt.util.CredentialsManager;
import amtt.epam.com.amtt.util.Kontext;

/**
 * Created by Irina Monchenko on 06.04.2015.
 */
public class ShowUserInfoTask extends AsyncTask<Void, Void, JiraUserInfo> {

    private final ShowUserInfoCallback mCallback;
    private final String mUserName = CredentialsManager.getInstance().getUserName();
    private final TypeSearchedData mTypeSearchData;

    public ShowUserInfoTask(TypeSearchedData typeSearchData, ShowUserInfoCallback callback) {
        mCallback = callback;
        mTypeSearchData = typeSearchData;
    }

    @Override
    protected JiraUserInfo doInBackground(Void... params) {
        HttpEntity i;
        JiraUserInfo jiraUserInfo;
        try {
            i = JiraApi.INSTANCE.searchData(mUserName, mTypeSearchData);
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