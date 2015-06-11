package amtt.epam.com.amtt.api.http;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.util.Map;
import java.util.Set;

import amtt.epam.com.amtt.api.exception.AmttException;
import amtt.epam.com.amtt.util.Logger;

/**
 * Created by Artsiom_Kaliaha on 11.06.2015.
 */
@SuppressWarnings("unchecked")
public class HttpClient {

    public static enum OperationResult {

        SUCCESS,
        FAILURE

    }

    private final String TAG = getClass().getSimpleName();
    public static final int EMPTY_STATUS_CODE = -1;

    private HttpCallback mCallback;
    private static final org.apache.http.client.HttpClient sHttpClient;

    static {
        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 8000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        int timeoutSocket = 10000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        sHttpClient = new DefaultHttpClient(httpParameters);
    }

    public HttpClient(HttpCallback callback) {
        mCallback = callback;
    }

    private void get(HttpRequestParams httpRequestParams) throws AmttException {
        HttpGet httpGet = new HttpGet(httpRequestParams.getUrl());
        execute(httpGet, httpRequestParams);
    }

    private void post(HttpRequestParams httpRequestParams) throws AmttException {
        HttpPost httpPost = new HttpPost(httpRequestParams.getUrl());
        httpPost.setEntity(httpRequestParams.getPostEntity());
        execute(httpPost, httpRequestParams);
    }

    private void delete(HttpRequestParams httpRequestParams) throws AmttException {
        HttpDelete httpPost = new HttpDelete(httpRequestParams.getUrl());
        execute(httpPost, httpRequestParams);
    }

    private void execute(HttpRequestBase httpRequestBase, HttpRequestParams httpRequestParams) {
        Logger.d(TAG, httpRequestParams.getUrl());
        for (Map.Entry<String, String> keyValuePair : (Set<Map.Entry<String, String>>) httpRequestParams.getHeaders().entrySet()) {
            httpRequestBase.setHeader(keyValuePair.getKey(), keyValuePair.getValue());
        }
        new HttpTask.Builder()
                .setHttpRequest(httpRequestBase)
                .setProcessor(httpRequestParams.getProcessor())
                .setCallback(mCallback)
                .createAndExecute();
    }

    public static org.apache.http.client.HttpClient getApacheHttpClient() {
        return sHttpClient;
    }

}
