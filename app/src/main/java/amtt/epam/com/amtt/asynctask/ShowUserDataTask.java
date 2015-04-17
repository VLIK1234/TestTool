package amtt.epam.com.amtt.asynctask;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;

import amtt.epam.com.amtt.api.JiraApi;
import amtt.epam.com.amtt.bo.issue.TypeSearchedData;
import amtt.epam.com.amtt.bo.issue.createmeta.JMetaResponse;
import amtt.epam.com.amtt.callbacks.ShowUserDataCallback;
import amtt.epam.com.amtt.processing.ProjectsToJsonProcessor;
import amtt.epam.com.amtt.util.CredentialsManager;
import amtt.epam.com.amtt.util.Kontext;

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
        HttpEntity i;
        JMetaResponse jMetaResponse;
        try {

            i = JiraApi.INSTANCE.searchData(mUserName, mTypeSearchData);
            ProjectsToJsonProcessor projects = new ProjectsToJsonProcessor();
            jMetaResponse = projects.process(i);

        } catch (Exception e) {
            //TODO if we get exception with processing, what shall happen to HttpEntity?
            jMetaResponse = null;
        }
        return jMetaResponse;
    }

    @Override
    protected void onPostExecute(JMetaResponse result) {
        //TODO What if someone passes callback and nulls it while processing?
        mCallback.onShowUserDataResult(result);
    }
}
