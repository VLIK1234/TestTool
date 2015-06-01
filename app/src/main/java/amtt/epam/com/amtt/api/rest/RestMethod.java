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
import amtt.epam.com.amtt.processing.Processor;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * Class for performing REST methods to Jira api
 * Created by Artsiom_Kaliaha on 15.04.2015.
 */
public class RestMethod<ResultType> {

    private final String TAG = this.getClass().getSimpleName();

    public enum RestMethodType {

        GET,
        POST

    }

    public static class Builder<ResultType> {


        private RestMethodType mRestMethodType;
        private Map<String, String> mHeaders;
        private String mUrl;
        private Processor<ResultType, HttpEntity> mProcessor; //processor for retrieving OBJECTS
        private HttpEntity mPostEntity;

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

        public Builder setPostEntity(HttpEntity postEntity) {
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
    private HttpEntity mPostEntity;

    static {
        mHttpClient = new DefaultHttpClient();
    }

    public RestMethod() {
    }

    private HttpResponse get() throws AmttException {
        HttpGet httpGet = new HttpGet(mUrl);
        Logger.d(TAG, mUrl);
        for (Map.Entry<String, String> keyValuePair : mHeaders.entrySet()) {
            httpGet.setHeader(keyValuePair.getKey(), keyValuePair.getValue());
        }

        HttpResponse httpResponse;
        try {
            httpResponse = mHttpClient.execute(httpGet);
        } catch (IllegalStateException e) {
            Logger.e(TAG, e.getMessage());
            throw new AmttException(e, EMPTY_STATUS_CODE, this, null);
        } catch (IllegalArgumentException e) {
            Logger.e(TAG, e.getMessage());
            throw new AmttException(e, EMPTY_STATUS_CODE, this, null);
        } catch (UnknownHostException e) {
            Logger.e(TAG, e.getMessage());
            throw new AmttException(e, EMPTY_STATUS_CODE, this, null);
        } catch (ClientProtocolException e) {
            Logger.e(TAG, e.getMessage());
            throw new AmttException(e, EMPTY_STATUS_CODE, this, null);
        } catch (IOException e) {
            Logger.e(TAG, e.getMessage());
            throw new AmttException(e, EMPTY_STATUS_CODE, this, null);
        }
        return httpResponse;
    }

    private HttpResponse post() throws AmttException {
        HttpPost httpPost = new HttpPost(mUrl);
        Logger.d(TAG, mUrl);
        httpPost.setEntity(mPostEntity);

        for (Map.Entry<String, String> keyValuePair : mHeaders.entrySet()) {
            httpPost.setHeader(keyValuePair.getKey(), keyValuePair.getValue());
        }

        HttpResponse httpResponse;
        try {
            httpResponse = mHttpClient.execute(httpPost);
        } catch (ClientProtocolException e) {
            Logger.e(TAG, e.getMessage());
            throw new AmttException(e, EMPTY_STATUS_CODE, this, null);
        } catch (IOException e) {
            Logger.e(TAG, e.getMessage());
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

        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (mRestMethodType == RestMethodType.GET && statusCode != HttpStatus.SC_OK ||
                mRestMethodType == RestMethodType.POST && statusCode != HttpStatus.SC_CREATED) {
            throw new AmttException(null, statusCode, this, null);
        }

        RestResponse<ResultType> restResponse = new RestResponse<>();
        ResultType result;
        HttpEntity entity = null;
        try {
            if (mProcessor != null) {
                entity = httpResponse.getEntity();
                result = mProcessor.process(entity);
                restResponse.setResultObject(result);
            }
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
            throw prepareException(e, statusCode, entity);
        }

        return restResponse;
    }

    public RestMethodType getRequestType() {
        return mRestMethodType;
    }


    private AmttException prepareException(Exception e, int statusCode, HttpEntity entity) {
        AmttException amttException = new AmttException(e, statusCode, this);
        String entityString = null;
        try {
            entityString = EntityUtils.toString(entity, HTTP.UTF_8);
        } catch (IOException entityParseException) {
            Logger.e(TAG, e.getMessage());
            //TODO for reviewer: addSuppressed requires API19, project API is 14th
            //amttException.getSuppressedOne().addSuppressed(entityParseException);
            amttException.replaceSuppressedOne(entityParseException);
        }
        amttException.setEntity(entityString);
        return amttException;
    }

}
