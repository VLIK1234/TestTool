package amtt.epam.com.amtt.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.util.Map;
import java.util.Set;

import amtt.epam.com.amtt.datasource.DataSource;
import amtt.epam.com.amtt.util.Logger;

/**
 @author Artsiom_Kaliaha
 @version on 11.06.2015
 */

public class HttpClient implements DataSource<Request, HttpEntity> {

    private final String TAG = getClass().getSimpleName();
    public static final int EMPTY_STATUS_CODE = -1;
    private final DefaultHttpClient mHttpClient;

    public HttpClient() {
        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 8000;
        int timeoutSocket = 10000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
//        mHttpClient = new DefaultHttpClient(httpParameters);
        mHttpClient = getThreadSafeClient();
    }

    public static DefaultHttpClient getThreadSafeClient()  {
        DefaultHttpClient client = new DefaultHttpClient();
        ClientConnectionManager mgr = client.getConnectionManager();
        HttpParams params = client.getParams();
        client = new DefaultHttpClient(new ThreadSafeClientConnManager(params,

                mgr.getSchemeRegistry()), params);
        return client;
    }

    private void setHeaders(Request request) {
        Logger.d(TAG, request.getUrl());
        Map headers = request.getHeaders();
        if (headers != null) {
            HttpRequestBase requestBase = request.getHttpRequestBase();
            for (Map.Entry<String, String> keyValuePair : (Set<Map.Entry<String, String>>) headers.entrySet()) {
                requestBase.setHeader(keyValuePair.getKey(), keyValuePair.getValue());
            }
        }
    }

    private HttpRequestBase getRequestBase(Request.Type type, String url, HttpEntity entity) {
        HttpRequestBase httpRequestBase = null;
        switch (type) {
            case GET:
                httpRequestBase = new HttpGet(url);
                break;
            case POST:
                httpRequestBase = new HttpPost(url);
                if(entity != null) {
                    ((HttpPost) httpRequestBase).setEntity(entity);
                }
                break;
            case DELETE:
                httpRequestBase = new HttpDelete(url);
                break;
        }
        return httpRequestBase;
    }

    @Override
    public HttpEntity getData(Request request) throws Exception {
        if (request == null) {
            throw new IllegalArgumentException("Illegal request for HttpClient");
        }
        request.setHttpRequestBase(getRequestBase(request.getType(), request.getUrl(), request.getEntity()));
        setHeaders(request);
        HttpResponse httpResponse = mHttpClient.execute(request.getHttpRequestBase());
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode < HttpStatus.SC_OK || statusCode >= HttpStatus.SC_MULTIPLE_CHOICES) {
            throw new HttpException(statusCode);
        }
        return httpResponse.getEntity();
    }

}
