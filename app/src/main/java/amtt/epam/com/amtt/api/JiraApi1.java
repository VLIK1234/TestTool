package amtt.epam.com.amtt.api;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import amtt.epam.com.amtt.api.http.postexecution.DefaultPostExecutionHandler;
import amtt.epam.com.amtt.api.http.HttpTask.HttpCallback;
import amtt.epam.com.amtt.api.http.HttpClient;
import amtt.epam.com.amtt.api.http.HttpRequestParams;
import amtt.epam.com.amtt.processing.Processor;
import amtt.epam.com.amtt.util.ActiveUser;

/**
 * Created by Artsiom_Kaliaha on 12.06.2015.
 * New api embodiment
 */
@SuppressWarnings("unchecked")
public class JiraApi1 {

    private static final JiraApi1 INSTANCE;

    static {
        INSTANCE = new JiraApi1();
    }

    private JiraApi1() {
    }

    public void signOut(HttpCallback httpCallback) {
        HttpClient.getClient().delete(new HttpRequestParams.Builder<>()
                        .setUrl(ActiveUser.getInstance().getUrl() + JiraApiConst.LOGIN_PATH)
                        .setPostExecutionHandler(new DefaultPostExecutionHandler())
                        .create(),
                httpCallback);
    }

    public <ResultType, InputType> void createIssue(HttpCallback httpCallback, final String postStringEntity, final Processor<ResultType, InputType> processor) {
        Map<String, String> headers = new HashMap<>();
        headers.put(JiraApiConst.AUTH, ActiveUser.getInstance().getCredentials());
        headers.put(JiraApiConst.CONTENT_TYPE, JiraApiConst.APPLICATION_JSON);
        HttpEntity postEntity = null;
        try {
            postEntity = new StringEntity(postStringEntity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpClient.getClient().post(new HttpRequestParams.Builder<Void>()
                        .setUrl(ActiveUser.getInstance().getUrl() + JiraApiConst.ISSUE_PATH)
                        .setHeaders(headers)
                        .setPostEntity(postEntity)
                        .setProcessor(processor)
                        .setPostExecutionHandler(new DefaultPostExecutionHandler())
                        .create(),
                httpCallback);
    }

    public <ResultType, InputType> void searchData(HttpCallback httpCallback,
                                                   final String requestSuffix,
                                                   final Processor<ResultType, InputType> processor,
                                                   final String userName,
                                                   final String password,
                                                   String url) {
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

        HttpClient.getClient().get(new HttpRequestParams.Builder()
                        .setUrl(url + requestSuffix)
                        .setHeaders(headers)
                        .setProcessor(processor)
                        .setPostExecutionHandler(new DefaultPostExecutionHandler())
                        .create(),
                httpCallback);
    }

    public void createAttachment(HttpCallback httpCallback, final String issueKey, ArrayList<String> fullFileName) {
        Map<String, String> headers = new HashMap<>();
        headers.put(JiraApiConst.AUTH, ActiveUser.getInstance().getCredentials());
        headers.put(JiraApiConst.ATLASSIAN_TOKEN, JiraApiConst.NO_CHECK);
        HttpEntity postEntity;
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        for (int i = 0; i < fullFileName.size(); i++) {
            String file = fullFileName.get(i);
            File fileToUpload = new File(file);
            if (file.contains(".png")) {
                multipartEntityBuilder.addBinaryBody("file", fileToUpload, ContentType.create("image/jpeg"),
                        fileToUpload.getName());
            } else if (file.contains(".txt")) {
                multipartEntityBuilder.addBinaryBody("file", fileToUpload, ContentType.create("text/plain"),
                        fileToUpload.getName());
            }

        }
        postEntity = multipartEntityBuilder.build();

        HttpClient.getClient().post(new HttpRequestParams.Builder<Void>()
                        .setUrl(ActiveUser.getInstance().getUrl() + JiraApiConst.ISSUE_PATH + issueKey + JiraApiConst.ATTACHMENTS_PATH)
                        .setHeaders(headers)
                        .setPostEntity(postEntity)
                        .setPostExecutionHandler(new DefaultPostExecutionHandler())
                        .create(),
                httpCallback);
    }

    public void buildAttachmentTxtCreating(HttpCallback httpCallback, final String issueKey, String fullfilename) {
        Map<String, String> headers = new HashMap<>();
        headers.put(JiraApiConst.AUTH, ActiveUser.getInstance().getCredentials());
        headers.put(JiraApiConst.ATLASSIAN_TOKEN, JiraApiConst.NO_CHECK);
        HttpEntity postEntity;
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        File fileToUpload = new File(fullfilename);
        multipartEntityBuilder.addBinaryBody("file", fileToUpload, ContentType.create("text/plain"),
                fileToUpload.getName());
        postEntity = multipartEntityBuilder.build();

        HttpClient.getClient().post(new HttpRequestParams.Builder<Void>()
                        .setUrl(ActiveUser.getInstance().getUrl() + JiraApiConst.ISSUE_PATH + issueKey + JiraApiConst.ATTACHMENTS_PATH)
                        .setHeaders(headers)
                        .setPostEntity(postEntity)
                        .setPostExecutionHandler(new DefaultPostExecutionHandler())
                        .create(),
                httpCallback);
    }

    public static JiraApi1 getInstance() {
        return INSTANCE;
    }

}
