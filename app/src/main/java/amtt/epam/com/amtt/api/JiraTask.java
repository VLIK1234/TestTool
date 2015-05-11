package amtt.epam.com.amtt.api;

import android.os.AsyncTask;

import amtt.epam.com.amtt.api.exception.AmttException;
import amtt.epam.com.amtt.api.rest.RestMethod;
import amtt.epam.com.amtt.api.rest.RestResponse;
import amtt.epam.com.amtt.api.result.JiraOperationResult;

/**
 * AsyncTask which is directly used in code to perform async requests to Jira api
 * Created by Artsiom_Kaliaha on 17.04.2015.
 */
public class JiraTask<ResultType> extends AsyncTask<Object, Void, RestResponse<ResultType>> {

    public static class Builder<ResultType> {

        private RestMethod<ResultType> mRestMethod;
        private JiraCallback<ResultType> mCallback;

        public Builder setRestMethod(RestMethod<ResultType> restMethod) {
            mRestMethod = restMethod;
            return this;
        }

        public Builder setCallback(JiraCallback<ResultType> callback) {
            mCallback = callback;
            return this;
        }


        public void createAndExecute() {
            JiraTask<ResultType> jiraTask = new JiraTask<>();
            jiraTask.mRestMethod = this.mRestMethod;
            jiraTask.mCallback = this.mCallback;
            //TODO executors?
            jiraTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        }

    }

    private RestMethod mRestMethod;
    private JiraCallback<ResultType> mCallback;
    private AmttException mException;

    private JiraTask() {
    }

    @Override
    protected void onPreExecute() {
        mCallback.onRequestStarted();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected RestResponse<ResultType> doInBackground(Object... params) {
        RestResponse<ResultType> restResponse = null;
        try {
            restResponse = mRestMethod.execute();
            setOperationResult(restResponse);
        } catch (AmttException e) {
            mException = e;
        }
        return restResponse;
    }

    @Override
    protected void onPostExecute(RestResponse<ResultType> restResponse) {
        if (restResponse != null) {
            mCallback.onRequestPerformed(restResponse);
        } else {
            mCallback.onRequestError(mException);
        }
    }

    private void setOperationResult(RestResponse<ResultType> restResponse) {
        JiraOperationResult operationResult;
        if (mRestMethod.getRequestType() == RestMethod.RestMethodType.GET) {
            operationResult = JiraOperationResult.REQUEST_PERFORMED;
        } else {
            operationResult = JiraOperationResult.ISSUE_CREATED;
        }
        restResponse.setOperationResult(operationResult);
    }

}
