package amtt.epam.com.amtt.authorization;

import android.util.Base64;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by Artsiom_Kaliaha on 24.03.2015.
 */
public class JiraApi {

    private static final String BASE_PATH = "https://atmmjira.atlassian.net/rest/auth/latest/session";

    public String authorize(String userName, String password) throws Exception {
        String credentials = Base64.encodeToString((userName + ":" + password).getBytes(), Base64.NO_WRAP);

        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(BASE_PATH);
        httpGet.setHeader("Authentication", "Basic " + credentials);
        HttpResponse response = client.execute(httpGet);
        return null;

    }

}
