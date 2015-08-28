package amtt.epam.com.amtt.api.loadcontent;

import java.util.List;

import amtt.epam.com.amtt.api.ContentLoadingCallback;
import amtt.epam.com.amtt.api.JiraApi;
import amtt.epam.com.amtt.api.JiraApiConst;
import amtt.epam.com.amtt.api.GetContentCallback;
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

public class ContentFromBackend {

    private static class ContentFromBackendHolder {
        public static final ContentFromBackend INSTANCE = new ContentFromBackend();
    }

    public static ContentFromBackend getInstance() {
        return ContentFromBackendHolder.INSTANCE;
    }

    public <Response, Content> void getProjects(final ContentLoadingCallback<Response, Content> loadingCallback,
                            final GetContentCallback<Content> contentCallback) {
        JiraApi.get().searchData(JiraApiConst.USER_PROJECTS_PATH, ProjectsProcessor.NAME,
                getCallback(loadingCallback, contentCallback));
    }

    public <Response, Content> void getVersions(String projectsKey, final ContentLoadingCallback<Response, Content> loadingCallback,
                            final GetContentCallback<Content> contentCallback) {
        String path = JiraApiConst.PROJECT_VERSIONS_PATH + projectsKey + JiraApiConst.PROJECT_VERSIONS_PATH_V;
        JiraApi.get().searchData(path, VersionsProcessor.NAME, getCallback(loadingCallback, contentCallback));
    }

    public <Response, Content> void getComponents(String projectsKey, final ContentLoadingCallback<Response, Content> loadingCallback,
                              final GetContentCallback<Content> contentCallback) {
        String path = JiraApiConst.PROJECT_COMPONENTS_PATH + projectsKey + JiraApiConst.PROJECT_COMPONENTS_PATH_C;
        JiraApi.get().searchData(path, ComponentsProcessor.NAME,
                getCallback(loadingCallback, contentCallback));
    }

    public <Response, Content> void getUsersAssignable(String projectKey, String userName, final ContentLoadingCallback<Response, Content> loadingCallback,
                                   final GetContentCallback<Content> contentCallback) {
        String path = JiraApiConst.USERS_ASSIGNABLE_PATH + projectKey + JiraApiConst.USERS_ASSIGNABLE_PATH_UN + userName + JiraApiConst.USERS_ASSIGNABLE_PATH_MR;
        JiraApi.get().searchData(path, UsersAssignableProcessor.NAME,
                getCallback(loadingCallback, contentCallback));
    }

    public <Response, Content> void getPriority(final ContentLoadingCallback<Response, Content> loadingCallback,
                            final GetContentCallback<Content> contentCallback) {
        String path = JiraApiConst.PROJECT_PRIORITY_PATH;
        JiraApi.get().searchData(path, PriorityProcessor.NAME, getCallback(loadingCallback, contentCallback));
    }


    public <Response> void createIssue(String issueJson, final ContentLoadingCallback<Response, Response> loadingCallback,
                            final GetContentCallback<Response> contentCallback) {
        JiraApi.get().createIssue(issueJson, PostCreateIssueProcessor.NAME, getCallback(loadingCallback, contentCallback));
    }

    public void sendAttachment(String issueKey, List<String> fullFileName, final ContentLoadingCallback<Boolean, Boolean> loadingCallback,
                               final GetContentCallback<Boolean> contentCallback) {
        JiraApi.get().createAttachment(issueKey, fullFileName, getCallbackStatus(loadingCallback, contentCallback));
    }

    private <Content> Callback getCallbackStatus(final ContentLoadingCallback<Boolean, Content> loadingCallback, final GetContentCallback<Content> contentCallback) {
        return new Callback<Void>() {
            @Override
            public void onLoadStart() {
            }

            @Override
            public void onLoadExecuted(Void result) {
                loadingCallback.resultFromBackend(true, contentCallback);
            }

            @Override
            public void onLoadError(Exception e) {
                loadingCallback.resultFromBackend(false, contentCallback);
            }
        };
    }

    private <Result, Content> Callback getCallback(final ContentLoadingCallback<Result, Content> loadingCallback,
                                          final GetContentCallback<Content> contentCallback) {
        return new Callback<Result>() {
            @Override
            public void onLoadStart() {
            }

            @Override
            public void onLoadExecuted(Result result) {
                loadingCallback.resultFromBackend(result, contentCallback);
            }

            @Override
            public void onLoadError(Exception e) {
                loadingCallback.resultFromBackend(null, contentCallback);
            }
        };
    }

}
