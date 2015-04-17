package amtt.epam.com.amtt.asynctask;

import android.os.AsyncTask;

import amtt.epam.com.amtt.authorization.JiraApi;
import amtt.epam.com.amtt.authorization.RestResponse;
import amtt.epam.com.amtt.callbacks.CreationIssueCallback;

/**
 * Created by Irina Monchenko on 27.03.2015.
 */
public class CreateIssueTask extends AsyncTask<Void, Void, RestResponse> {

    private final CreationIssueCallback mCallback;
    private final String mJson;

    public CreateIssueTask(String json, CreationIssueCallback callback) {
        mCallback = callback;
        mJson = json;
    }

    @Override
    protected RestResponse doInBackground(Void... params) {
        return new JiraApi().createIssue(mJson);
    }

    @Override
    protected void onPostExecute(RestResponse restResponse) {
        mCallback.onCreationIssueResult(restResponse);

    }
}
