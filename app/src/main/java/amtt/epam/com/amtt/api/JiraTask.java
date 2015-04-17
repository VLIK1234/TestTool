package amtt.epam.com.amtt.api;

import android.os.AsyncTask;

import amtt.epam.com.amtt.authorization.JiraApi;
import amtt.epam.com.amtt.authorization.RestMethod;
import amtt.epam.com.amtt.authorization.RestResponse;
import amtt.epam.com.amtt.bo.issue.JiraSearchType;
import amtt.epam.com.amtt.util.CredentialsManager;

/**
 * Created by Artsiom_Kaliaha on 17.04.2015.
 */
public class JiraTask<ResultType> extends AsyncTask<Void, Void, RestResponse<ResultType>> {

    public static class Builder<ResultType> {

        private JiraOperationType mOperationType;
        private JiraCallback<ResultType> mCallback;
        private String mJson;
        private JiraSearchType mSearchType;

        public Builder setOperationType(JiraOperationType operationType) {
            mOperationType = operationType;
            return this;
        }

        public Builder setCallback(JiraCallback<ResultType> callback) {
            mCallback = callback;
            return this;
        }

        public Builder setJson(String json) {
            mJson = json;
            return this;
        }

        public Builder setSearchType(JiraSearchType jiraSearchType) {
            mSearchType = jiraSearchType;
            return this;
        }

        public JiraTask create() {
            JiraTask<ResultType> jiraTask = new JiraTask<>();
            jiraTask.mOperationType = this.mOperationType;
            jiraTask.mCallback = this.mCallback;
            jiraTask.mJson = this.mJson;
            jiraTask.mSearchType = this.mSearchType;
            return jiraTask;
        }

    }

    private JiraOperationType mOperationType;
    private JiraCallback<ResultType> mCallback;
    private String mJson;
    private JiraSearchType mSearchType;

    private JiraTask() {
    }

    @Override
    @SuppressWarnings("unchecked")
    protected RestResponse<ResultType> doInBackground(Void... params) {
        RestResponse<ResultType> restResponse = null;

        switch (mOperationType) {
            case AUTH:
                restResponse = new JiraApi().authorize();
                break;
            case CREATE_ISSUE:
                restResponse = new JiraApi().createIssue(mJson);
                break;
            case SEARCH:
                String userName = CredentialsManager.getInstance().getUserName();
                restResponse = new JiraApi().searchData(userName, mSearchType);
                break;
        }
        return restResponse;
    }

    @Override
    protected void onPostExecute(RestResponse<ResultType> restResponse) {
        mCallback.onJiraRequestPerformed(restResponse);
    }

}
