package amtt.epam.com.amtt.util;

import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by Iryna_Monchanka on 3/27/2015.
 */
public class Logger {

    public Logger() {
    }

    public static void printReqestLog(HttpPost post) throws IOException {

        HttpEntity entity = post.getEntity();

        //
        // Read the contents of an entity and return it as a String.
        //
        String content = EntityUtils.toString(entity);
        Log.i("HTTP Logger", "ProtocolVersion : " + post.getRequestLine().getProtocolVersion()
            + ", Method : " + post.getRequestLine().getMethod()
            + ", Uri : " + post.getRequestLine().getUri() + ", Body : " + content);

    }

    public static void printResponceLog(HttpResponse response) throws IOException {

        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity, "UTF-8");
        Log.i("HTTP Logger", "ProtocolVersion : " + response.getStatusLine().getProtocolVersion()
            + ", StatusCode : " + response.getStatusLine().getStatusCode()
            + ", ReasonPhrase : " + response.getStatusLine().getReasonPhrase() + ", Body : " + content);
        response.getStatusLine().toString();
    }

}
