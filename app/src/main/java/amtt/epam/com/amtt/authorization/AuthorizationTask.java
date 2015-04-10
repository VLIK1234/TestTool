package amtt.epam.com.amtt.authorization;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import amtt.epam.com.amtt.authorization.exceptions.AuthGateWayException;
import amtt.epam.com.amtt.processing.AuthResponseProcessor;

/**
 * Created by Artsiom_Kaliaha on 25.03.2015.
 */
public class AuthorizationTask extends AsyncTask<Void, Void, String> {

    private final AuthorizationCallback mCallback;
    private final String mUserName;
    private final String mPassword;
    private final String mUrl;
    private Exception mAuthException;
    private static final AuthResponseProcessor sAuthResponseProcessor;

    static {
        sAuthResponseProcessor = new AuthResponseProcessor();
    }

    public AuthorizationTask(String userName, String password, String url, AuthorizationCallback callback) {
        mCallback = callback;
        mUserName = userName;
        mPassword = password;
        mUrl = url;
    }

    @Override
    protected String doInBackground(Void... params) {
        HttpResponse httpResponse;
        try {
            httpResponse = new JiraApi().authorize(mUserName, mPassword, mUrl);
        } catch (Exception e) {
            mAuthException = e;
            return null;
        }

        //Bad gate way is considered as a response, not an exception
        if (httpResponse != null && httpResponse.getStatusLine().getStatusCode() == JiraApi.BAD_GATE_WAY) {
            mAuthException = new AuthGateWayException();
        }

        String retrievedResponse;
        try {
            retrievedResponse = sAuthResponseProcessor.process(httpResponse);
        } catch (Exception e) {
            return "Authorization is passed but response is illegible=(";
        }
        return retrievedResponse;
    }

    @Override
    protected void onPostExecute(String responseMessage) {
        mCallback.onAuthorizationResult(responseMessage, mAuthException);
    }
}
