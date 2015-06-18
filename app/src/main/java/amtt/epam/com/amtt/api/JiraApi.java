package amtt.epam.com.amtt.api;

import org.apache.http.HttpResponse;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import amtt.epam.com.amtt.CoreApplication;
import amtt.epam.com.amtt.CoreApplication.Callback;
import amtt.epam.com.amtt.http.HttpClient;
import amtt.epam.com.amtt.http.HttpException;
import amtt.epam.com.amtt.http.HttpResult;
import amtt.epam.com.amtt.http.Request;
import amtt.epam.com.amtt.processing.Processor;
import amtt.epam.com.amtt.util.ActiveUser;

/**
 * Created by Artsiom_Kaliaha on 12.06.2015.
 * Updated api implementation
 */
@SuppressWarnings("unchecked")
public class JiraApi {

    private static final JiraApi INSTANCE;

    static {
        INSTANCE = new JiraApi();
    }

    private JiraApi() {
    }

    public void signOut(Callback callback) {
        Request.Builder requestBuilder = new Request.Builder().setUrl(ActiveUser.getInstance().getUrl() + JiraApiConst.LOGIN_PATH);
        HttpClient.getClient().delete(requestBuilder);
        execute(requestBuilder, callback);
    }

    public <ResultType, InputType> void createIssue(final String postEntityString, final Processor<ResultType, InputType> processor, Callback callback) {
        Map<String, String> headers = new HashMap<>();
        headers.put(JiraApiConst.AUTH, ActiveUser.getInstance().getCredentials());
        headers.put(JiraApiConst.CONTENT_TYPE, JiraApiConst.APPLICATION_JSON);

        Request.Builder requestBuilder;
        try {
            requestBuilder = new Request.Builder()
                    .setUrl(ActiveUser.getInstance().getUrl() + JiraApiConst.ISSUE_PATH)
                    .setHeaders(headers)
                    .setPostEntity(postEntityString)
                    .setProcessor(processor);
            HttpClient.getClient().post(requestBuilder);
        } catch (UnsupportedEncodingException e) {
            callback.onLoadError(new HttpException(e, HttpClient.EMPTY_STATUS_CODE, null, postEntityString));
            return;
        }
        execute(requestBuilder, callback);
    }

    public <ResultType, InputType> void searchData(final String requestSuffix,
                                                   final Processor<ResultType, InputType> processor,
                                                   final String userName,
                                                   final String password,
                                                   String url,
                                                   Callback callback) {
        String credentials;
        if (userName != null && password != null) {
            //this code is used when new user is added and we need to getClient all the info about a user and authorize him/her in one request
            credentials = ActiveUser.getInstance().makeTempCredentials(userName, password);
        } else {
            credentials = ActiveUser.getInstance().getCredentials();
            url = ActiveUser.getInstance().getUrl();
        }
        Map<String, String> headers = new HashMap<>();
        headers.put(JiraApiConst.AUTH, credentials);

        Request.Builder requestBuilder = new Request.Builder()
                .setUrl(url + requestSuffix)
                .setHeaders(headers)
                .setProcessor(processor);
        HttpClient.getClient().get(requestBuilder);
        execute(requestBuilder, callback);
    }

    public void createAttachment(final String issueKey, ArrayList<String> filesPaths, Callback callback) {
        Map<String, String> headers = new HashMap<>();
        headers.put(JiraApiConst.AUTH, ActiveUser.getInstance().getCredentials());
        headers.put(JiraApiConst.ATLASSIAN_TOKEN, JiraApiConst.NO_CHECK);

        Request.Builder requestBuilder = new Request.Builder()
                .setUrl(ActiveUser.getInstance().getUrl() + JiraApiConst.ISSUE_PATH + issueKey + JiraApiConst.ATTACHMENTS_PATH)
                .setHeaders(headers)
                .setPostEntity(filesPaths);
        HttpClient.getClient().post(requestBuilder);
        execute(requestBuilder, callback);
    }

    public static JiraApi get() {
        return INSTANCE;
    }

    private void execute(Request.Builder requestBuilder, Callback callback) {
        new CoreApplication.DataLoadingBuilder<HttpResult, Request, HttpResponse>()
                .setDataSource(HttpClient.SOURCE_NAME)
                .setDataSourceParam(requestBuilder.create())
                .setCallback(callback)
                .load();
    }

}
