package amtt.epam.com.amtt.bo;

import amtt.epam.com.amtt.authorization.AuthorizationCallback;
import amtt.epam.com.amtt.authorization.AuthorizationResult;
import amtt.epam.com.amtt.authorization.JiraApi;
import android.content.Context;
import android.os.AsyncTask;
import org.apache.http.auth.AuthenticationException;

/**
 * Created by shiza on 27.03.2015.
 */
public class CreateIssueTask  extends AsyncTask<Void, Void, AuthorizationResult> {

    private final Context mContext;
    private final AuthorizationCallback mCallback;
    private final String mJson;

    public CreateIssueTask(Context context, String json, AuthorizationCallback callback) {
        mContext = context;
        mCallback = callback;
        mJson = json;
    }

    @Override
    protected AuthorizationResult doInBackground(Void... params) {
        try {
            if (JiraApi.STATUS_CREATED != new JiraApi().createIssue(mJson)) {
                throw new AuthenticationException("illegal user name or pass");
            }
        } catch (Exception e) {
            return  AuthorizationResult.AUTHORIZATION_DENIED;
        }
        return AuthorizationResult.AUTHORIZATION_SUCCESS;
    }

    @Override
    protected void onPostExecute(AuthorizationResult result) {
        mCallback.onAuthorizationResult(result);
    }
}
