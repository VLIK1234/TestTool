package amtt.epam.com.amtt.api.rest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Objects;

import amtt.epam.com.amtt.api.JiraException;
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

    private static HttpClient mHttpClient;
    private RestMethodType mRestMethodType;
    private Map<String, String> mHeaders;
    private String mUrl;
    private Processor<ResultType, HttpEntity> mProcessor;
    private String mPostEntity;


    static {
        mHttpClient = new DefaultHttpClient();
    }

    public RestMethod() {
    }

    private HttpResponse get() throws IOException {
        HttpGet httpGet = new HttpGet(mUrl);
        for (Map.Entry<String, String> keyValuePair : mHeaders.entrySet()) {
            httpGet.setHeader(keyValuePair.getKey(), keyValuePair.getValue());
        }

        HttpResponse httpResponse;
        try {
            httpResponse = mHttpClient.execute(httpGet);
        } catch (IllegalStateException e) {
            throw new IllegalStateException();
        } catch (UnknownHostException e) {
            throw new UnknownHostException();
        }
        return httpResponse;
    }

    private HttpResponse post() throws IOException {
        HttpPost httpPost = new HttpPost(mUrl);
        httpPost.setEntity(new StringEntity(mPostEntity));
        for (Map.Entry<String, String> keyValuePair : mHeaders.entrySet()) {
            httpPost.setHeader(keyValuePair.getKey(), keyValuePair.getValue());
        }
        return mHttpClient.execute(httpPost);
    }

    public RestResponse<ResultType> execute() throws Exception {
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

        int statusCode = httpResponse.getStatusLine().getStatusCode();
        ResultType result = null;
        try {
            if (mProcessor != null) {
                result = mProcessor.process(httpResponse.getEntity());
                restResponse.setResultObject(result);
            }
        } catch (Exception e) {
            throw new JiraException(e, httpResponse.getStatusLine().getStatusCode(), null);
        }

        if (statusCode == HttpStatus.SC_NOT_FOUND || statusCode == HttpStatus.SC_BAD_GATEWAY ||
                statusCode == HttpStatus.SC_UNAUTHORIZED || statusCode == HttpStatus.SC_FORBIDDEN ||
                statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
            //why the "result" is cast to String and set as result
            //Two cases can take place:
            //1. RestMethod must return String response -> request performed and error response is received
            // -> processor parses the response and give String back
            // -> we check status code and throw an exception
            // -> throw new JiraException(null, statusCode, (String)result); <-
            //2. RestMethod must return Entity -> request performed
            // -> processor parses the response and throws an exception as current response can't be processed in a proper way
            // -> exception is thrown, we catch it and throw JiraException with null "result"
            // -> throw new JiraException(e, httpResponse.getStatusLine().getStatusCode(), null); <-
            //Conclusion: the result passed to JiraException constructor can be only of String type=)
            throw new JiraException(null, statusCode, (String)result);
        }

        if (mRestMethodType == RestMethodType.POST) {
            restResponse.setOperationResult(JiraOperationResult.CREATED);
        }
        return restResponse;
    }

}
