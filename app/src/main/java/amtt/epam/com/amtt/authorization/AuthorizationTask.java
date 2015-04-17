package amtt.epam.com.amtt.authorization;

import android.os.AsyncTask;

/**
 * Created by Artsiom_Kaliaha on 25.03.2015.
 */
public class AuthorizationTask extends AsyncTask<Void, Void, RestResponse> {
    private final AuthorizationCallback mCallback;
    private final String mUserName;
    private final String mPassword;
    private final String mUrl;

    public AuthorizationTask(String userName, String password, String url, AuthorizationCallback callback) {
        mCallback = callback;
        mUserName = userName;
        mPassword = password;
        mUrl = url;
    }

    @Override
    protected RestResponse doInBackground(Void... params) {
        //return new JiraApi().authorize(mUserName, mPassword, mUrl);
        return null;
    }

    @Override
    protected void onPostExecute(RestResponse restResponse) {
        mCallback.onAuthorizationResult(restResponse);
    }
}
