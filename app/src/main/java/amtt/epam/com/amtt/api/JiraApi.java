package amtt.epam.com.amtt.api;

import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

import amtt.epam.com.amtt.authorization.AuthorizationResult;
import amtt.epam.com.amtt.bo.issue.TypeSearchedData;
import amtt.epam.com.amtt.bo.issue.willrefactored.CreationIssueResult;
import amtt.epam.com.amtt.util.Constants;
import amtt.epam.com.amtt.util.CredentialsManager;
import amtt.epam.com.amtt.util.Kontext;
import amtt.epam.com.amtt.util.Logger;

/**
 * Created by Artsiom_Kaliaha on 24.03.2015.
 */
public enum JiraApi {
    INSTANCE;
    public static String BASIC_AUTH = "Basic ";
    public static String BASE_PATH = "/rest/auth/latest/";
    public static String LOGIN_METHOD = BASE_PATH + "session";
    public static String AUTH_HEADER = "Authorization";
    public static String ISSUE_PATH = "/rest/api/2/issue/";
    public static String USER_PROJECTS_PATH = "/rest/api/2/issue/createmeta";
    public static String USER_INFO_PATH = "/rest/api/2/user?username=";
    public static String EXPAND_GROUPS = "&expand=groups";
    public static String CONTENT_TYPE = "content-type";
    public static String APPLICATION_JSON = "application/json";
    
    private static final String TAG = JiraApi.class.getSimpleName();
    public static final int STATUS_AUTHORIZED = 200;
    public static final int STATUS_CREATED = 201;
    private HttpClient client = new DefaultHttpClient();
    private HttpResponse response;
    //TODO What if url is changed?
    final private String mUrl = CredentialsManager.getInstance().getUrl();

    //TODO OAUTH?
    public AuthorizationResult authorize(){
        try {
            HttpGet httpGet = new HttpGet(mUrl + LOGIN_METHOD);
            httpGet.setHeader(AUTH_HEADER, getCredential());
            HttpResponse response = client.execute(httpGet);
            switch (response.getStatusLine().getStatusCode()) {
                case STATUS_AUTHORIZED:
                    return AuthorizationResult.AUTHORIZATION_SUCCESS;
                default: return AuthorizationResult.AUTHORIZATION_DENIED;
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
            //TODO if exception - we think it is success O_O
            return AuthorizationResult.AUTHORIZATION_SUCCESS;
        }
    }

    public CreationIssueResult createIssue(final String json){

        try {
            //TODO is it ok to set headers and entity when request is executed?
            HttpPost post = new HttpPost(mUrl + ISSUE_PATH);
            StringEntity input = new StringEntity(json);
            Logger.printRequestPost(post);
            response = client.execute(post);
            Logger.printResponseLog(response);
            post.addHeader(AUTH_HEADER, getCredential());
            post.addHeader(CONTENT_TYPE, APPLICATION_JSON);
            post.setEntity(input);

            switch (response.getStatusLine().getStatusCode()) {
                case JiraApi.STATUS_CREATED:
                    return CreationIssueResult.CREATION_SUCCESS;
                default: return CreationIssueResult.CREATION_UNSUCCESS;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
            return CreationIssueResult.CREATION_UNSUCCESS;
        }
    }

    public HttpEntity searchData(final String userName, final TypeSearchedData typeData) throws Exception {
        HttpGet httpGet = new HttpGet();

        if (typeData != null) {
            switch (typeData) {
                case SEARCH_ISSUE: {
                    httpGet = new HttpGet(mUrl + USER_PROJECTS_PATH);
                    break;
                }
                case SEARCH_USER_INFO:
                    httpGet = new HttpGet(mUrl + USER_INFO_PATH + userName + EXPAND_GROUPS);
                    break;
            }
        }
        //TODO and what if typeData is null?

        httpGet.addHeader(AUTH_HEADER, getCredential());
        response = client.execute(httpGet);
        return response.getEntity();
    }

    public String getCredential(){
        //TODO why do we encode this line every time?
        return JiraApi.BASIC_AUTH + Base64.encodeToString((CredentialsManager.getInstance().getUserName() +
                Constants.SharedPreferenceKeys.COLON + CredentialsManager.getInstance().getPassword()).getBytes(), Base64.NO_WRAP);
    }

}


