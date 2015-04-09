package amtt.epam.com.amtt.authorization;

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
//TODO wrong package
public class JiraApi {

    private static final String TAG = JiraApi.class.getSimpleName();
    public static final int STATUS_AUTHORIZED = 200;
    public static final int STATUS_CREATED = 201;
    private HttpClient client = new DefaultHttpClient();
    private HttpGet httpGet;
    private HttpPost post;
    private HttpResponse response;

    //TODO OAUTH?
    public static int authorize(final String credentials, final String mUrl) throws Exception {
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(mUrl + Constants.UrlKeys.LOGIN_METHOD);
        httpGet.setHeader(Constants.UrlKeys.AUTH_HEADER, credentials);
        HttpResponse response = client.execute(httpGet);
        //TODO return status, not code
        return response.getStatusLine().getStatusCode();

    }

    public int createIssue(final String credentials, final String mUrl, final String json) throws Exception {
        post = new HttpPost(mUrl + Constants.UrlKeys.ISSUE_PATH);
        StringEntity input = new StringEntity(json);
        post.addHeader(Constants.UrlKeys.AUTH_HEADER, credentials);
        post.addHeader(Constants.UrlKeys.CONTENT_TYPE, Constants.UrlKeys.APPLICATION_JSON);
        post.setEntity(input);
        Logger.printRequestPost(post);
        response = client.execute(post);
        Logger.printResponseLog(response);
        //TODO return status, not code
        return response.getStatusLine().getStatusCode();
    }

    public HttpEntity searchData(final String userName, final String credentials, final String mUrl, final TypeSearchedData typeData) throws Exception {

        if (typeData != null) {
            switch (typeData) {
                case SEARCH_ISSUE: {
                    httpGet = new HttpGet(mUrl + Constants.UrlKeys.USER_PROJECTS_PATH);
                    break;
                }
                case SEARCH_USER_INFO:
                    httpGet = new HttpGet(mUrl + Constants.UrlKeys.USER_INFO_PATH + userName + Constants.UrlKeys.EXPAND_GROUPS);
                    break;
            }
        }
        httpGet.addHeader(Constants.UrlKeys.AUTH_HEADER, credentials);
        response = client.execute(httpGet);
        return response.getEntity();
    }

}


