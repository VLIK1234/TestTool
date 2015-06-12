package amtt.epam.com.amtt.api.http;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;

import java.util.Map;
import java.util.Set;

import amtt.epam.com.amtt.util.Logger;
import amtt.epam.com.amtt.api.http.HttpTask.HttpCallback;

/**
 * Created by Artsiom_Kaliaha on 11.06.2015.
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

    public void get(HttpRequestParams httpRequestParams, HttpCallback callback) {
        HttpGet httpGet = new HttpGet(httpRequestParams.getUrl());
        execute(httpGet, httpRequestParams, callback);
    }

    public void post(HttpRequestParams httpRequestParams, HttpCallback callback) {
        HttpPost httpPost = new HttpPost(httpRequestParams.getUrl());
        httpPost.setEntity(httpRequestParams.getPostEntity());
        execute(httpPost, httpRequestParams, callback);
    }

    public void delete(HttpRequestParams httpRequestParams, HttpCallback callback) {
        HttpDelete httpDelete = new HttpDelete(httpRequestParams.getUrl());
        execute(httpDelete, httpRequestParams, callback);
    }

    private void execute(HttpRequestBase httpRequestBase, HttpRequestParams httpRequestParams, HttpCallback callback) {
        Logger.d(TAG, httpRequestParams.getUrl());
        for (Map.Entry<String, String> keyValuePair : (Set<Map.Entry<String, String>>) httpRequestParams.getHeaders().entrySet()) {
            httpRequestBase.setHeader(keyValuePair.getKey(), keyValuePair.getValue());
        }
        new HttpTask.Builder()
                .setHttpRequest(httpRequestBase)
                .setProcessor(httpRequestParams.getProcessor())
                .setPostExecutionHandler(httpRequestParams.getPostExecutionHandler())
                .setCallback(callback)
                .createAndExecute();
    }

    public static HttpClient getClient() {
        return INSTANCE;
    }

}
