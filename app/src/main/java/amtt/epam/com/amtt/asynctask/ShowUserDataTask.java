package amtt.epam.com.amtt.asynctask;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;

import amtt.epam.com.amtt.authorization.JiraApi;
import amtt.epam.com.amtt.bo.issue.createmeta.JiraMetaResponse;
import amtt.epam.com.amtt.callbacks.ShowUserDataCallback;
import amtt.epam.com.amtt.processing.ProjectsToJsonProcessor;

/**
 * Created by Irina Monchenko on 30.03.2015.
 */
public class ShowUserDataTask extends AsyncTask<Void, Void, JiraMetaResponse> {

    private final ShowUserDataCallback mCallback;
    private final String mUserName;
    private final String mPassword;
    private final String mUrl;

    public ShowUserDataTask(String username, String userPassword, String url, ShowUserDataCallback callback) {
        mUserName = username;
        mPassword = userPassword;
        mCallback = callback;
        mUrl = url;
    }

    @Override
    protected JiraMetaResponse doInBackground(Void... params) {
        JiraMetaResponse jiraMetaResponse = null;
        try {
            HttpEntity httpEntity = new JiraApi().searchIssue(mUserName, mPassword, mUrl);
            ProjectsToJsonProcessor projects = new ProjectsToJsonProcessor();
            jiraMetaResponse = projects.process(httpEntity);
        } catch (Exception e) {
            //ignored
        }
        return jiraMetaResponse;
    }

    @Override
    protected void onPostExecute(JiraMetaResponse result) {
        mCallback.onShowUserDataResult(result);
    }
}
