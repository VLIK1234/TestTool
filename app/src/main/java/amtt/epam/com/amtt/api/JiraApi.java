package amtt.epam.com.amtt.api;

import android.content.Context;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import amtt.epam.com.amtt.api.rest.RestMethod;
import amtt.epam.com.amtt.api.rest.RestMethod.RestMethodType;
import amtt.epam.com.amtt.processing.AuthResponseProcessor;
import amtt.epam.com.amtt.processing.Processor;
import amtt.epam.com.amtt.util.ActiveUser;

/**
 * @author Artsiom_Kaliaha
 * @version on 24.03.2015
 */

@SuppressWarnings("unchecked")
public class JiraApi {

    private static class JiraApiSingletonHolder {

        public static final JiraApi INSTANCE = new JiraApi();

    }

    public static JiraApi getInstance() {
        return JiraApiSingletonHolder.INSTANCE;
    }

    private final ActiveUser mUser;
    private RestMethod mMethod;

    private JiraApi() {
        mUser = ActiveUser.getInstance();
    }

    public RestMethod buildAuth(final String userName, final String password, final String url) {
        Map<String, String> headers = new HashMap<>();
        headers.put(JiraApiConst.AUTH, mUser.makeTempCredentials(userName, password));
        mMethod = new RestMethod.Builder<String>()
                .setType(RestMethodType.GET)
                .setUrl(url + JiraApiConst.LOGIN_PATH)
                .setHeadersMap(headers)
                .setProcessor(new AuthResponseProcessor())
                .create();
        return mMethod;
    }

    public void signOut(JiraCallback jiraCallback) {
        mMethod = new RestMethod.Builder<String>()
                .setType(RestMethodType.DELETE)
                .setUrl(mUser.getUrl() + JiraApiConst.LOGIN_PATH)
                .create();
        execute(mMethod, jiraCallback);
    }

    public <ResultType, InputType> void createIssue(JiraCallback jiraCallback, final String postStringEntity, final Processor<ResultType, InputType> processor) {
        Map<String, String> headers = new HashMap<>();
        headers.put(JiraApiConst.AUTH, mUser.getCredentials());
        headers.put(JiraApiConst.CONTENT_TYPE, JiraApiConst.APPLICATION_JSON);
        HttpEntity postEntity = null;
        try {
            postEntity = new StringEntity(postStringEntity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        mMethod = new RestMethod.Builder<Void>()
                .setType(RestMethodType.POST)
                .setUrl(mUser.getUrl() + JiraApiConst.ISSUE_PATH)
                .setHeadersMap(headers)
                .setPostEntity(postEntity)
                .setProcessor(processor)
                .create();
        execute(mMethod, jiraCallback);
    }

    public <ResultType, InputType> void searchData(JiraCallback jiraCallback,
                                                   final String requestSuffix,
                                                   final Processor<ResultType, InputType> processor,
                                                   final String userName,
                                                   final String password,
                                                   String url) {
        String credentials;
        if (userName != null && password != null) {
            //this code is used when new user is added and we need to get all the info about a user and authorize him/her in one request
            credentials = mUser.makeTempCredentials(userName, password);
        } else {
            credentials = mUser.getCredentials();
            url = mUser.getUrl();
        }

        Map<String, String> headers = new HashMap<>();
        headers.put(JiraApiConst.AUTH, credentials);

        mMethod = new RestMethod.Builder()
                .setType(RestMethodType.GET)
                .setUrl(url + requestSuffix)
                .setHeadersMap(headers)
                .setProcessor(processor)
                .create();
        execute(mMethod, jiraCallback);
    }

    public void createAttachment(JiraCallback jiraCallback, final String issueKey, ArrayList<String> fullFileName) {
        Map<String, String> headers = new HashMap<>();
        headers.put(JiraApiConst.AUTH, mUser.getCredentials());
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
        mMethod = new RestMethod.Builder<Void>()
                .setType(RestMethodType.POST)
                .setUrl(mUser.getUrl() + JiraApiConst.ISSUE_PATH + issueKey + JiraApiConst.ATTACHMENTS_PATH)
                .setHeadersMap(headers)
                .setPostEntity(postEntity)
                .create();
        execute(mMethod, jiraCallback);
    }

    public RestMethod buildAttachmentTxtCreating(final String issueKey, String fullfilename) {
        Map<String, String> headers = new HashMap<>();
        headers.put(JiraApiConst.AUTH, mUser.getCredentials());
        headers.put(JiraApiConst.ATLASSIAN_TOKEN, JiraApiConst.NO_CHECK);
        HttpEntity postEntity;
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        File fileToUpload = new File(fullfilename);
        multipartEntityBuilder.addBinaryBody("file", fileToUpload, ContentType.create("text/plain"),
                fileToUpload.getName());
        postEntity = multipartEntityBuilder.build();
        mMethod = new RestMethod.Builder<Void>()
                .setType(RestMethodType.POST)
                .setUrl(mUser.getUrl() + JiraApiConst.ISSUE_PATH + issueKey + JiraApiConst.ATTACHMENTS_PATH)
                .setHeadersMap(headers)
                .setPostEntity(postEntity)
                .create();
        return mMethod;
    }

    private void execute(RestMethod restMethod, JiraCallback jiraCallback) {
        new JiraTask.Builder()
                .setRestMethod(restMethod)
                .setCallback(jiraCallback)
                .createAndExecute();
    }

}


