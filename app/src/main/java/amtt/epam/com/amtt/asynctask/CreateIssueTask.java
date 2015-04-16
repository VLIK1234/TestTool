package amtt.epam.com.amtt.asynctask;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthenticationException;

import amtt.epam.com.amtt.authorization.JiraApi;
import amtt.epam.com.amtt.authorization.RestResponse;
import amtt.epam.com.amtt.bo.issue.willrefactored.CreationIssueResult;
import amtt.epam.com.amtt.callbacks.CreationIssueCallback;

/**
 * Created by Irina Monchenko on 27.03.2015.
 */
public class CreateIssueTask extends AsyncTask<Void, Void, RestResponse> {

    private final CreationIssueCallback mCallback;
    private final String mJson;
    private final String mUserName;
    private final String mPassword;
    private final String mUrl;

    public CreateIssueTask(String username, String userPassword, String url, String json, CreationIssueCallback callback) {
        mUserName = username;
        mPassword = userPassword;
        mCallback = callback;
        mJson = json;
        mUrl = url;
    }

    @Override
    protected RestResponse doInBackground(Void... params) {
        return new JiraApi().createIssue(mUserName, mPassword, mUrl, mJson);
    }

    @Override
    protected void onPostExecute(RestResponse restResponse) {
        mCallback.onCreationIssueResult(restResponse);
    }
}
