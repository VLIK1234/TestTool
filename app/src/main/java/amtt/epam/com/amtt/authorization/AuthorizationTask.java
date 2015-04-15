package amtt.epam.com.amtt.authorization;

import android.os.AsyncTask;

import org.apache.http.auth.AuthenticationException;

import amtt.epam.com.amtt.api.JiraApi;

/**
 * Created by Artsiom_Kaliaha on 25.03.2015.
 */
public class AuthorizationTask extends AsyncTask<Void, Void, AuthorizationResult> {

    private final AuthorizationCallback mCallback;

    public AuthorizationTask(AuthorizationCallback callback) {
        mCallback = callback;
    }

    @Override
    protected AuthorizationResult doInBackground(Void... params) {
        //TODO update for amtt/epam/com/amtt/authorization/JiraApi.java:42
        return JiraApi.INSTANCE.authorize();
    }

    @Override
    protected void onPostExecute(AuthorizationResult result) {
        mCallback.onAuthorizationResult(result);
    }
}
