package amtt.epam.com.amtt.api.rest;

import amtt.epam.com.amtt.util.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.Map;

import amtt.epam.com.amtt.api.exception.AmttException;
import amtt.epam.com.amtt.api.result.JiraOperationResult;
import amtt.epam.com.amtt.processing.Processor;

/**
 * Class for performing REST methods to Jira api
 * Created by Artsiom_Kaliaha on 15.04.2015.
 */
public class RestMethod<ResultType> {

    public enum RestMethodType {

        GET,
        POST

    }

    public static class Builder<ResultType> {

        private RestMethodType mRestMethodType;
        private Map<String, String> mHeaders;
        private String mUrl;
        private Processor<ResultType, HttpEntity> mProcessor; //processor for retrieving OBJECTS
        private String mPostEntity;

        public Builder setType(RestMethodType methodType) {
            mRestMethodType = methodType;
            return this;
        }

        /*
        * Set headers for current method
        * @param headers Headers for method in form (headerName, headerValue)
        * */
        public Builder setHeadersMap(Map<String, String> headers) {
            mHeaders = headers;
            return this;
        }

        public Builder setUrl(String url) {
            mUrl = url;
            return this;
        }

        public Builder setProcessor(Processor<ResultType, HttpEntity> processor) {
            mProcessor = processor;
            return this;
        }

        public Builder setPostEntity(String postEntity) {
            mPostEntity = postEntity;
            return this;
        }

        public RestMethod create() {
            RestMethod<ResultType> restMethod = new RestMethod<>();
            restMethod.mRestMethodType = this.mRestMethodType;
            restMethod.mHeaders = this.mHeaders;
            restMethod.mUrl = this.mUrl;
            restMethod.mProcessor = this.mProcessor;
            restMethod.mPostEntity = this.mPostEntity;
            return restMethod;
        }

    }

    public static final int EMPTY_STATUS_CODE = -1;

    private static HttpClient mHttpClient;
    private RestMethodType mRestMethodType;
    private Map<String, String> mHeaders;
    private String mUrl;
    private Processor<ResultType, HttpEntity> mProcessor;
    private String mPostEntity;

    private static final String TAG = RestMethod.class.getSimpleName();

    static {
        mHttpClient = new DefaultHttpClient();
    }

    public RestMethod() {
    }

    private HttpResponse get() throws AmttException {
        HttpGet httpGet = new HttpGet(mUrl);
        for (Map.Entry<String, String> keyValuePair : mHeaders.entrySet()) {
            httpGet.setHeader(keyValuePair.getKey(), keyValuePair.getValue());
        }

        HttpResponse httpResponse;
        try {
            httpResponse = mHttpClient.execute(httpGet);
        } catch (IllegalStateException e) {
            Logger.e(TAG, e.getMessage(), e.getCause());
            throw new AmttException(e, EMPTY_STATUS_CODE, this, null);
        } catch (IllegalArgumentException e) {
            Logger.e(TAG, e.getMessage(), e.getCause());
            throw new AmttException(e, EMPTY_STATUS_CODE, this, null);
        } catch (UnknownHostException e) {
            Logger.e(TAG, e.getMessage(), e.getCause());
            throw new AmttException(e, EMPTY_STATUS_CODE, this, null);
        } catch (ClientProtocolException e) {
            Logger.e(TAG, e.getMessage(), e.getCause());
            throw new AmttException(e, EMPTY_STATUS_CODE, this, null);
        } catch (IOException e) {
            Logger.e(TAG, e.getMessage(), e.getCause());
            throw new AmttException(e, EMPTY_STATUS_CODE, this, null);
        }
        return httpResponse;
    }

    private HttpResponse post() throws AmttException {
        HttpPost httpPost = new HttpPost(mUrl);
        try {
            httpPost.setEntity(new StringEntity(mPostEntity));
        } catch (UnsupportedEncodingException e) {
            Logger.e(TAG, e.getMessage(), e.getCause());
            throw new AmttException(e, EMPTY_STATUS_CODE, this, null);
        }
        for (Map.Entry<String, String> keyValuePair : mHeaders.entrySet()) {
            httpPost.setHeader(keyValuePair.getKey(), keyValuePair.getValue());
        }

        HttpResponse httpResponse;
        try {
            httpResponse = mHttpClient.execute(httpPost);
        } catch (ClientProtocolException e) {
            Logger.e(TAG, e.getMessage(), e.getCause());
            throw new AmttException(e, EMPTY_STATUS_CODE, this, null);
        } catch (IOException e) {
            Logger.e(TAG, e.getMessage(), e.getCause());
            throw new AmttException(e, EMPTY_STATUS_CODE, this, null);
        }
        return httpResponse;
    }

    public RestResponse<ResultType> execute() throws AmttException {
        HttpResponse httpResponse = null;

        switch (mRestMethodType) {
            case GET:
                httpResponse = get();
                break;
            case POST:
                httpResponse = post();
                break;
        }

        RestResponse<ResultType> restResponse = new RestResponse<>();
        ResultType result;
        HttpEntity entity = null;
        //TODO we have processor only for successful request. You can get parse error before wrong status code.  Check codes before.
        try {
            if (mProcessor != null) {
                entity = httpResponse.getEntity();
                result = mProcessor.process(entity);
                restResponse.setResultObject(result);
            }
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage(), e.getCause());
            throw new AmttException(e, httpResponse.getStatusLine().getStatusCode(), this, entity);
        }

        int statusCode = httpResponse.getStatusLine().getStatusCode();
        //TODO what if return result only on good codes, such as 200?
        if (statusCode == HttpStatus.SC_NOT_FOUND || statusCode == HttpStatus.SC_BAD_GATEWAY ||
                statusCode == HttpStatus.SC_UNAUTHORIZED || statusCode == HttpStatus.SC_FORBIDDEN ||
                statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
            //TODO put error
            Logger.d(TAG, String.valueOf(statusCode));
            throw new AmttException(null, statusCode, this, null);
        }

        return restResponse;
    }

    public RestMethodType getRequestType() {
        return mRestMethodType;
    }

}
