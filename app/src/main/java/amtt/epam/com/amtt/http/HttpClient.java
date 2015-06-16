package amtt.epam.com.amtt.http;

import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import java.util.Map;
import java.util.Set;

import amtt.epam.com.amtt.os.Task;
import amtt.epam.com.amtt.os.Task.AsyncTaskCallback;
import amtt.epam.com.amtt.processing.HttpProcessor;
import amtt.epam.com.amtt.util.Logger;

/**
 * Created by Artsiom_Kaliaha on 11.06.2015.
 * HttpClient that prepares Requests to be executed
 */
@SuppressWarnings("unchecked")
public class HttpClient {

    private final String TAG = getClass().getSimpleName();

    private static final HttpClient INSTANCE;
    public static final int EMPTY_STATUS_CODE = -1;

    static {
        INSTANCE = new HttpClient();
    }

    private HttpClient() {
    }

    public void get(Request.Builder requestBuilder, AsyncTaskCallback asyncTaskCallback) {
        requestBuilder.setHttpRequestBase(new HttpGet(requestBuilder.getUrl()));
        execute(requestBuilder, asyncTaskCallback);
    }

    public void post(Request.Builder requestBuilder, AsyncTaskCallback asyncTaskCallback) {
        HttpPost httpPost = new HttpPost(requestBuilder.getUrl());
        httpPost.setEntity(requestBuilder.getPostEntity());
        requestBuilder.setHttpRequestBase(httpPost);
        execute(requestBuilder, asyncTaskCallback);
    }

    public void delete(Request.Builder requestBuilder, AsyncTaskCallback asyncTaskCallback) {
        requestBuilder.setHttpRequestBase(new HttpDelete(requestBuilder.getUrl()));
        execute(requestBuilder, asyncTaskCallback);
    }

    private void execute(Request.Builder requestBuilder, AsyncTaskCallback asyncTaskCallback) {
        Logger.d(TAG, requestBuilder.getUrl());
        if (requestBuilder.getHeaders() != null) {
            for (Map.Entry<String, String> keyValuePair : (Set<Map.Entry<String, String>>) requestBuilder.getHeaders().entrySet()) {
                requestBuilder.getHttpRequestBase().setHeader(keyValuePair.getKey(), keyValuePair.getValue());
            }
        }
        Request request = requestBuilder.create();
        new Task.Builder<HttpResult,HttpRequest>()
                .setExecutable(request)
                .setProcessor(new HttpProcessor(request))
                .setCallback(asyncTaskCallback)
                .createAndExecute();
    }

    public static HttpClient getClient() {
        return INSTANCE;
    }

}
