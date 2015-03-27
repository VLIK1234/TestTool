package amtt.epam.com.amtt.util;

import android.util.Log;

import org.apache.http.client.methods.HttpPost;

/**
 * Created by Iryna_Monchanka on 3/27/2015.
 */
public class HttpLogger {

    public HttpLogger() {
    }

    public void printReqestLog(HttpPost post) {
        Log.i("HTTP Logger", "ProtocolVersion : " + post.getRequestLine().getProtocolVersion()
                + ", Method : " + post.getRequestLine().getMethod()
                + ", Uri : " + post.getRequestLine().getUri());
    }

    public void printResponceLog(org.apache.http.StatusLine statusLine) {
        Log.i("HTTP Logger", "ProtocolVersion : " + statusLine.getProtocolVersion()
                + ", StatusCode : " + statusLine.getStatusCode()
                + ", ReasonPhrase : " + statusLine.getReasonPhrase());
    }

}
