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
    private static final Boolean IS_SHOW_LOGS = true;

    public Logger() {
    }

    public static void d(String className, String message) {
        if (IS_SHOW_LOGS) {
            android.util.Log.d(className, message);
        }
    }

    public static void i(String className, String message) {
        if (IS_SHOW_LOGS) {
            android.util.Log.i(className, message);
        }
    }

    public static void e(String className, String message) {
        if (IS_SHOW_LOGS) {
            android.util.Log.e(className, message);
        }
    }

    public static void printRequestPost(HttpPost post) throws IOException {
        HttpEntity entity = post.getEntity();

        //
        // Read the contents of an entity and return it as a String.
        //
        String content = EntityUtils.toString(entity);
        Logger.i(TAG, PROTOCOL_VERSION + post.getRequestLine().getProtocolVersion());
        Logger.i(TAG, METHOD + post.getRequestLine().getMethod());
        Logger.i(TAG, URI + post.getRequestLine().getUri());
        Logger.i(TAG, BODY + content);

    }

    public static void printRequestGet(HttpGet get) throws IOException {
        Logger.i(TAG, PROTOCOL_VERSION + get.getRequestLine().getProtocolVersion());
        Logger.i(TAG, METHOD + get.getRequestLine().getMethod());
        Logger.i(TAG, URI + get.getRequestLine().getUri());

    }

    public static void printResponseLog(HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity, ENCODING_UTF_8);
        Logger.i(TAG, PROTOCOL_VERSION + response.getStatusLine().getProtocolVersion());
        Logger.i(TAG, STATUS_CODE + response.getStatusLine().getStatusCode());
        Logger.i(TAG, REASON_PHRASE + response.getStatusLine().getReasonPhrase());
        Logger.i(TAG, BODY + content);
    }

}
