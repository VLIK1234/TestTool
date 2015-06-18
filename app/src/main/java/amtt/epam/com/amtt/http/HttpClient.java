package amtt.epam.com.amtt.http;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
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
import java.util.concurrent.LinkedBlockingQueue;

import amtt.epam.com.amtt.datasource.IDataSource;
import amtt.epam.com.amtt.util.Logger;

/**
 * Created by Artsiom_Kaliaha on 11.06.2015.
 * HttpClient that prepares Requests to be executed
 */
@SuppressWarnings("unchecked")
public class HttpClient implements IDataSource<HttpResponse, Request> {

    private final String TAG = getClass().getSimpleName();
    public static final String SOURCE_NAME = "HttpClient";

    private static final HttpClient INSTANCE;
    public static final int EMPTY_STATUS_CODE = -1;

    private static final org.apache.http.client.HttpClient sHttpClient;

    static {
        INSTANCE = new HttpClient();

        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 8000;
        int timeoutSocket = 10000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        sHttpClient = new DefaultHttpClient(httpParameters);
    }

    private HttpClient() {
    }

    public void get(Request.Builder requestBuilder) {
        requestBuilder.setHttpRequestBase(new HttpGet(requestBuilder.getUrl()));
        setHeaders(requestBuilder);
    }

    public void post(Request.Builder requestBuilder) {
        HttpPost httpPost = new HttpPost(requestBuilder.getUrl());
        httpPost.setEntity(requestBuilder.getPostEntity());
        requestBuilder.setHttpRequestBase(httpPost);
        setHeaders(requestBuilder);
    }

    public void delete(Request.Builder requestBuilder) {
        requestBuilder.setHttpRequestBase(new HttpDelete(requestBuilder.getUrl()));
        setHeaders(requestBuilder);
    }

    private void setHeaders(Request.Builder requestBuilder) {
        Logger.d(TAG, requestBuilder.getUrl());
        Map headers = requestBuilder.getHeaders();
        if (headers != null) {
            HttpRequestBase requestBase = requestBuilder.getHttpRequestBase();
            for (Map.Entry<String, String> keyValuePair : (Set<Map.Entry<String, String>>) headers.entrySet()) {
                requestBase.setHeader(keyValuePair.getKey(), keyValuePair.getValue());
            }
        }
    }

    public static HttpClient getClient() {
        return INSTANCE;
    }

    @Override
    public HttpResponse getData(Request request) throws Exception {
        HttpResponse httpResponse = sHttpClient.execute(request.getHttpRequestBase());

        int statusCode = httpResponse.getStatusLine().getStatusCode();
        String requestMethodName = request.getHttpRequestBase().getMethod();

        if (requestMethodName.equals(HttpGet.METHOD_NAME) && statusCode != HttpStatus.SC_OK ||
                requestMethodName.equals(HttpPost.METHOD_NAME) && statusCode != HttpStatus.SC_CREATED) {
            throw new HttpException(null, statusCode, request, null);
        }

        return httpResponse;
    }

}
