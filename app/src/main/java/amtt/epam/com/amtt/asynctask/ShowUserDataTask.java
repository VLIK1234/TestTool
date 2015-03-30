package amtt.epam.com.amtt.asynctask;

import amtt.epam.com.amtt.authorization.JiraApi;
import amtt.epam.com.amtt.bo.ShowUserDataResult;
import amtt.epam.com.amtt.callbacks.ShowUserDataCallback;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.auth.AuthenticationException;

/**
 * Created by shiza on 30.03.2015.
 */
public class ShowUserDataTask  extends AsyncTask<Void, Void, ShowUserDataResult> {


    private final ShowUserDataCallback mCallback;
    private final String mUserName;
    private final String mPassword;

    public ShowUserDataTask(String username, String userPassword, ShowUserDataCallback callback) {
        mUserName = username;
        mPassword = userPassword;
        mCallback = callback;
    }

    @Override
    protected ShowUserDataResult doInBackground(Void... params) {
        try {
            int i = new JiraApi().searchIssue(mUserName, mPassword);
            Log.d("STATUS", String.valueOf(i));
                //throw new AuthenticationException("issue can`t be create");

        } catch (Exception e) {

            return ShowUserDataResult.CREATION_UNSUCCESS;
        }
        return ShowUserDataResult.CREATION_SUCCESS;
    }

    @Override
    protected void onPostExecute(ShowUserDataResult result) {
        mCallback.onShowUserDataResult(result);
    }
}
