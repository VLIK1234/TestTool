package amtt.epam.com.amtt.authorization;

import android.util.Base64;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import amtt.epam.com.amtt.bo.issue.TypeSearchedData;
import amtt.epam.com.amtt.util.Constants;
import amtt.epam.com.amtt.util.Logger;

/**
 * Created by Artsiom_Kaliaha on 24.03.2015.
 */
public class JiraApi {

    private static final String TAG = JiraApi.class.getSimpleName();

    private static final String BASE_PATH = "/rest/auth/latest/";
    private static final String LOGIN_METHOD = BASE_PATH + "session";

    private static final String AUTH_HEADER = "Authorization";


    private static final String ISSUE_PATH = "/rest/api/2/issue/";
    private static final String USER_PROJECTS_PATH = "/rest/api/2/issue/createmeta";
    private static final String USER_INFO_PATH = "/rest/api/2/user?username=";
    public static final int STATUS_AUTHORIZED = 200;
    public static final int STATUS_CREATED = 201;
    private HttpClient client = new DefaultHttpClient();
    private HttpGet httpGet;
    private HttpPost post;
    private HttpResponse response;


    //TODO OAUTH?
    public int authorize(final String credentials, final String mUrl) throws Exception {
        httpGet = new HttpGet(mUrl + LOGIN_METHOD);
        httpGet.setHeader(AUTH_HEADER, credentials);
        response = client.execute(httpGet);
        //TODO return status, not code
        return response.getStatusLine().getStatusCode();

    }

    public int createIssue(final String credentials, final String mUrl, final String json) throws Exception {
        post = new HttpPost(mUrl + ISSUE_PATH);
        StringEntity input = new StringEntity(json);
        post.addHeader(AUTH_HEADER, credentials);
        post.addHeader("content-type", "application/json");
        post.setEntity(input);
        Logger.printRequestPost(post);
        response = client.execute(post);
        Logger.printResponseLog(response);
        //TODO return status, not code
        return response.getStatusLine().getStatusCode();
    }

    /*
        public HttpEntity searchIssue(final String userName, final String password, final String mUrl) throws Exception {
            String credentials = BASIC_AUTH + Base64.encodeToString((userName + ":" + password).getBytes(), Base64.NO_WRAP);
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(mUrl + USER_PROJECTS_PATH);
            get.addHeader(AUTH_HEADER, credentials);
            HttpResponse httpRsponse = client.execute(get);
            // Logger.printResponseLog(httpRsponse);
            // Read all the text returned by the server
            return httpRsponse.getEntity();
        }

        public HttpEntity searchUserInfo(final String userName, final String password, final String mUrl) throws Exception {
            String credentials = BASIC_AUTH + Base64.encodeToString((userName + ":" + password).getBytes(), Base64.NO_WRAP);
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(mUrl + USER_INFO_PATH + userName);
            get.addHeader(AUTH_HEADER, credentials);
            HttpResponse httpRsponse = client.execute(get);
            return httpRsponse.getEntity();
        }
    */
    public HttpEntity searchData(final String userName, final String credentials, final String mUrl, final String typeData) throws Exception {
        TypeSearchedData request = TypeSearchedData.getType(typeData);
        Logger.d(TAG, String.valueOf(request));
        if (request != null) {
            switch (request) {
                case SEARCH_ISSUE:
                    httpGet = new HttpGet(mUrl + USER_PROJECTS_PATH);
                    break;
                case SEARCH_USER_INFO:
                    httpGet = new HttpGet(mUrl + USER_INFO_PATH + userName);
                    break;
            }
        }
        httpGet.addHeader(AUTH_HEADER, credentials);
        response = client.execute(httpGet);
        return response.getEntity();
    }

}


