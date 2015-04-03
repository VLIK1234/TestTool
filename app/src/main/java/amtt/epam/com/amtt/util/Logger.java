package amtt.epam.com.amtt.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by Iryna_Monchanka on 3/27/2015.
 */
public class Logger {

    private static final String TAG = Logger.class.getSimpleName();
    private static final String ENCODING_UTF_8 = "UTF-8";
    private static final String PROTOCOL_VERSION = "ProtocolVersion : ";
    private static final String METHOD = "Method : ";
    private static final String URI = "Uri : ";
    private static final String BODY = "Body : ";
    private static final String STATUS_CODE = "StatusCode : ";
    private static final String REASON_PHRASE = "ReasonPhrase : ";

    public Logger() {
    }

    public static void printRequestPost(HttpPost post) throws IOException {
        HttpEntity entity = post.getEntity();

        //
        // Read the contents of an entity and return it as a String.
        //
        String content = EntityUtils.toString(entity);
        Log.i(TAG, PROTOCOL_VERSION + post.getRequestLine().getProtocolVersion());
        Log.i(TAG, METHOD + post.getRequestLine().getMethod());
        Log.i(TAG, URI + post.getRequestLine().getUri());
        Log.i(TAG, BODY + content);

    }

    public static void printRequestGet(HttpGet get) throws IOException {
        Log.i(TAG, PROTOCOL_VERSION + get.getRequestLine().getProtocolVersion());
        Log.i(TAG, METHOD + get.getRequestLine().getMethod());
        Log.i(TAG, URI + get.getRequestLine().getUri());

    }

    public static void printResponseLog(HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity, ENCODING_UTF_8);
        Log.i(TAG, PROTOCOL_VERSION + response.getStatusLine().getProtocolVersion());
        Log.i(TAG, STATUS_CODE + response.getStatusLine().getStatusCode());
        Log.i(TAG, REASON_PHRASE + response.getStatusLine().getReasonPhrase());
        Log.i(TAG, BODY + content);
    }

}
