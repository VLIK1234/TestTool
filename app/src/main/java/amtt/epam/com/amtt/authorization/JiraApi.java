package amtt.epam.com.amtt.authorization;

import android.util.Base64;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;

/**
 * Created by Artsiom_Kaliaha on 24.03.2015.
 */
public class JiraApi {

    private static final String BASE_PATH = "https://atmmjira.atlassian.net/rest/auth/latest/";
    private static final String LOGIN_METHOD = BASE_PATH + "session";

    private static final String AUTH_HEADER = "Authorization";
    private static final String BASIC_AUTH = "Basic ";

    public static final int STATUS_AUTHORIZED = 200;

    public int authorize(final String userName, final String password) throws Exception {
        String credentials = BASIC_AUTH + Base64.encodeToString((userName + ":" + password).getBytes(), Base64.NO_WRAP);

        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(LOGIN_METHOD);
        httpGet.setHeader(AUTH_HEADER, credentials);
        HttpResponse response = client.execute(httpGet);
        return response.getStatusLine().getStatusCode();

    }

}
