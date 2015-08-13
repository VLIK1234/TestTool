package amtt.epam.com.amtt.api;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import amtt.epam.com.amtt.AmttApplication;
import amtt.epam.com.amtt.common.Callback;
import amtt.epam.com.amtt.common.CoreApplication;
import amtt.epam.com.amtt.common.DataRequest;
import amtt.epam.com.amtt.http.HttpClient;
import amtt.epam.com.amtt.http.HttpException;
import amtt.epam.com.amtt.http.Request;
import amtt.epam.com.amtt.http.Request.Type;
import amtt.epam.com.amtt.util.ActiveUser;

/**
 * Created by Artsiom_Kaliaha on 12.06.2015.
 * Updated api implementation
 */
public class JiraApi {

    private static final JiraApi INSTANCE;
    private ActiveUser mUser = ActiveUser.getInstance();

    static {
        INSTANCE = new JiraApi();
    }

    private JiraApi() {
    }

    public void signOut() {
        Request.Builder requestBuilder = new Request.Builder()
                .setType(Type.DELETE)
                .setUrl(mUser.getUrl() + JiraApiConst.LOGIN_PATH);
        execute(requestBuilder, CoreApplication.NO_PROCESSOR, null);
    }

    public void createIssue(String postEntityString, String processorName, Callback callback) {
        Map<String, String> headers = new HashMap<>();
        headers.put(JiraApiConst.AUTH, mUser.getCredentials());
        headers.put(JiraApiConst.CONTENT_TYPE, JiraApiConst.APPLICATION_JSON);

        HttpEntity entity;
        try {
            entity = new StringEntity(postEntityString);
        } catch (UnsupportedEncodingException e) {
            if (callback != null) {
                callback.onLoadError(new HttpException(HttpClient.EMPTY_STATUS_CODE));
            }
            return;
        }

        Request.Builder requestBuilder = new Request.Builder()
                .setType(Type.POST)
                .setUrl(mUser.getUrl() + JiraApiConst.ISSUE_PATH)
                .setHeaders(headers)
                .setEntity(entity);
        execute(requestBuilder, processorName, callback);
    }

    public void searchData(String requestSuffix, String processorName, String userName, String password, String url, Callback callback) {
        String credentials;
        if (userName != null && password != null) {
            //this code is used when new user is added and we need to getClient all the info about a user and authorize him/her in one request
            credentials = mUser.makeTempCredentials(userName, password);
        } else {
            credentials = mUser.getCredentials();
            url = mUser.getUrl();
        }
        Map<String, String> headers = new HashMap<>();
        headers.put(JiraApiConst.AUTH, credentials);

        Request.Builder requestBuilder = new Request.Builder()
                .setType(Type.GET)
                .setUrl(url + requestSuffix)
                .setHeaders(headers);
        execute(requestBuilder, processorName, callback);
    }

    public void createAttachment(String issueKey, List<String> filesPaths, Callback callback) {
        Map<String, String> headers = new HashMap<>();
        headers.put(JiraApiConst.AUTH, mUser.getCredentials());
        headers.put(JiraApiConst.ATLASSIAN_TOKEN, JiraApiConst.NO_CHECK);

        Request.Builder requestBuilder = new Request.Builder()
                .setType(Type.POST)
                .setUrl(mUser.getUrl() + JiraApiConst.ISSUE_PATH + issueKey + JiraApiConst.ATTACHMENTS_PATH)
                .setHeaders(headers)
                .setEntity(filesPaths);
        execute(requestBuilder, CoreApplication.NO_PROCESSOR, callback);
    }

    public static JiraApi get() {
        return INSTANCE;
    }

    private void execute(Request.Builder requestBuilder, String processorName, Callback callback) {
        Request request = requestBuilder.build();
        AmttApplication.executeRequest(new DataRequest<>(HttpClient.NAME, request, processorName, callback));
    }

}
