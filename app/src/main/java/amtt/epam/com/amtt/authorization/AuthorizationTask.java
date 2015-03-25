package amtt.epam.com.amtt.authorization;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by Artsiom_Kaliaha on 25.03.2015.
 */
public class AuthorizationTask extends AsyncTask<Void, Void, AuthorizationResult> {

    private final Context mContext;
    private final AuthorizationCallback mCallback;
    private final String mUserName;
    private final String mPassword;
    private String mToken;

    public AuthorizationTask(Context context, String userName, String password, AuthorizationCallback callback) {
        mContext = context;
        mCallback = callback;
        mUserName = userName;
        mPassword = password;
    }

    @Override
    protected AuthorizationResult doInBackground(Void... params) {
        try {
            mToken = new JiraApi().authorize(mUserName, mPassword);
        } catch (Exception e) {
            return  AuthorizationResult.AUTHORIZATION_DENIED;
        }
        return AuthorizationResult.AUTHORIZATION_SUCCESS;
    }

    @Override
    protected void onPostExecute(AuthorizationResult result) {
        mCallback.onAuthorizationResult(result, mToken);
    }
}
