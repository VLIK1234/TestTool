package amtt.epam.com.amtt.asynctask;

import amtt.epam.com.amtt.authorization.JiraApi;
import amtt.epam.com.amtt.bo.issue.willrefactored.CreationIssueResult;
import amtt.epam.com.amtt.callbacks.CreationIssueCallback;
import android.os.AsyncTask;
import org.apache.http.auth.AuthenticationException;

/**
 * Created by Irina Monchenko on 27.03.2015.
 */
public class CreateIssueTask extends AsyncTask<Void, Void, CreationIssueResult> {

    private final CreationIssueCallback mCallback;
    private final String mJson;
    private final String mCredentials;
    private final String mUrl;

    public CreateIssueTask(String credentials, String url, String json, CreationIssueCallback callback) {
        mCredentials = credentials;
        mCallback = callback;
        mJson = json;
        mUrl = url;
    }

    @Override
    protected CreationIssueResult doInBackground(Void... params) {
        //TODO update for amtt/epam/com/amtt/authorization/JiraApi.java:58
        try {
            if (JiraApi.STATUS_CREATED != new JiraApi().createIssue(mCredentials, mUrl, mJson)) {
                throw new AuthenticationException("issue can`t be create");
            }
        } catch (Exception e) {

            return CreationIssueResult.CREATION_UNSUCCESS;
        }
        return CreationIssueResult.CREATION_SUCCESS;
    }

    @Override
    protected void onPostExecute(CreationIssueResult result) {
        //TODO can mCallback be null?
        mCallback.onCreationIssueResult(result);
    }
}
