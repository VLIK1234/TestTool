package amtt.epam.com.amtt.asynctask;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;

import amtt.epam.com.amtt.authorization.JiraApi;
import amtt.epam.com.amtt.bo.issue.createmeta.JMetaResponse;
import amtt.epam.com.amtt.callbacks.ShowUserDataCallback;
import amtt.epam.com.amtt.processing.ProjectsToJsonProcessor;

/**
 * Created by Irina Monchenko on 30.03.2015.
 */
public class ShowUserDataTask extends AsyncTask<Void, Void, JMetaResponse> {

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
    protected JMetaResponse doInBackground(Void... params) {
        HttpEntity i;
        JMetaResponse jMetaResponse;
        try {
            i = new JiraApi().searchIssue(mUserName, mPassword, mUrl);
            ProjectsToJsonProcessor projects = new ProjectsToJsonProcessor();
            jMetaResponse = projects.process(i);

        } catch (Exception e) {
            jMetaResponse = null;
        }
        return jMetaResponse;
    }

    @Override
    protected void onPostExecute(JMetaResponse result) {
        mCallback.onShowUserDataResult(result);
    }
}