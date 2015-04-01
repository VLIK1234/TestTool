package amtt.epam.com.amtt.asynctask;

import amtt.epam.com.amtt.authorization.JiraApi;
import amtt.epam.com.amtt.bo.CreationIssueResult;
import amtt.epam.com.amtt.callbacks.CreationIssueCallback;
import android.os.AsyncTask;
import org.apache.http.auth.AuthenticationException;

/**
 * Created by Irina Monchenko on 27.03.2015.
 */
public class CreateIssueTask extends AsyncTask<Void, Void, CreationIssueResult> {


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
                throw new AuthenticationException("issue can`t be create");
            }
        } catch (Exception e) {

            return CreationIssueResult.CREATION_UNSUCCESS;
        }
        return CreationIssueResult.CREATION_SUCCESS;
    }

    @Override
    protected void onPostExecute(CreationIssueResult result) {
        mCallback.onCreationIssueResult(result);
    }
}
