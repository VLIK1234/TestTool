package amtt.epam.com.amtt.api;

import android.os.AsyncTask;

import amtt.epam.com.amtt.api.rest.RestResponse;
import amtt.epam.com.amtt.util.CredentialsManager;

/**
 * AsyncTask which is directly used in code to perform async requests to Jira api
 * Created by Artsiom_Kaliaha on 17.04.2015.
 */
public class JiraTask<ResultType,ResultObjectType> extends AsyncTask<Object, Void, RestResponse<ResultType,ResultObjectType>> {

    //TODO we use this class only outside
    public enum JiraSearchType {

        ISSUE,
        USER_INFO

    }

    public enum JiraTaskType {

        AUTH,
        CREATE_ISSUE,
        SEARCH

    }

    public static class Builder<ResultType,ResultObjectType> {

        private JiraTaskType mOperationType;
        private JiraCallback<ResultType,ResultObjectType> mCallback;
        //TODO what is the purpose of json. rename
        private String mJson;
        private JiraSearchType mSearchType;

        public Builder setOperationType(JiraTaskType operationType) {
            mOperationType = operationType;
            return this;
        }

        public Builder setCallback(JiraCallback<ResultType,ResultObjectType> callback) {
            mCallback = callback;
            return this;
        }

        //TODO what is the purpose of json. rename
        public Builder setJson(String json) {
            mJson = json;
            return this;
        }

        public Builder setSearchType(JiraSearchType jiraSearchType) {
            mSearchType = jiraSearchType;
            return this;
        }

        public JiraTask create() {
            JiraTask<ResultType,ResultObjectType> jiraTask = new JiraTask<>();
            jiraTask.mOperationType = this.mOperationType;
            jiraTask.mCallback = this.mCallback;
            jiraTask.mJson = this.mJson;
            jiraTask.mSearchType = this.mSearchType;
            return jiraTask;
        }

    }

    private JiraTaskType mOperationType;
    private JiraCallback<ResultType,ResultObjectType> mCallback;
    private String mJson;
    private JiraSearchType mSearchType;

    private JiraTask() {
    }

    @Override
    @SuppressWarnings("unchecked")
    protected RestResponse<ResultType,ResultObjectType> doInBackground(Object... params) {
        RestResponse<ResultType,ResultObjectType> restResponse = null;

        switch (mOperationType) {
            case AUTH:
                //TODO we create class everyTime we perform call? Why do you are afraid of singltones?
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
    protected void onPostExecute(RestResponse<ResultType,ResultObjectType> restResponse) {
        mCallback.onJiraRequestPerformed(restResponse);
    }

}
