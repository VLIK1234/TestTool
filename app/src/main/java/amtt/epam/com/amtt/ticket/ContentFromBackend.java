package amtt.epam.com.amtt.ticket;

import amtt.epam.com.amtt.api.JiraApi;
import amtt.epam.com.amtt.api.JiraApiConst;
import amtt.epam.com.amtt.api.JiraCallback;
import amtt.epam.com.amtt.api.JiraTask;
import amtt.epam.com.amtt.api.exception.AmttException;
import amtt.epam.com.amtt.api.rest.RestMethod;
import amtt.epam.com.amtt.api.rest.RestResponse;
import amtt.epam.com.amtt.bo.*;
import amtt.epam.com.amtt.processing.*;

import java.util.ArrayList;

/**
 @author Iryna Monchanka
 @version on 15.05.2015
 */

public class ContentFromBackend {

    private static class ContentFromBackendHolder {
        public static final ContentFromBackend INSTANCE = new ContentFromBackend();
    }

    public static ContentFromBackend getInstance() {
        return ContentFromBackendHolder.INSTANCE;
    }

    @SuppressWarnings("unchecked")
    public void getMetaAsynchronously(final ContentLoadingCallback<JProjectsResponse> contentLoadingCallback,
                                      final JiraGetContentCallback jiraGetContentCallback) {
        JiraApi.getInstance().searchData(new JiraCallback() {
                                             @Override
                                             public void onRequestStarted() {
                                             }

                                             @Override
                                             public void onRequestPerformed(RestResponse restResponse) {
                                                 contentLoadingCallback.resultFromBackend((JProjectsResponse) restResponse.getResultObject(),
                                                         JiraContentConst.META_RESPONSE, jiraGetContentCallback);
                                             }

                                             @Override
                                             public void onRequestError(AmttException e) {
                                                 contentLoadingCallback.resultFromBackend(null, JiraContentConst.META_RESPONSE, jiraGetContentCallback);
                                             }
                                         }, JiraApiConst.USER_PROJECTS_PATH, new ProjectsProcessor(), null, null, null);
    }

    @SuppressWarnings("unchecked")
    public void getVersionsAsynchronously(String projectsKey,
                                          final ContentLoadingCallback<JVersionsResponse> contentLoadingCallback,
                                          final JiraGetContentCallback jiraGetContentCallback) {
        String path = JiraApiConst.PROJECT_VERSIONS_PATH + projectsKey + JiraApiConst.PROJECT_VERSIONS_PATH_V;
        JiraApi.getInstance().searchData(new JiraCallback() {
            @Override
            public void onRequestStarted() {
            }

            @Override
            public void onRequestPerformed(RestResponse restResponse) {
                contentLoadingCallback.resultFromBackend((JVersionsResponse) restResponse.getResultObject(),
                        JiraContentConst.VERSIONS_RESPONSE, jiraGetContentCallback);
            }

            @Override
            public void onRequestError(AmttException e) {
                contentLoadingCallback.resultFromBackend(null, JiraContentConst.VERSIONS_RESPONSE, jiraGetContentCallback);
            }
        }, path, new VersionsProcessor(), null, null, null);
    }

    @SuppressWarnings("unchecked")
    public void getUsersAssignableAsynchronously(String projectKey, String userName,
                                                 final ContentLoadingCallback<JUserAssignableResponse> contentLoadingCallback,
                                                 final JiraGetContentCallback jiraGetContentCallback) {
        String path = JiraApiConst.USERS_ASSIGNABLE_PATH + projectKey + JiraApiConst.USERS_ASSIGNABLE_PATH_UN + userName + JiraApiConst.USERS_ASSIGNABLE_PATH_MR;
        JiraApi.getInstance().searchData(new JiraCallback() {
            @Override
            public void onRequestStarted() {
            }

            @Override
            public void onRequestPerformed(RestResponse restResponse) {
                contentLoadingCallback.resultFromBackend((JUserAssignableResponse) restResponse.getResultObject(),
                        JiraContentConst.USERS_ASSIGNABLE_RESPONSE, jiraGetContentCallback);
            }

            @Override
            public void onRequestError(AmttException e) {
                contentLoadingCallback.resultFromBackend(null, JiraContentConst.USERS_ASSIGNABLE_RESPONSE, jiraGetContentCallback);
            }
        }, path, new UsersAssignableProcessor(), null, null, null);
    }

    @SuppressWarnings("unchecked")
    public void getPriorityAsynchronously(final ContentLoadingCallback<JPriorityResponse> contentLoadingCallback,
                                          final JiraGetContentCallback jiraGetContentCallback) {
        String path = JiraApiConst.PROJECT_PRIORITY_PATH;
        JiraApi.getInstance().searchData(new JiraCallback() {
            @Override
            public void onRequestStarted() {
            }

            @Override
            public void onRequestPerformed(RestResponse restResponse) {
                contentLoadingCallback.resultFromBackend((JPriorityResponse) restResponse.getResultObject(),
                        JiraContentConst.PRIORITIES_RESPONSE, jiraGetContentCallback);
            }

            @Override
            public void onRequestError(AmttException e) {
                contentLoadingCallback.resultFromBackend(null, JiraContentConst.PRIORITIES_RESPONSE, jiraGetContentCallback);
            }

        }, path, new PriorityProcessor(), null, null, null);
    }


    @SuppressWarnings("unchecked")
    public void createIssueAsynchronously(String issueJson, final ContentLoadingCallback<JCreateIssueResponse> contentLoadingCallback,
                                          final JiraGetContentCallback jiraGetContentCallback) {
        JiraApi.getInstance().createIssue(new JiraCallback() {
            @Override
            public void onRequestStarted() {
            }

            @Override
            public void onRequestPerformed(RestResponse restResponse) {
                contentLoadingCallback.resultFromBackend((JCreateIssueResponse) restResponse.getResultObject(), JiraContentConst.CREATE_ISSUE_RESPONSE, jiraGetContentCallback);
            }

            @Override
            public void onRequestError(AmttException e) {
                contentLoadingCallback.resultFromBackend(null, JiraContentConst.CREATE_ISSUE_RESPONSE, jiraGetContentCallback);
            }
        }, issueJson, new PostCreateIssueProcessor());
    }

    @SuppressWarnings("unchecked")
    public void sendAttachmentAsynchronously(String issueKey, ArrayList<String> fullFileName, final ContentLoadingCallback<Boolean> contentLoadingCallback,
                                             final JiraGetContentCallback<Boolean> jiraGetContentCallback) {
        JiraApi.getInstance().createAttachment(new JiraCallback() {
            @Override
            public void onRequestStarted() {
            }

            @Override
            public void onRequestPerformed(RestResponse restResponse) {
                contentLoadingCallback.resultFromBackend(true, JiraContentConst.SEND_ATTACHMENT, jiraGetContentCallback);
            }

            @Override
            public void onRequestError(AmttException e) {
                contentLoadingCallback.resultFromBackend(false, JiraContentConst.SEND_ATTACHMENT, jiraGetContentCallback);
            }
        }, issueKey, fullFileName);
    }

}
