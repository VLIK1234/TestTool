package amtt.epam.com.amtt.ticket;

import org.apache.http.HttpRequest;

import java.util.ArrayList;

import amtt.epam.com.amtt.api.JiraApi;
import amtt.epam.com.amtt.api.JiraApiConst;
import amtt.epam.com.amtt.bo.JCreateIssueResponse;
import amtt.epam.com.amtt.bo.JPriorityResponse;
import amtt.epam.com.amtt.bo.JProjectsResponse;
import amtt.epam.com.amtt.bo.JUserAssignableResponse;
import amtt.epam.com.amtt.bo.JVersionsResponse;
import amtt.epam.com.amtt.http.HttpResult;
import amtt.epam.com.amtt.os.Task.AsyncTaskCallback;
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


    public void getMetaAsynchronously(final ContentLoadingCallback<JProjectsResponse> contentLoadingCallback,
                                      final JiraGetContentCallback jiraGetContentCallback) {
        JiraApi.get().searchData(JiraApiConst.USER_PROJECTS_PATH,
                new ProjectsProcessor(),
                null,
                null,
                null,
                getCallback(JiraContentConst.META_RESPONSE, null, null, contentLoadingCallback, jiraGetContentCallback));
    }

    public void getVersionsAsynchronously(String projectsKey,
                                          final ContentLoadingCallback<JVersionsResponse> contentLoadingCallback,
                                          final JiraGetContentCallback jiraGetContentCallback) {
        String path = JiraApiConst.PROJECT_VERSIONS_PATH + projectsKey + JiraApiConst.PROJECT_VERSIONS_PATH_V;
        JiraApi.get().searchData(path,
                new VersionsProcessor(),
                null,
                null,
                null,
                getCallback(JiraContentConst.VERSIONS_RESPONSE, null, null, contentLoadingCallback, jiraGetContentCallback));
    }

    public void getUsersAssignableAsynchronously(String projectKey,
                                                 String userName,
                                                 final ContentLoadingCallback<JUserAssignableResponse> contentLoadingCallback,
                                                 final JiraGetContentCallback jiraGetContentCallback) {
        String path = JiraApiConst.USERS_ASSIGNABLE_PATH + projectKey + JiraApiConst.USERS_ASSIGNABLE_PATH_UN + userName + JiraApiConst.USERS_ASSIGNABLE_PATH_MR;
        JiraApi.get().searchData(path,
                new UsersAssignableProcessor(),
                null,
                null,
                null,
                getCallback(JiraContentConst.USERS_ASSIGNABLE_RESPONSE, null, null, contentLoadingCallback, jiraGetContentCallback));
    }

    public void getPriorityAsynchronously(final ContentLoadingCallback<JPriorityResponse> contentLoadingCallback,
                                          final JiraGetContentCallback jiraGetContentCallback) {
        String path = JiraApiConst.PROJECT_PRIORITY_PATH;
        JiraApi.get().searchData(path,
                new PriorityProcessor(),
                null,
                null,
                null,
                getCallback(JiraContentConst.PRIORITIES_RESPONSE, null, null, contentLoadingCallback, jiraGetContentCallback));


    }


    public void createIssueAsynchronously(String issueJson,
                                          final ContentLoadingCallback<JCreateIssueResponse> contentLoadingCallback,
                                          final JiraGetContentCallback jiraGetContentCallback) {
        JiraApi.get().createIssue(issueJson,
                new PostCreateIssueProcessor(),
                getCallback(JiraContentConst.CREATE_ISSUE_RESPONSE, null, null, contentLoadingCallback, jiraGetContentCallback));
    }

    public void sendAttachmentAsynchronously(String issueKey,
                                             ArrayList<String> fullFileName,
                                             final ContentLoadingCallback<Boolean> contentLoadingCallback,
                                             final JiraGetContentCallback jiraGetContentCallback) {
        JiraApi.get().createAttachment(issueKey,
                fullFileName,
                getCallback(JiraContentConst.SEND_ATTACHMENT, true, false, contentLoadingCallback, jiraGetContentCallback));
    }

    private <Result> AsyncTaskCallback getCallback(final JiraContentConst requestType, final Result successResult, final Result errorResult, final ContentLoadingCallback<Result> contentLoadingCallback, final JiraGetContentCallback jiraGetContentCallback) {
        return new AsyncTaskCallback<HttpResult>() {
            @Override
            public void onTaskStart() {

            }

            @Override
            public void onTaskExecuted(HttpResult httpResult) {
                if (successResult != null) {
                    contentLoadingCallback.resultFromBackend(successResult, requestType, jiraGetContentCallback);
                } else {
                    contentLoadingCallback.resultFromBackend((Result) httpResult.getResultObject(), requestType, jiraGetContentCallback);
                }
            }

            @Override
            public void onTaskError(Exception e) {
                contentLoadingCallback.resultFromBackend(errorResult, requestType, jiraGetContentCallback);
            }
        };
    }

}
