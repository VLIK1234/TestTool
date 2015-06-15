package amtt.epam.com.amtt.ticket;

import org.apache.http.HttpRequest;

import java.util.ArrayList;

import amtt.epam.com.amtt.api.JiraApi;
import amtt.epam.com.amtt.api.JiraApiConst;
import amtt.epam.com.amtt.api.rest.RestResponse;
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
        JiraApi.get().searchData(JiraApiConst.USER_PROJECTS_PATH, new ProjectsProcessor(), null, null, null, new AsyncTaskCallback<HttpResult>() {
            @Override
            public void onTaskStart() {
            }

            @Override
            public void onTaskExecuted(HttpResult httpResult) {
                contentLoadingCallback.resultFromBackend((JProjectsResponse) httpResult.getResultObject(),
                        JiraContentConst.META_RESPONSE,
                        jiraGetContentCallback);
            }

            @Override
            public void onTaskError(Exception e) {
                contentLoadingCallback.resultFromBackend(null, JiraContentConst.META_RESPONSE, jiraGetContentCallback);
            }
        });
    }

    public void getVersionsAsynchronously(String projectsKey,
                                          final ContentLoadingCallback<JVersionsResponse> contentLoadingCallback,
                                          final JiraGetContentCallback jiraGetContentCallback) {
        String path = JiraApiConst.PROJECT_VERSIONS_PATH + projectsKey + JiraApiConst.PROJECT_VERSIONS_PATH_V;
        JiraApi.get().searchData(path, new VersionsProcessor(), null, null, null, new AsyncTaskCallback<HttpResult>() {
            @Override
            public void onTaskStart() {

            }

            @Override
            public void onTaskExecuted(HttpResult httpResult) {
                contentLoadingCallback.resultFromBackend((JVersionsResponse) httpResult.getResultObject(), JiraContentConst.VERSIONS_RESPONSE, jiraGetContentCallback);
            }

            @Override
            public void onTaskError(Exception e) {
                contentLoadingCallback.resultFromBackend(null, JiraContentConst.VERSIONS_RESPONSE, jiraGetContentCallback);
            }
        });
    }

    public void getUsersAssignableAsynchronously(String projectKey,
                                                 String userName,
                                                 final ContentLoadingCallback<JUserAssignableResponse> contentLoadingCallback,
                                                 final JiraGetContentCallback jiraGetContentCallback) {
        String path = JiraApiConst.USERS_ASSIGNABLE_PATH + projectKey + JiraApiConst.USERS_ASSIGNABLE_PATH_UN + userName + JiraApiConst.USERS_ASSIGNABLE_PATH_MR;
        JiraApi.get().searchData(path, new UsersAssignableProcessor(), null, null, null, new AsyncTaskCallback<HttpResult>() {
            @Override
            public void onTaskStart() {

            }

            @Override
            public void onTaskExecuted(HttpResult httpResult) {
                contentLoadingCallback.resultFromBackend((JUserAssignableResponse) httpResult.getResultObject(),
                        JiraContentConst.USERS_ASSIGNABLE_RESPONSE,
                        jiraGetContentCallback);
            }

            @Override
            public void onTaskError(Exception e) {
                contentLoadingCallback.resultFromBackend(null, JiraContentConst.USERS_ASSIGNABLE_RESPONSE, jiraGetContentCallback);
            }
        });
    }

    public void getPriorityAsynchronously(final ContentLoadingCallback<JPriorityResponse> contentLoadingCallback,
                                          final JiraGetContentCallback jiraGetContentCallback) {
        String path = JiraApiConst.PROJECT_PRIORITY_PATH;
        JiraApi.get().searchData(path, new PriorityProcessor(), null, null, null, new AsyncTaskCallback<HttpResult>() {
            @Override
            public void onTaskStart() {

            }

            @Override
            public void onTaskExecuted(HttpResult httpResult) {
                contentLoadingCallback.resultFromBackend((JPriorityResponse) httpResult.getResultObject(),
                        JiraContentConst.PRIORITIES_RESPONSE,
                        jiraGetContentCallback);
            }

            @Override
            public void onTaskError(Exception e) {
                contentLoadingCallback.resultFromBackend(null, JiraContentConst.PRIORITIES_RESPONSE, jiraGetContentCallback);
            }
        });
    }


    public void createIssueAsynchronously(String issueJson,
                                          final ContentLoadingCallback<JCreateIssueResponse> contentLoadingCallback,
                                          final JiraGetContentCallback jiraGetContentCallback) {
        JiraApi.get().createIssue(issueJson, new PostCreateIssueProcessor(), new AsyncTaskCallback<HttpResult>() {
            @Override
            public void onTaskStart() {

            }

            @Override
            public void onTaskExecuted(HttpResult httpResult) {
                contentLoadingCallback.resultFromBackend((JCreateIssueResponse) httpResult.getResultObject(),
                        JiraContentConst.CREATE_ISSUE_RESPONSE,
                        jiraGetContentCallback);
            }

            @Override
            public void onTaskError(Exception e) {
                contentLoadingCallback.resultFromBackend(null, JiraContentConst.CREATE_ISSUE_RESPONSE, jiraGetContentCallback);
            }
        });
    }

    public void sendAttachmentAsynchronously(String issueKey,
                                             ArrayList<String> fullFileName,
                                             final ContentLoadingCallback<Boolean> contentLoadingCallback,
                                             final JiraGetContentCallback<Boolean> jiraGetContentCallback) {
        JiraApi.get().createAttachment(issueKey, fullFileName, new AsyncTaskCallback<HttpResult>() {
            @Override
            public void onTaskStart() {

            }

            @Override
            public void onTaskExecuted(HttpResult httpResult) {
                contentLoadingCallback.resultFromBackend(true, JiraContentConst.SEND_ATTACHMENT, jiraGetContentCallback);
            }

            @Override
            public void onTaskError(Exception e) {
                contentLoadingCallback.resultFromBackend(false, JiraContentConst.SEND_ATTACHMENT, jiraGetContentCallback);
            }
        });
    }

}
