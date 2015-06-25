package amtt.epam.com.amtt.api.loadcontent;

import java.util.ArrayList;

import amtt.epam.com.amtt.api.ContentLoadingCallback;
import amtt.epam.com.amtt.api.JiraApi;
import amtt.epam.com.amtt.api.JiraApiConst;
import amtt.epam.com.amtt.api.JiraContentConst;
import amtt.epam.com.amtt.api.JiraGetContentCallback;
import amtt.epam.com.amtt.bo.JComponentsResponse;
import amtt.epam.com.amtt.bo.JCreateIssueResponse;
import amtt.epam.com.amtt.bo.JPriorityResponse;
import amtt.epam.com.amtt.bo.JProjectsResponse;
import amtt.epam.com.amtt.bo.JUserAssignableResponse;
import amtt.epam.com.amtt.bo.JVersionsResponse;
import amtt.epam.com.amtt.common.Callback;
import amtt.epam.com.amtt.processing.ComponentsProcessor;
import amtt.epam.com.amtt.processing.PostCreateIssueProcessor;
import amtt.epam.com.amtt.processing.PriorityProcessor;
import amtt.epam.com.amtt.processing.ProjectsProcessor;
import amtt.epam.com.amtt.processing.UsersAssignableProcessor;
import amtt.epam.com.amtt.processing.VersionsProcessor;

/**
 * @author Iryna Monchanka
 * @version on 15.05.2015
 */
@SuppressWarnings("unchecked")
public class ContentFromBackend {

    private static class ContentFromBackendHolder {
        public static final ContentFromBackend INSTANCE = new ContentFromBackend();
    }

    public static ContentFromBackend getInstance() {
        return ContentFromBackendHolder.INSTANCE;
    }

    public void getProjectsAsynchronously(final ContentLoadingCallback<JProjectsResponse> contentLoadingCallback,
                                          final JiraGetContentCallback jiraGetContentCallback) {
        JiraApi.get().searchData(JiraApiConst.USER_PROJECTS_PATH,
                ProjectsProcessor.NAME,
                null,
                null,
                null,
                getCallback(JiraContentConst.PROJECTS_RESPONSE, contentLoadingCallback, jiraGetContentCallback));
    }

    public void getVersionsAsynchronously(String projectsKey,
                                          final ContentLoadingCallback<JVersionsResponse> contentLoadingCallback,
                                          final JiraGetContentCallback jiraGetContentCallback) {
        String path = JiraApiConst.PROJECT_VERSIONS_PATH + projectsKey + JiraApiConst.PROJECT_VERSIONS_PATH_V;
        JiraApi.get().searchData(path,
                VersionsProcessor.NAME,
                null,
                null,
                null,
                getCallback(JiraContentConst.VERSIONS_RESPONSE, contentLoadingCallback, jiraGetContentCallback));
    }

    @SuppressWarnings("unchecked")
    public void getComponentsAsynchronously(String projectsKey,
                                          final ContentLoadingCallback<JComponentsResponse> contentLoadingCallback,
                                          final JiraGetContentCallback jiraGetContentCallback) {
        String path = JiraApiConst.PROJECT_COMPONENTS_PATH + projectsKey + JiraApiConst.PROJECT_COMPONENTS_PATH_C;
        JiraApi.get().searchData(path,
                ComponentsProcessor.NAME,
                null,
                null,
                null,
                getCallback(JiraContentConst.COMPONENTS_RESPONSE, contentLoadingCallback, jiraGetContentCallback));
    }

    @SuppressWarnings("unchecked")
    public void getUsersAssignableAsynchronously(String projectKey,
                                                 String userName,
                                                 final ContentLoadingCallback<JUserAssignableResponse> contentLoadingCallback,
                                                 final JiraGetContentCallback jiraGetContentCallback) {
        String path = JiraApiConst.USERS_ASSIGNABLE_PATH + projectKey + JiraApiConst.USERS_ASSIGNABLE_PATH_UN + userName + JiraApiConst.USERS_ASSIGNABLE_PATH_MR;
        JiraApi.get().searchData(path,
                UsersAssignableProcessor.NAME,
                null,
                null,
                null,
                getCallback(JiraContentConst.USERS_ASSIGNABLE_RESPONSE, contentLoadingCallback, jiraGetContentCallback));
    }

    public void getPriorityAsynchronously(final ContentLoadingCallback<JPriorityResponse> contentLoadingCallback,
                                          final JiraGetContentCallback jiraGetContentCallback) {
        String path = JiraApiConst.PROJECT_PRIORITY_PATH;
        JiraApi.get().searchData(path,
                PriorityProcessor.NAME,
                null,
                null,
                null,
                getCallback(JiraContentConst.PRIORITIES_RESPONSE, contentLoadingCallback, jiraGetContentCallback));


    }


    public void createIssueAsynchronously(String issueJson,
                                          final ContentLoadingCallback<JCreateIssueResponse> contentLoadingCallback,
                                          final JiraGetContentCallback jiraGetContentCallback) {
        JiraApi.get().createIssue(issueJson,
                PostCreateIssueProcessor.NAME,
                getCallback(JiraContentConst.CREATE_ISSUE_RESPONSE, contentLoadingCallback, jiraGetContentCallback));
    }

    public void sendAttachmentAsynchronously(String issueKey,
                                             ArrayList<String> fullFileName,
                                             final ContentLoadingCallback<Boolean> contentLoadingCallback,
                                             final JiraGetContentCallback jiraGetContentCallback) {
        JiraApi.get().createAttachment(issueKey,
                fullFileName,
                getCallback(JiraContentConst.SEND_ATTACHMENT, true, false, contentLoadingCallback, jiraGetContentCallback));
    }

    private <Result> Callback getCallback(final JiraContentConst requestType,
                                          final Result successResult,
                                          final Result errorResult,
                                          final ContentLoadingCallback<Result> contentLoadingCallback,
                                          final JiraGetContentCallback jiraGetContentCallback) {
        return new Callback<Result>() {
            @Override
            public void onLoadStart() {

            }

            @Override
            public void onLoadExecuted(Result result) {
                contentLoadingCallback.resultFromBackend(successResult, requestType, jiraGetContentCallback);
            }

            @Override
            public void onLoadError(Exception e) {
                contentLoadingCallback.resultFromBackend(errorResult, requestType, jiraGetContentCallback);
            }
        };
    }

    private <Result> Callback getCallback(final JiraContentConst requestType,
                                          final ContentLoadingCallback<Result> contentLoadingCallback,
                                          final JiraGetContentCallback jiraGetContentCallback) {
        return new Callback<Result>() {
            @Override
            public void onLoadStart() {

            }

            @Override
            public void onLoadExecuted(Result result) {
                contentLoadingCallback.resultFromBackend(result, requestType, jiraGetContentCallback);
            }

            @Override
            public void onLoadError(Exception e) {
                contentLoadingCallback.resultFromBackend(null, requestType, jiraGetContentCallback);
            }
        };
    }

}
