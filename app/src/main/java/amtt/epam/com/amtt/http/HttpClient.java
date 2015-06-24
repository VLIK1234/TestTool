package amtt.epam.com.amtt.http;

import org.apache.http.HttpEntity;
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

import amtt.epam.com.amtt.datasource.DataSource;
import amtt.epam.com.amtt.util.Logger;

/**
 * Created by Artsiom_Kaliaha on 11.06.2015.
 * HttpClient that prepares Requests to be executed
 */
@SuppressWarnings("unchecked")
public class HttpClient implements DataSource<HttpEntity, Request> {

    public static final String NAME = HttpClient.class.getName();
    private final String TAG = getClass().getSimpleName();

    public static final int EMPTY_STATUS_CODE = -1;

    private final DefaultHttpClient mHttpClient;

    public HttpClient() {
        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 8000;
        int timeoutSocket = 10000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        mHttpClient = new DefaultHttpClient(httpParameters);
    }

    public void get(Request request) {
        request.setHttpRequestBase(new HttpGet(request.getUrl()));
    }

    public void post(Request request) {
        HttpPost httpPost = new HttpPost(request.getUrl());
        httpPost.setEntity(request.getEntity());
        request.setHttpRequestBase(httpPost);
    }

    public void delete(Request request) {
        request.setHttpRequestBase(new HttpDelete(request.getUrl()));
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

    @Override
    public HttpEntity getData(Request request) throws Exception {
        switch (request.getType()) {
            case GET:
                get(request);
                break;
            case POST:
                post(request);
                break;
            case DELETE:
                delete(request);
                break;
        }
        setHeaders(request);

        HttpResponse httpResponse = mHttpClient.execute(request.getHttpRequestBase());
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        if (statusCode < HttpStatus.SC_OK || statusCode >= HttpStatus.SC_MULTIPLE_CHOICES) {
            throw new HttpException(null, statusCode, request, null);
        }

        return httpResponse.getEntity();
    }

    @Override
    public String getName() {
        return NAME;
    }

}
