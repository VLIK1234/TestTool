package amtt.epam.com.amtt.authorization;

import amtt.epam.com.amtt.bo.issue.TypeSearchedData;
import amtt.epam.com.amtt.util.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by Artsiom_Kaliaha on 24.03.2015.
 */
//TODO wrong package
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
    public static int authorize(final String credentials, final String mUrl) throws Exception {
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(mUrl + LOGIN_METHOD);
        httpGet.setHeader(AUTH_HEADER, credentials);
        HttpResponse response = client.execute(httpGet);
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


