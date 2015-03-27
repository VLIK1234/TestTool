package amtt.epam.com.amtt.bo;

import amtt.epam.com.amtt.authorization.AuthorizationCallback;
import amtt.epam.com.amtt.authorization.AuthorizationResult;
import amtt.epam.com.amtt.authorization.JiraApi;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.auth.AuthenticationException;

/**
 * Created by shiza on 27.03.2015.
 */
public class CreateIssueTask  extends AsyncTask<Void, Void, CreationIssueResult> {


    private final CreationIssueCallback mCallback;
    private final String mJson;
    private final String mUserName;
    private final String mPassword;

    public CreateIssueTask(String username, String userPassword, String json, CreationIssueCallback callback) {
        mUserName = username;
        mPassword = userPassword;
        mCallback = callback;
        mJson = json;
    }

    @Override
    protected CreationIssueResult doInBackground(Void... params) {
        try {
            if (JiraApi.STATUS_CREATED != new JiraApi().createIssue(mUserName, mPassword, mJson)) {
                throw new AuthenticationException("issue can`t create");
            }
        } catch (Exception e) {

            return  CreationIssueResult.CREATION_UNSUCCESS;
        }
        return CreationIssueResult.CREATION_SUCCESS;
    }

    @Override
    protected void onPostExecute(CreationIssueResult result) {
        mCallback.onCreationIssueResult(result);
    }
}
