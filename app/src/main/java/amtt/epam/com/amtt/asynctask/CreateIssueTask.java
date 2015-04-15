package amtt.epam.com.amtt.asynctask;

import android.os.AsyncTask;

import org.apache.http.auth.AuthenticationException;

import amtt.epam.com.amtt.api.JiraApi;
import amtt.epam.com.amtt.bo.issue.willrefactored.CreationIssueResult;
import amtt.epam.com.amtt.callbacks.CreationIssueCallback;

/**
 * Created by Irina Monchenko on 27.03.2015.
 */
public class CreateIssueTask extends AsyncTask<Void, Void, CreationIssueResult> {

    private final CreationIssueCallback mCallback;
    private final String mJson;

    public CreateIssueTask(String json, CreationIssueCallback callback) {
        mCallback = callback;
        mJson = json;
    }

    @Override
    protected CreationIssueResult doInBackground(Void... params) {
        //TODO update for amtt/epam/com/amtt/authorization/JiraApi.java:58
        return JiraApi.INSTANCE.createIssue(mJson);
    }

    @Override
    protected void onPostExecute(CreationIssueResult result) {
        if (mCallback!=null) {
            mCallback.onCreationIssueResult(result);
        }
    }
}
