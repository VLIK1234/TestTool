package amtt.epam.com.amtt.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 @author Iryna Monchanka
 @version on 3/27/2015
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

    public static void d(String tag, String message) {
        if (IS_SHOW_LOGS) {
            android.util.Log.d(tag, message);
        }
    }

    public static void d(String tag, String message, Throwable throwable) {
        if (IS_SHOW_LOGS) {
            android.util.Log.d(tag, message, throwable);
        }
    }

    public static void i(String tag, String message) {
        if (IS_SHOW_LOGS) {
            android.util.Log.i(tag, message);
        }
    }

    public static void i(String tag, String message, Throwable throwable) {
        if (IS_SHOW_LOGS) {
            android.util.Log.i(tag, message, throwable);
        }
    }

    public static void e(String tag, String message) {
        if (IS_SHOW_LOGS) {
            android.util.Log.e(tag, message);
        }
    }

    public static void e(String tag, String message, Throwable throwable) {
        if (IS_SHOW_LOGS) {
            android.util.Log.e(tag, message, throwable);
        }
    }

    public static void v(String tag, String message) {
        if (IS_SHOW_LOGS) {
            android.util.Log.v(tag, message);
        }
    }

    public static void v(String tag, String message, Throwable throwable) {
        if (IS_SHOW_LOGS) {
            android.util.Log.v(tag, message, throwable);
        }
    }

    public static void w(String tag, Throwable throwable) {
        if (IS_SHOW_LOGS) {
            android.util.Log.w(tag, throwable);
        }
    }

    public static void w(String tag, String message, Throwable throwable) {
        if (IS_SHOW_LOGS) {
            android.util.Log.w(tag, message, throwable);
        }
    }

    public static void printRequestPost(HttpPost post) throws IOException {
        HttpEntity entity = post.getEntity();
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
