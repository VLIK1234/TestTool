package amtt.epam.com.amtt.api;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import amtt.epam.com.amtt.http.HttpClient;
import amtt.epam.com.amtt.http.HttpException;
import amtt.epam.com.amtt.http.Request;
import amtt.epam.com.amtt.os.Task.AsyncTaskCallback;
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

    public void signOut(AsyncTaskCallback callback) {
        HttpClient.getClient().delete(new Request.Builder()
                        .setUrl(ActiveUser.getInstance()
                                .getUrl() + JiraApiConst.LOGIN_PATH),
                callback);
    }

    public <ResultType, InputType> void createIssue(final String postEntityString, final Processor<ResultType, InputType> processor, AsyncTaskCallback callback) {
        Map<String, String> headers = new HashMap<>();
        headers.put(JiraApiConst.AUTH, ActiveUser.getInstance().getCredentials());
        headers.put(JiraApiConst.CONTENT_TYPE, JiraApiConst.APPLICATION_JSON);
        try {
            HttpClient.getClient().post(new Request.Builder()
                            .setUrl(ActiveUser.getInstance().getUrl() + JiraApiConst.ISSUE_PATH)
                            .setHeaders(headers)
                            .setPostEntity(postEntityString)
                            .setProcessor(processor),
                    callback);
        } catch (UnsupportedEncodingException e) {
            callback.onTaskError(new HttpException(e, HttpClient.EMPTY_STATUS_CODE, null, postEntityString));
        }
    }

    public <ResultType, InputType> void searchData(final String requestSuffix,
                                                   final Processor<ResultType, InputType> processor,
                                                   final String userName,
                                                   final String password,
                                                   String url,
                                                   AsyncTaskCallback callback) {
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

        HttpClient.getClient().get(new Request.Builder()
                        .setUrl(url + requestSuffix)
                        .setHeaders(headers)
                        .setProcessor(processor),
                callback);
    }

    public void createAttachment(final String issueKey, ArrayList<String> filesPaths, AsyncTaskCallback callback) {
        Map<String, String> headers = new HashMap<>();
        headers.put(JiraApiConst.AUTH, ActiveUser.getInstance().getCredentials());
        headers.put(JiraApiConst.ATLASSIAN_TOKEN, JiraApiConst.NO_CHECK);
        HttpClient.getClient().post(new Request.Builder()
                        .setUrl(ActiveUser.getInstance().getUrl() + JiraApiConst.ISSUE_PATH + issueKey + JiraApiConst.ATTACHMENTS_PATH)
                        .setHeaders(headers)
                        .setPostEntity(filesPaths),
                callback);
    }

    public static JiraApi get() {
        return INSTANCE;
    }

}
