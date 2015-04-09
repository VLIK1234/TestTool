package amtt.epam.com.amtt.asynctask;

import amtt.epam.com.amtt.authorization.JiraApi;
import amtt.epam.com.amtt.bo.issue.createmeta.JMetaResponse;
import amtt.epam.com.amtt.callbacks.ShowUserDataCallback;
import amtt.epam.com.amtt.processing.ProjectsToJsonProcessor;
import android.os.AsyncTask;
import org.apache.http.HttpEntity;

/**
 * Created by Irina Monchenko on 30.03.2015.
 */
public class ShowUserDataTask extends AsyncTask<Void, Void, JMetaResponse> {

    private final ShowUserDataCallback mCallback;
    private final String mUserName;
    private final String mCredentials;
    private final String mUrl;
    private final String mTypeSearchData;

    public ShowUserDataTask(String username, String credentials, String url, String typeSearchData, ShowUserDataCallback callback) {
        mUserName = username;
        mCredentials = credentials;
        mCallback = callback;
        mUrl = url;
        mTypeSearchData = typeSearchData;
    }

    @Override
    protected JMetaResponse doInBackground(Void... params) {
        HttpEntity i;
        JMetaResponse jMetaResponse;
        try {

            i = new JiraApi().searchData(mUserName, mCredentials, mUrl, mTypeSearchData);
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
