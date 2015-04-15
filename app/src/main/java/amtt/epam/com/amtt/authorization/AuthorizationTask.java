package amtt.epam.com.amtt.authorization;

import android.os.AsyncTask;

/**
 * Created by Artsiom_Kaliaha on 25.03.2015.
 */
public class AuthorizationTask extends AsyncTask<Void, Void, String> {

    private final AuthorizationCallback mCallback;
    private final String mUserName;
    private final String mPassword;
    private final String mUrl;

    private AuthorizationResult mAuthResult;

    public AuthorizationTask(String userName, String password, String url, AuthorizationCallback callback) {
        mCallback = callback;
        mUserName = userName;
        mPassword = password;
        mUrl = url;
    }

    @Override
    protected String doInBackground(Void... params) {
        JiraApi api = new JiraApi();
        RestResponse restResponse = api.authorize(mUserName, mPassword, mUrl);
        return restResponse.getMessage();
    }

    @Override
    protected void onPostExecute(String responseMessage) {
        mCallback.onAuthorizationResult(mAuthResult, responseMessage);
    }
}
