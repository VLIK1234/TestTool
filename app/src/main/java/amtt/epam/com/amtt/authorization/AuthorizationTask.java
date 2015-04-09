package amtt.epam.com.amtt.authorization;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthenticationException;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Artsiom_Kaliaha on 25.03.2015.
 */
public class AuthorizationTask extends AsyncTask<Void, Void, AuthorizationResult> {

    private final Context mContext;
    private final AuthorizationCallback mCallback;
    private final String mUserName;
    private final String mPassword;
    private final String mUrl;
    private static final Map<Class,AuthorizationResult> sAuthorizationResults;

    static {
        sAuthorizationResults = new HashMap<>();
        sAuthorizationResults.put(UnknownHostException.class,AuthorizationResult.AUTHORIZATION_DENIED_WRONG_HOST);
    }

    public AuthorizationTask(Context context, String userName, String password, String url, AuthorizationCallback callback) {
        mContext = context;
        mCallback = callback;
        mUserName = userName;
        mPassword = password;
        mUrl = url;
    }

    @Override
    protected AuthorizationResult doInBackground(Void... params) {
        try {
            new JiraApi().authorize(mUserName, mPassword, mUrl);
        } catch (Exception e) {
            if (sAuthorizationResults.get(e.getClass()) != null) {
                return sAuthorizationResults.get(e.getClass());
            }
            return AuthorizationResult.AUTHORIZATION_DENIED_UNKNOWN_REASON;
        }
        return AuthorizationResult.AUTHORIZATION_SUCCESS;
    }

    @Override
    protected void onPostExecute(AuthorizationResult result) {
        mCallback.onAuthorizationResult(result);
    }
}
