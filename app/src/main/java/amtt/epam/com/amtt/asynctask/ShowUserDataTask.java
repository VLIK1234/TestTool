package amtt.epam.com.amtt.asynctask;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;


import amtt.epam.com.amtt.authorization.JiraApi;
import amtt.epam.com.amtt.bo.issue.createmeta.JMetaResponse;

import amtt.epam.com.amtt.bo.issue.TypeSearchedData;

import amtt.epam.com.amtt.callbacks.ShowUserDataCallback;
import amtt.epam.com.amtt.processing.ProjectsToJsonProcessor;
import amtt.epam.com.amtt.util.CredentialsManager;

/**
 * Created by Irina Monchenko on 30.03.2015.
 */
public class ShowUserDataTask extends AsyncTask<Void, Void, JMetaResponse> {

    private final ShowUserDataCallback mCallback;
    private final String mUserName = CredentialsManager.getInstance().getUserName();
    private final TypeSearchedData mTypeSearchData;

    public ShowUserDataTask(TypeSearchedData typeSearchData, ShowUserDataCallback callback) {
        mCallback = callback;
        mTypeSearchData = typeSearchData;
    }

    @Override
    protected JMetaResponse doInBackground(Void... params) {
        JMetaResponse jMetaResponse = null;
        try {
            HttpEntity httpEntity = new JiraApi().searchData(mUserName, mTypeSearchData);
            ProjectsToJsonProcessor projects = new ProjectsToJsonProcessor();
            jMetaResponse = projects.process(httpEntity);
        } catch (Exception e) {
            //ignored
        }
        return jMetaResponse;
    }

    @Override
    protected void onPostExecute(JMetaResponse result) {
        mCallback.onShowUserDataResult(result);
    }
}
