package amtt.epam.com.amtt.authorization;

import android.os.AsyncTask;

import org.apache.http.auth.AuthenticationException;

/**
 * Created by Artsiom_Kaliaha on 25.03.2015.
 */
public class AuthorizationTask extends AsyncTask<Void, Void, AuthorizationResult> {

    private final AuthorizationCallback mCallback;
    private final String mCredentials;
    private final String mUrl;

    public AuthorizationTask(String credentials, String url, AuthorizationCallback callback) {
        mCallback = callback;
        mCredentials = credentials;
        mUrl = url;
    }

    @Override
    protected AuthorizationResult doInBackground(Void... params) {
        try {
            if (JiraApi.STATUS_AUTHORIZED != new JiraApi().authorize(mCredentials,mUrl)) {
                throw new AuthenticationException("illegal user name or pass");
            }
        } catch (Exception e) {
            return AuthorizationResult.AUTHORIZATION_DENIED;
        }
        return AuthorizationResult.AUTHORIZATION_SUCCESS;
    }

    @Override
    protected void onPostExecute(AuthorizationResult result) {
        mCallback.onAuthorizationResult(result);
    }
}
