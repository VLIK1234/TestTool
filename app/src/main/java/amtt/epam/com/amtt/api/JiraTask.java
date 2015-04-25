package amtt.epam.com.amtt.api;

import android.os.AsyncTask;

import amtt.epam.com.amtt.api.rest.RestResponse;
import amtt.epam.com.amtt.processing.ProjectsProcessor;
import amtt.epam.com.amtt.processing.UserInfoProcessor;
import amtt.epam.com.amtt.util.CredentialsManager;

/**
 * AsyncTask which is directly used in code to perform async requests to Jira api
 * Created by Artsiom_Kaliaha on 17.04.2015.
 */
public class JiraTask< ResultObjectType> extends AsyncTask<Object, Void, RestResponse<ResultObjectType>> {

    public enum JiraSearchType {

        ISSUE,
        USER_INFO

    }

    public enum JiraTaskType {

        AUTH,
        CREATE_ISSUE,
        SEARCH

    }

    public static class Builder<ResultObjectType> {

        private JiraTaskType mOperationType;
        private JiraCallback<ResultObjectType> mCallback;
        private String mPostMessage;
        private JiraSearchType mSearchType;

        public Builder setOperationType(JiraTaskType operationType) {
            mOperationType = operationType;
            return this;
        }

        public Builder setCallback(JiraCallback<ResultObjectType> callback) {
            mCallback = callback;
            return this;
        }

        public Builder setPostJson(String json) {
            mPostMessage = json;
            return this;
        }

        public Builder setSearchType(JiraSearchType jiraSearchType) {
            mSearchType = jiraSearchType;
            return this;
        }

        public JiraTask create() {
            JiraTask<ResultObjectType> jiraTask = new JiraTask<>();
            jiraTask.mOperationType = this.mOperationType;
            jiraTask.mCallback = this.mCallback;
            jiraTask.mJson = this.mPostMessage;
            jiraTask.mSearchType = this.mSearchType;
            return jiraTask;
        }

    }

    private JiraTaskType mOperationType;
    private JiraCallback<ResultObjectType> mCallback;
    private String mJson;
    private JiraSearchType mSearchType;

    private JiraTask() {
    }

    @Override
    @SuppressWarnings("unchecked")
    protected RestResponse<ResultObjectType> doInBackground(Object... params) {
        JiraApi api = JiraApi.getInstance();
        switch (mOperationType) {
            case AUTH:
                api = api.authorize();
                break;
            case CREATE_ISSUE:
                api = api.createIssue(mJson);
                break;
            case SEARCH:
                switch (mSearchType) {
                    case ISSUE:
                        api = api.searchData(JiraApiConst.USER_PROJECTS_PATH, new ProjectsProcessor());
                        break;
                    case USER_INFO:
                        String requestSuffix = JiraApiConst.USER_INFO_PATH + CredentialsManager.getInstance().getUserName() + JiraApiConst.EXPAND_GROUPS;
                        api = api.searchData(requestSuffix, new UserInfoProcessor());
                        break;
                }
                break;
        }
        return api.execute();
    }

    @Override
    protected void onPostExecute(RestResponse<ResultObjectType> restResponse) {
        mCallback.onJiraRequestPerformed(restResponse);
    }

}
