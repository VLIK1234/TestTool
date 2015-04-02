package amtt.epam.com.amtt.authorization;

import amtt.epam.com.amtt.util.Logger;
import android.util.Base64;
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
public class JiraApi {

    private static final String BASE_PATH = "/rest/auth/latest/";
    private static final String LOGIN_METHOD = BASE_PATH + "session";

    private static final String AUTH_HEADER = "Authorization";
    private static final String BASIC_AUTH = "Basic ";

    private static final String ISSUE_PATH = "/rest/api/2/issue/";
    private static final String USER_PROJECTS_PATH = "/rest/api/2/issue/createmeta";
    public static final int STATUS_AUTHORIZED = 200;
    public static final int STATUS_CREATED = 201;

    public int authorize(final String userName, final String password, final String mUrl) throws Exception {
        String credentials = BASIC_AUTH + Base64.encodeToString((userName + ":" + password).getBytes(), Base64.NO_WRAP);

        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(mUrl + LOGIN_METHOD);
        httpGet.setHeader(AUTH_HEADER, credentials);
        HttpResponse response = client.execute(httpGet);
        return response.getStatusLine().getStatusCode();

    }

    public int createIssue(final String userName, final String password, final String mUrl, final String json) throws Exception {
        String credentials = BASIC_AUTH + Base64.encodeToString((userName + ":" + password).getBytes(), Base64.NO_WRAP);
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(mUrl + ISSUE_PATH);
        StringEntity input = new StringEntity(json);
        post.addHeader(AUTH_HEADER, credentials);
        post.addHeader("content-type", "application/json");
        post.setEntity(input);
        Logger.printReqestLog(post);
        HttpResponse response = client.execute(post);
        //  Logger.printResponceLog(response);
        return response.getStatusLine().getStatusCode();
    }

    public HttpEntity searchIssue(final String userName, final String password, final String mUrl) throws Exception {
        String credentials = BASIC_AUTH + Base64.encodeToString((userName + ":" + password).getBytes(), Base64.NO_WRAP);
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(mUrl + USER_PROJECTS_PATH);
        get.addHeader(AUTH_HEADER, credentials);
        HttpResponse httpRsponse = client.execute(get);
        // Logger.printResponceLog(httpRsponse);
        // Read all the text returned by the server
        HttpEntity getResponseEntity = httpRsponse.getEntity();
        return getResponseEntity;
    }

}


