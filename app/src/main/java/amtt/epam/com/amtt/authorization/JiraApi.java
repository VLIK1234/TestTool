package amtt.epam.com.amtt.authorization;

import android.util.Base64;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import amtt.epam.com.amtt.util.Logger;

/**
 * Created by Artsiom_Kaliaha on 24.03.2015.
 */
public class JiraApi {

    private static final String BASE_PATH = "https://atmmjira.atlassian.net/rest/auth/latest/";
    private static final String LOGIN_METHOD = BASE_PATH + "session";

    private static final String AUTH_HEADER = "Authorization";
    private static final String BASIC_AUTH = "Basic ";

    private static final String ISSUE_PATH = "https://atmmjira.atlassian.net/rest/api/2/issue/";

    public static final int STATUS_AUTHORIZED = 200;
    public static final int STATUS_CREATED = 201;

    public int authorize(final String userName, final String password) throws Exception {
        String credentials = BASIC_AUTH + Base64.encodeToString((userName + ":" + password).getBytes(), Base64.NO_WRAP);

        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(LOGIN_METHOD);
        httpGet.setHeader(AUTH_HEADER, credentials);
        HttpResponse response = client.execute(httpGet);
        return response.getStatusLine().getStatusCode();

    }

    public int createIssue(final String userName, final String password, final String json) throws Exception {
        String credentials = BASIC_AUTH + Base64.encodeToString((userName + ":" + password).getBytes(), Base64.NO_WRAP);
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(ISSUE_PATH);
        StringEntity input = new StringEntity(json);
        post.addHeader(AUTH_HEADER, credentials);
        post.addHeader("content-type", "application/json");
        post.setEntity(input);
        Logger logger = new Logger();
        logger.printReqestLog(post);
        HttpResponse response = client.execute(post);
        logger.printResponceLog(response);
        return response.getStatusLine().getStatusCode();
    }

}


